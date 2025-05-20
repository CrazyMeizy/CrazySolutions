package client

import cats.Applicative
import cats.effect.Async
import cats.effect.kernel.Sync
import cats.effect.syntax.all._
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import sttp.client4.{Backend, asStringAlways, basicRequest}
import sttp.model.{StatusCode, Uri}

import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.xml.XML

trait CrawlerClient[F[_]] {
  def loadTitle(uri: Uri): F[Either[String, String]]
}

object CrawlerClient {

  def make[F[_]: Async](
      backend: Backend[F],
      timeout: FiniteDuration = 3.seconds,
  ): CrawlerClient[F] =
    new CrawlerClientImpl[F](backend, timeout)

  private def extractTitle[F[_]: Sync](html: String): F[Either[String, String]] =
    Sync[F].blocking {
      val xml = XML.loadString(html)
      Right((xml \\ "title").text.trim): Either[String, String]
    }.handleErrorWith(_ => Applicative[F].pure(Left("Title parsing error")))

  private class CrawlerClientImpl[F[_]: Async](
      backend: Backend[F],
      timeout: FiniteDuration = 3.seconds
  ) extends CrawlerClient[F] {

    override def loadTitle(uri: Uri): F[Either[String, String]] = {
      val request = basicRequest
        .get(uri)
        .response(asStringAlways)

      request
        .send(backend)
        .flatMap {
          case response if response.code == StatusCode.Ok =>
            extractTitle(response.body)
          case response => Applicative[F].pure[Either[String, String]](Left(s"HTTP Error: ${response.code} ($uri)"))
        }
        .timeout(timeout)
        .handleErrorWith(ex =>
          Applicative[F].pure[Either[String, String]](Left(s"Request failed: ${ex.getMessage} ($uri)"))
        )
    }
  }
}
