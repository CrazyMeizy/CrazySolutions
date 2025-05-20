package domain

import sttp.model.Uri
import sttp.model.Uri.UriContext
import sttp.tapir.Schema
import tethys.derivation.semiauto.{jsonReader, jsonWriter}
import tethys.readers.ReaderError
import tethys.{JsonReader, JsonWriter}

import scala.annotation.nowarn

case class CrawlerRequest(
    uri: List[Uri]
)

@nowarn
object CrawlerRequest {
  implicit val jsonReaderUri: JsonReader[Uri] = JsonReader
    .stringReader
    .mapWithField(fieldName =>
      Uri.parse(_).fold(
        ReaderError.wrongJson(_)(fieldName),
        identity
      )
    )
  implicit val jsonWriterUri: JsonWriter[Uri] = JsonWriter.stringWriter.contramap(_.toString)

  implicit val jsonReaderCrawlerRequest: JsonReader[CrawlerRequest] = jsonReader[CrawlerRequest]
  implicit val jsonWriterCrawlerRequest: JsonWriter[CrawlerRequest] = jsonWriter[CrawlerRequest]
  implicit val schemaCrawlerRequest: Schema[CrawlerRequest] =
    Schema.derived[CrawlerRequest]

  val example: CrawlerRequest = CrawlerRequest(
    List(
      uri"https://www.google.com",
      uri"https://www.yandex.ru",
      uri"https://scala-lang.org",
    )
  )
}
