package service

import cats.Functor
import cats.effect.Ref
import cats.effect.kernel.Sync
import cats.syntax.functor._
import cats.syntax.option._
import service.AuthService.RegistrationResult
import sttp.tapir.model.UsernamePassword

trait AuthService[F[_]] {
  def login(usernamePass: UsernamePassword): F[Option[String]]
  def register(usernamePass: UsernamePassword): F[RegistrationResult]
}

object AuthService {

  def make[I[_]: Sync, F[_]: Sync]: I[AuthService[F]] =
    Ref.in[I, F, Map[String, Option[String]]](Map.empty[String, Option[String]])
      .map(new AuthServiceImpl[F](_))

  def populate[F[_]: Functor](service: AuthService[F]): F[Unit] =
    service.register(UsernamePassword("admin", "admin".some)).void

  sealed trait RegistrationResult

  case object Success extends RegistrationResult
  case object Failure extends RegistrationResult

  private class AuthServiceImpl[F[_]: Functor](
      store: Ref[F, Map[String, Option[String]]]
  ) extends AuthService[F] {

    override def login(
        usernamePass: UsernamePassword
    ): F[Option[String]] = {
      val password = usernamePass.password
      store.get
        .map(_.get(usernamePass.username) match {
          case Some(`password`) => Some(usernamePass.username)
          case _                => None
        })
    }

    override def register(
        usernamePass: UsernamePassword
    ): F[RegistrationResult] =
      store.modify(map => // atomically update the map
        if (map.contains(usernamePass.username))
          (map, Failure)
        else
          (map + (usernamePass.username -> usernamePass.password), Success)
      )
  }

}
