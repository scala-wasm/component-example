package httpclient

import java.util.Optional

import scala.scalajs.wasi.http.{outgoing_handler, types => http}
import scala.scalajs.wasi.io.streams.StreamError

import WitConversion._

object HttpClient {
  def get(authority: String, path: String): http.IncomingResponse = {
    val request = http.OutgoingRequest(http.Fields())
    val future = (for {
      _ <- request.setMethod(http.Method.Get)
      _ <- request.setScheme(Optional.of(http.Scheme.Https))
      _ <- request.setAuthority(Optional.of(authority))
      _ <- request.setPathWithQuery(Optional.of(path))
      future <- outgoing_handler
        .handle(request, Optional.empty[http.RequestOptions]())
    } yield future).fold(err => throw new RuntimeException(s"request failed: $err"), identity)

    val pollable = future.subscribe()
    try pollable.block()
    finally pollable.close()

    (for {
      outer <- future.get()
    } yield {
      (for {
        inner <- outer
        response <- inner
      } yield response).fold(err => throw new RuntimeException(s"response failed: $err"), identity)
    }).getOrElse(throw new RuntimeException("response is not ready"))
  }

  def readBody(response: http.IncomingResponse): String = {
    val body = response.consume().getOrElse(
      throw new RuntimeException("failed to consume response body"))
    val stream = body.stream().getOrElse(
      throw new RuntimeException("failed to open response stream"))
    val bytes = scala.collection.mutable.ArrayBuffer.empty[Byte]
    var done = false

    while (!done) {
      stream.blockingRead(65536L).fold(
        {
          case StreamError.Closed =>
            done = true
          case err =>
            throw new RuntimeException(s"failed to read response body: $err")
        },
        chunk => bytes ++= chunk.map(_.toByte))
    }

    new String(bytes.toArray, "UTF-8")
  }
}
