package httpclient

import java.util.Optional

import scala.scalajs.WitConversions._
import scala.scalajs.wasi.http.{outgoing_handler, types => http}
import scala.scalajs.wasi.io.streams.StreamError

object Main {
  def main(args: Array[String]): Unit = {
    val response = get("httpbin.org", "/get")
    val body = readBody(response)

    println(s"Status: ${response.status()}")
    println(body.take(200))
  }

  private def get(authority: String, path: String): http.IncomingResponse = {
    val request = http.OutgoingRequest(http.Fields())
    request.setMethod(http.Method.Get).toEither.getOrElse(
      throw new RuntimeException("failed to set method"))
    request.setScheme(Optional.of(http.Scheme.Https)).toEither.getOrElse(
      throw new RuntimeException("failed to set scheme"))
    request.setAuthority(Optional.of(authority)).toEither.getOrElse(
      throw new RuntimeException("failed to set authority"))
    request.setPathWithQuery(Optional.of(path)).toEither.getOrElse(
      throw new RuntimeException("failed to set path"))

    val future = outgoing_handler.handle(request, Optional.empty[http.RequestOptions]()).toEither match {
      case Right(future) => future
      case Left(err)    => throw new RuntimeException(s"request rejected: $err")
    }
    val pollable = future.subscribe()
    try pollable.block()
    finally pollable.close()

    val outer = future.get().toOption.getOrElse(
      throw new RuntimeException("response is not ready"))
    val inner = outer.toEither.getOrElse(
      throw new RuntimeException("response was already consumed"))
    inner.toEither match {
      case Right(response) => response
      case Left(err)       => throw new RuntimeException(s"HTTP error: $err")
    }
  }

  private def readBody(response: http.IncomingResponse): String = {
    val body = response.consume().toEither.getOrElse(
      throw new RuntimeException("failed to consume response body"))
    val stream = body.stream().toEither.getOrElse(
      throw new RuntimeException("failed to open response stream"))
    val bytes = scala.collection.mutable.ArrayBuffer.empty[Byte]
    var done = false

    while (!done) {
      stream.blockingRead(65536L).toEither match {
        case Right(chunk) =>
          bytes ++= chunk.map(_.toByte)
        case Left(StreamError.Closed) =>
          done = true
        case Left(err) =>
          throw new RuntimeException(s"failed to read response body: $err")
      }
    }

    new String(bytes.toArray, "UTF-8")
  }
}
