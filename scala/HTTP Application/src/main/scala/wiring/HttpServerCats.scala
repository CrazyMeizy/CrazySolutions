package wiring

import cats.effect.std.{Console, Dispatcher}
import cats.effect.{Async, Resource, Sync}
import cats.syntax.functor._
import config.{ServerConfig, ZoneConfig}
import controller.Controller
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import sttp.apispec.openapi.circe.yaml.RichOpenAPI
import sttp.capabilities.WebSockets
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir.docs.openapi.{OpenAPIDocsInterpreter, OpenAPIDocsOptions}
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics
import sttp.tapir.server.vertx.cats.VertxCatsServerInterpreter.VertxFutureToCatsF
import sttp.tapir.server.vertx.cats.{VertxCatsServerInterpreter, VertxCatsServerOptions}
import sttp.tapir.swagger.SwaggerUI

final case class HttpServerCats(
    public: HttpServer,
    monitoring: HttpServer,
)

object HttpServerCats {

  private def withDocs[F[_], R](
      endpoints: List[ServerEndpoint[R, F]],
      config: ZoneConfig
  ): List[ServerEndpoint[R, F]] = {
    val openApiOptions: OpenAPIDocsOptions = OpenAPIDocsOptions.default

    val openApi: String =
      OpenAPIDocsInterpreter(openApiOptions)
        .toOpenAPI(es = endpoints.map(_.endpoint), config.name, "1.0")
        .toYaml

    if (config.swaggerEnabled) endpoints ::: SwaggerUI[F](openApi) else endpoints
  }

  private def registerZone[F[_]: Async](
      endpoints: List[ServerEndpoint[Fs2Streams[F] with WebSockets, F]],
      config: ZoneConfig,
      vertx: Vertx,
      options: VertxCatsServerOptions[F]
  ): Resource[F, HttpServer] =
    Resource.make {
      Sync[F].defer {
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)

        endpoints.foreach(
          VertxCatsServerInterpreter(options)
            .route(_)
            .apply(router)
        )

        server
          .requestHandler(router)
          .listen(config.port, config.host)
          .asF[F]
      }
    }(_.close().asF[F].void)

  def startServer[F[_]: Async: Console](
      publicApi: List[Controller[F]],
      config: ServerConfig,
  ): Resource[F, HttpServerCats] =
    for {
      _ <- Resource.make(Console[F].println("Starting server..."))(_ => Console[F].println("Server stopped"))

      dispatcher <- Dispatcher.parallel[F]
      prometheusMetrics = PrometheusMetrics.default[F](namespace = "http")

      publicEndpoints     = withDocs(publicApi.flatMap(_.all), config.internal)
      monitoringEndpoints = withDocs(List(prometheusMetrics.metricsEndpoint), config.monitoring)
        
      serverOptions: VertxCatsServerOptions[F] =
        VertxCatsServerOptions
          .customiseInterceptors[F](dispatcher)
          .metricsInterceptor(prometheusMetrics.metricsInterceptor())
          .options

      server <- Resource.make(Sync[F].delay(Vertx.vertx()))(_.close().asF[F].void)
        
      publicServer     <- registerZone[F](publicEndpoints, config.internal, server, serverOptions)
      monitoringServer <- registerZone[F](monitoringEndpoints, config.monitoring, server, serverOptions)

      _ <- Resource.eval(Console[F].println(s"Started http://localhost:${publicServer.actualPort()}/docs"))
      _ <- Resource.eval(Console[F].println(s"Started http://localhost:${monitoringServer.actualPort()}/docs"))

      _ <- Resource.make(Console[F].println("Server started"))(_ => Console[F].println("Closing server..."))
    } yield HttpServerCats(publicServer, monitoringServer)

}
