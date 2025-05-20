package controller

import sttp.capabilities.WebSockets
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir.server.ServerEndpoint

trait Controller[F[_]] {
  def all: List[ServerEndpoint[Fs2Streams[F] with WebSockets, F]]
}
