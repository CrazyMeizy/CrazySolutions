package data

import unmarshal.decoder.Decoder
import unmarshal.encoder.Encoder
import unmarshal.error.DecoderError.wrongJson
import unmarshal.error.DecoderError
import unmarshal.model.Json
import unmarshal.model.Json._

case class Employee(
  name: String,
  age: Long,
  id: Long,
  bossId: Option[Long]
)

object Employee {

  private val validKeys = Set("name", "age", "id", "bossId")

  implicit def employeeEncoder: Encoder[Employee] = { employee: Employee =>
    JsonObject(
      Map(
        "name" -> JsonString(employee.name),
        "age"  -> JsonNum(employee.age),
        "id"   -> JsonNum(employee.id),
        "bossId" -> {
          employee.bossId match {
            case Some(x) => JsonNum(x)
            case None    => JsonNull
          }
        }
      )
    )
  }

  def getEitherFromMap[T](map: Map[String, Json], key: String)(
    extract: (Json, String) => Either[DecoderError, T]
  ): Either[DecoderError, T] =
    map.get(key) match {
      case Some(json: Json) => extract(json, key)
      case None             => Left(wrongJson("no value for field", key))
    }

  def extractFromString(json: Json, key: String): Either[DecoderError, String] = json match {
    case JsonString(value) => Right(value)
    case _                 => Left(wrongJson("Not String value", key))
  }

  def extractFromNum(json: Json, key: String): Either[DecoderError, Long] = json match {
    case JsonNum(value) => Right(value)
    case _              => Left(wrongJson("Not Long value", key))
  }

  def extractFromOptionNum(json: Json, key: String): Either[DecoderError, Option[Long]] =
    json match {
      case JsonNum(value) => Right(Some(value))
      case JsonNull       => Right(None)
      case _              => Left(wrongJson("Not Option[Long] value", key))
    }

  def checkFields(map: Map[String, Json]): Either[DecoderError, Unit] =
    map.keySet.diff(validKeys).headOption match {
      case Some(key) => Left(wrongJson(s"No such field in class", key))
      case None      => Right(())
    }

  implicit def employeeDecoder: Decoder[Employee] = {
    case JsonObject(map: Map[String, Json]) =>
      for {
        _      <- checkFields(map)
        name   <- getEitherFromMap(map, "name")(extractFromString)
        age    <- getEitherFromMap(map, "age")(extractFromNum)
        id     <- getEitherFromMap(map, "id")(extractFromNum)
        bossId <- getEitherFromMap(map, "bossId")(extractFromOptionNum)
      } yield Employee(name, age, id, bossId)
    case _ => Left(wrongJson("Not a JsonObject", ""))
  }

}
