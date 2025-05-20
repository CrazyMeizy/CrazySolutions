package endpoint

import sttp.model.StatusCode
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.model.UsernamePassword
import sttp.tapir.{Endpoint, PublicEndpoint, auth, endpoint, statusCode, stringBody}
import tethys.JsonObjectWriter.lowPriorityWriter
import tethys.derivation.auto.{jsonReaderMaterializer, jsonWriterMaterializer}

import scala.annotation.nowarn

@nowarn
object RegisterEndpoints {

  private val registerEndpoint: PublicEndpoint[Unit, Unit, Unit, Any] =
    endpoint
      .tag("User authentication")
      .in("user")

  val registerUser: PublicEndpoint[UsernamePassword, String, String, Any] =
    registerEndpoint.post
      .summary("Registers new user")
      .in("register")
      .in(jsonBody[UsernamePassword])
      .out(stringBody)
      .errorOut(stringBody and statusCode(StatusCode.BadRequest))

  val checkUser: Endpoint[UsernamePassword, Unit, String, String, Any] =
    registerEndpoint.get
      .summary("Checks if user exists")
      .in("check")
      .securityIn(auth.basic[UsernamePassword]())
      .out(stringBody)
      .errorOut(stringBody and statusCode(StatusCode.Unauthorized))

}
