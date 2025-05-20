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

  /** Generate the OpenAPI spec by the given endpoints, and then add swagger endpoint to endpoints.
    */
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

  /** Immediately starts the server with the given endpoints on the given port (from [[ZoneConfig]]).
    */
  private def registerZone[F[_]: Async](
      endpoints: List[ServerEndpoint[Fs2Streams[F] with WebSockets, F]],
      config: ZoneConfig,
      vertx: Vertx,
      options: VertxCatsServerOptions[F]
  ): Resource[F, HttpServer] =
    Resource.make {
      Sync[F].defer {
        // server and router are mutable classes, so we have a new one for each zone
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)

        // register each endpoint in router
        endpoints.foreach(
          VertxCatsServerInterpreter(options)
            .route(_)
            .apply(router)
        )

        // register router with all endpoints in server, and start server (listen)
        server
          .requestHandler(router)
          .listen(config.port, config.host)
          .asF[F]
      }
    }(_.close().asF[F].void) // close server on exit

  def startServer[F[_]: Async: Console](
      publicApi: List[Controller[F]],
      config: ServerConfig,
  ): Resource[F, HttpServerCats] =
    for {
      _ <- Resource.make(Console[F].println("Starting server..."))(_ => Console[F].println("Server stopped"))

      dispatcher <- Dispatcher.parallel[F] // create dispatcher for server
      prometheusMetrics = PrometheusMetrics.default[F](namespace = "http") // register metrics collectors

      // Get all tapir endpoints and add swagger endpoint for them
      publicEndpoints     = withDocs(publicApi.flatMap(_.all), config.internal)
      monitoringEndpoints = withDocs(List(prometheusMetrics.metricsEndpoint), config.monitoring)

      // Server ops (register metrics)
      serverOptions: VertxCatsServerOptions[F] =
        VertxCatsServerOptions
          .customiseInterceptors[F](dispatcher)
          .metricsInterceptor(prometheusMetrics.metricsInterceptor())
          .options

      // Init server runtime
      server <- Resource.make(Sync[F].delay(Vertx.vertx()))(_.close().asF[F].void)

      // Starts the server in the 2 zones
      publicServer     <- registerZone[F](publicEndpoints, config.internal, server, serverOptions)
      monitoringServer <- registerZone[F](monitoringEndpoints, config.monitoring, server, serverOptions)

      _ <- Resource.eval(Console[F].println(s"Started http://localhost:${publicServer.actualPort()}/docs"))
      _ <- Resource.eval(Console[F].println(s"Started http://localhost:${monitoringServer.actualPort()}/docs"))

      _ <- Resource.make(Console[F].println("Server started"))(_ => Console[F].println("Closing server..."))
    } yield HttpServerCats(publicServer, monitoringServer)

}
