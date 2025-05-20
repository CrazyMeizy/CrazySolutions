package endpoint

import domain.AnimalInfo
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.model.UsernamePassword

object AnimalEndpoints {

  private val animalEndpoint =
    endpoint
      .tag("Animal information")
      .in("animal")

  val allAnimals: PublicEndpoint[Unit, Unit, List[String], Any] =
    animalEndpoint.get
      .summary("Returns all animals names")
      .in("all")
      .out(jsonBody[List[String]])

  val animalInfo: Endpoint[UsernamePassword, String, String, AnimalInfo, Any] =
    animalEndpoint.get
      .summary("Returns info about animal")
      .securityIn(auth.basic[UsernamePassword]())
      .in(query[String]("name"))
      .out(jsonBody[AnimalInfo])
      .errorOut(stringBody.and(statusCode(StatusCode.NotFound)))

  val animalAdd: PublicEndpoint[AnimalInfo, Unit, Unit, Any] =
    animalEndpoint.post
      .summary("Creates new info about animal")
      .in(jsonBody[AnimalInfo])
}
