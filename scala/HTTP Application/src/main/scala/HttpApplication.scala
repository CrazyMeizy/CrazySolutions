import cats.effect.{ExitCode, IO, IOApp, Resource}
import client.CrawlerClient
import config.AppConfiguration
import controller.{AnimalApi, RegisterApi}
import pureconfig.ConfigSource
import service.{AnimalService, AuthService, CrawlerService}
import sttp.client4.httpclient.cats.HttpClientCatsBackend
import wiring.HttpServerCats

object HttpApplication extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    def application: Resource[IO, HttpServerCats] = for {
      _ <- Resource.make(IO.println("Starting application..."))(_ => IO.println("Application stopped"))

      // load configuration
      config  <- AppConfiguration.unsafeLoad[IO](ConfigSource.default).toResource
      backend <- HttpClientCatsBackend.resource[IO]()

      crawlerClient = CrawlerClient.make(backend)

      // create services
      authService   <- AuthService.make[Resource[IO, *], IO]
      animalService <- AnimalService.make[Resource[IO, *], IO]
      _ = CrawlerService.make(crawlerClient)
      _ <- AnimalService.populate(animalService).toResource
      _ <- AuthService.populate(authService).toResource

      // create controllers
      animalApi = AnimalApi.make[IO](animalService, authService)
      authApi   = RegisterApi.make[IO](authService)

      // start server
      httpServer <- HttpServerCats.startServer[IO](List(animalApi, authApi), config.server)

      _ <- Resource.make(IO.println("Application started"))(_ => IO.println("Closing application..."))
    } yield httpServer

    // run application
    application.useForever
      .as(ExitCode.Success)
      .handleErrorWith { exception => // handle all errors
        IO.println(s"Error: ${exception.getMessage}") >>
          IO.println("Application exited with error") >>
          IO.pure(ExitCode.Error)
      }
      .onCancel(IO.println("Application cancelled"))
  }
}
