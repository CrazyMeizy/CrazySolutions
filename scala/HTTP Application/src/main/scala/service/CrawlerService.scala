package service

import cats.implicits.{catsSyntaxParallelTraverse1, toFunctorOps}
import cats.{Applicative, Parallel}
import client.CrawlerClient
import domain.{CrawlerRequest, CrawlerResponse}

trait CrawlerService[F[_]] {
  def crawler(request: CrawlerRequest): F[CrawlerResponse]
}

object CrawlerService {

  def make[F[_]: Parallel: Applicative](client: CrawlerClient[F]): CrawlerService[F] =
    new CrawlerServiceImpl(client)

  private class CrawlerServiceImpl[F[_]: Parallel: Applicative](
      client: CrawlerClient[F]
  ) extends CrawlerService[F] {

    override def crawler(request: CrawlerRequest): F[CrawlerResponse] =
      request.uri
        .parTraverse(client.loadTitle)
        .map(CrawlerResponse(_))
  }

}
