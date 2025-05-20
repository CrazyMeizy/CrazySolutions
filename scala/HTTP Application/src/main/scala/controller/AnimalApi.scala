package controller

import cats.Monad
import cats.effect.std.Console
import cats.implicits.toFunctorOps
import cats.syntax.flatMap._
import endpoint.AnimalEndpoints
import service.{AnimalService, AuthService}
import sttp.tapir.server.ServerEndpoint

private class AnimalApi[F[_]: Monad: Console](
    animalService: AnimalService[F],
    authService: AuthService[F],
) extends Controller[F] {

  private val allAnimals: ServerEndpoint[Any, F] =
    AnimalEndpoints.allAnimals
      .serverLogicSuccess { _ =>
        animalService.getAnimals
      }

  private val animalInfo: ServerEndpoint[Any, F] =
    AnimalEndpoints.animalInfo
      .serverSecurityLogic { usernamePass =>
        authService.login(usernamePass).map {
          case Some(username) => Right(username)
          case _              => Left("Invalid username or password")
        }
      }
      .serverLogic { user => animalName =>
        Console[F].println(s"User $user requested info about $animalName") >>
          animalService.getAnimalByName(animalName).map {
            case Some(value) => Right(value)
            case None        => Left("Animal not found")
          }
      }

  private val animalAdd: ServerEndpoint[Any, F] =
    AnimalEndpoints.animalAdd
      .serverLogicSuccess { animalInfo =>
        animalService.addAnimal(animalInfo)
      }

  override def all: List[ServerEndpoint[Any, F]] =
    List(allAnimals, animalInfo, animalAdd)
}

object AnimalApi {

  def make[F[_]: Monad: Console](
      animalService: AnimalService[F],
      authService: AuthService[F],
  ): Controller[F] =
    new AnimalApi[F](animalService, authService)

}
