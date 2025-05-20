package data

import cats.implicits.toTraverseOps
import unmarshal.decoder.Decoder
import unmarshal.encoder.Encoder
import unmarshal.error.DecoderError
import unmarshal.error.DecoderError.wrongJson
import unmarshal.model.Json
import unmarshal.model.Json._

case class CompanyEmployee(
  employees: List[Employee]
)

object CompanyEmployee {

  private val validKeys = Set("employees")

  implicit def companyEmployeeEncoder: Encoder[CompanyEmployee] = { company: CompanyEmployee =>
    JsonObject(
      Map(
        "employees" -> JsonArray(
          company.employees.map(employee => Encoder[Employee].toJson(employee))
        )
      )
    )
  }

  def checkFields(map: Map[String, Json]): Either[DecoderError, Unit] =
    map.keySet.diff(validKeys).headOption match {
      case Some(key) => Left(wrongJson(s"No such field in class", key))
      case None      => Right(())
    }

  def getEitherFromMap[T](map: Map[String, Json], key: String)(
    extract: (Json, String) => Either[DecoderError, T]
  ): Either[DecoderError, T] =
    map.get(key) match {
      case Some(json: Json) => extract(json, key)
      case None             => Left(wrongJson("no value for field", key))
    }

  def extractFromJArray(json: Json, key: String): Either[DecoderError, List[Json]] = json match {
    case JsonArray(value) => Right(value)
    case _                => Left(wrongJson("Not List value", key))
  }

  implicit def companyEmployeeDecoder: Decoder[CompanyEmployee] = {
    case JsonObject(map: Map[String, Json]) =>
      for {
        _        <- checkFields(map)
        jsonList <- getEitherFromMap(map, "employees")(extractFromJArray)
        employees <- jsonList.zipWithIndex.map { case (json, idx) =>
          Decoder[Employee]
            .fromJson(json)
            .left
            .map(err => wrongJson(err.message, s"employees.$idx." + err.field))
        }.sequence
      } yield CompanyEmployee(employees)
    case _ => Left(wrongJson("Not a JsonObject", ""))
  }

}
