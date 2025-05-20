package db_app

import cats.Monad
import cats.effect._
import cats.effect.std.Console
import cats.implicits.{toFlatMapOps, toFunctorOps, toTraverseOps}
import db_app.config.{AppConfiguration, DbConfig}
import db_app.repository.{PeopleRepository, PeopleRepositoryDoobie}
import doobie.WeakAsync
import doobie.WeakAsync.doobieWeakAsyncForAsync
import doobie.hikari.HikariTransactor
import doobie.util.transactor.Transactor
import pureconfig.ConfigSource

import scala.annotation.unused
import scala.util.matching.Regex

object DbApplication extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    def application: Resource[IO, IO[Unit]] = for {
      _ <- Resource.make(IO.println("Starting application..."))(_ => IO.println("Application stopped"))

      // load configuration
      config <- AppConfiguration.unsafeLoad[IO](ConfigSource.default).toResource

      // init Db
      transactor <- hikariTransactor[IO](config.db)
      _          <- LiquibaseMigration.run(transactor).toResource

      // init repo
      repo = PeopleRepositoryDoobie.make[IO](transactor)

      // run console loop
      result <- consoleLoop[IO](repo).background

      _ <- Resource.make(IO.println("Application started"))(_ => IO.println("Closing application..."))
    } yield result.void

    // run application
    application.use(identity) // wait until console loop ends
      .as(ExitCode.Success)
      .handleErrorWith { exception => // handle all errors
        IO.println(s"Error: ${exception.getMessage}") >>
          IO.println("Application exited with error") >>
          IO.pure(ExitCode.Error)
      }
      .onCancel(IO.println("Application cancelled"))
  }

  @unused
  // example how to use Db without hikari pool
  private def defaultTransactor[F[_]: Async](
      config: DbConfig
  ): Transactor[F] =
    Transactor.fromDriverManager[F](
      driver = config.driver,
      url = config.url,
      user = config.user,
      password = config.password,
      logHandler = None
    )

  // example how to use with hikari pool
  private def hikariTransactor[F[_]: Async](
      config: DbConfig
  ): Resource[F, Transactor[F]] =
    HikariTransactor.fromHikariConfig[F](
      DbConfig.toHikariConf(config)
    )

  private def consoleLoop[F[_]: WeakAsync: Console](repo: PeopleRepository[F]): F[Unit] =
    Monad[F].iterateUntil {
      Console[F].readLine.flatTap {
        case getAll()        => repo.getAll.evalTap(Console[F].println).compile.drain.void
        case getById(id)     => id.toLongOption.traverse(repo.getById(_).flatMap(_.traverse(Console[F].println))).void
        case getByName(name) => repo.getLikeName(name).flatMap(_.traverse(Console[F].println)).void
        case addOne(age, name) =>
          age.toIntOption
            .traverse(repo.addOne(_, name).flatMap(cnt => Console[F].println(s"id of new person is $cnt")))
            .void
        case updateName(id, name) =>
          id.toLongOption
            .traverse(repo.updateName(_, name).flatMap(cnt => Console[F].println(s"rows updated: $cnt")))
            .void
        case swapRecords(id1, id2) =>
          (id1.toLongOption, id2.toLongOption) match {
            case (Some(leftId), Some(rightId)) =>
              repo.swap(leftId, rightId)
                .flatMap(cnt => Console[F].println(s"rows updated: $cnt"))
            case _ => Console[F].println("Invalid id").void
          }
        case exit()  => Console[F].println("exit").void
        case command => Console[F].println(s"unknown command '$command'").void
      }
    }(exit.matches(_))
      .void

  private val exit: Regex        = """^exit$""".r
  private val getAll: Regex      = """^all$""".r
  private val getById: Regex     = """^id (\d+)$""".r
  private val getByName: Regex   = """^like ([\w%]+)$""".r
  private val addOne: Regex      = """^add (\d+) (\w+)$""".r
  private val updateName: Regex  = """^update (\d+) (\w+)$""".r
  private val swapRecords: Regex = """^swap (\d+) (\d+)$""".r

}
