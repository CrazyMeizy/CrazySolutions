package controller

import cats.implicits.toFunctorOps
import cats.{Applicative, Monad}
import endpoint.RegisterEndpoints
import service.AuthService
import sttp.tapir.server.ServerEndpoint

private class RegisterApi[F[_]: Monad](
    authService: AuthService[F],
) extends Controller[F] {

  private val registerUser =
    RegisterEndpoints.registerUser
      .serverLogic { usernamePass =>
        authService.register(usernamePass).map {
          case AuthService.Success => Right(s"User ${usernamePass.username} registered")
          case AuthService.Failure => Left(s"User already exists")
        }
      }

  private val checkUser =
    RegisterEndpoints.checkUser
      .serverSecurityLogic { usernamePass =>
        authService
          .login(usernamePass)
          .map(_.toRight(s"User or password is incorrect"))
      }
      .serverLogicSuccess { username => _ =>
        Applicative[F].pure(s"User $username exists")
      }

  override def all: List[ServerEndpoint[Any, F]] =
    List(registerUser, checkUser)
}

object RegisterApi {

  def make[F[_]: Monad](authService: AuthService[F]): Controller[F] =
    new RegisterApi[F](authService)

}
