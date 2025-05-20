package domain

import sttp.tapir.Schema
import tethys.derivation.semiauto.{jsonReader, jsonWriter}
import tethys.{JsonReader, JsonWriter}

import scala.annotation.nowarn

case class CrawlerResponse(
    uri: List[Either[String, String]]
)

@nowarn
object CrawlerResponse {
  implicit val jsonReaderEither: JsonReader[Either[String, String]] =
    JsonReader.stringReader.map(Right(_))

  implicit val jsonReaderCrawlerResponse: JsonReader[CrawlerResponse] = jsonReader[CrawlerResponse]
  implicit val jsonWriterCrawlerResponse: JsonWriter[CrawlerResponse] = jsonWriter[CrawlerResponse]
  implicit val schemaCrawlerResponse: Schema[CrawlerResponse] =
    Schema.derived[CrawlerResponse]

  val example: CrawlerResponse = CrawlerResponse(
    List(
      Right("Google"),
      Left("Timeout to load page https://www.yandex.ru"),
      Right("scala-lang"),
    )
  )
}
