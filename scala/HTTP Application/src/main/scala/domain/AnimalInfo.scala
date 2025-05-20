package domain

import sttp.tapir.{Schema, Validator}
import tethys.derivation.semiauto.{jsonReader, jsonWriter}
import tethys.{JsonReader, JsonWriter}

import scala.annotation.nowarn

final case class AnimalInfo(
    name: String,
    description: String,
    maxAge: Int,
)

@nowarn
object AnimalInfo {
  implicit val jsonReaderAnimalInfo: JsonReader[AnimalInfo] = jsonReader[AnimalInfo]
  implicit val jsonWriterAnimalInfo: JsonWriter[AnimalInfo] = jsonWriter[AnimalInfo]
  implicit val schemaAnimalInfo: Schema[AnimalInfo] =
    Schema.derived[AnimalInfo]
      .modify(_.name)(_.description("Animal name"))
      .modify(_.description)(_.description("Animal description"))
      .modify(_.maxAge)(
        _.description("Average max age of the animal")
          .validate(Validator.min(0, exclusive = true) and Validator.max(1000))
      )
      .description("Information about animal")
}
