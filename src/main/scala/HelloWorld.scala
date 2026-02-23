package example

import scalajs.wit.annotation._
import scalajs.wit._

import componentmodel.exports.scala_wasm.component_example
import scalajs.wasi.http.{types => http}
import scalajs.wasi.http.outgoing_handler

@WitImplementation
object GreetImpl extends component_example.Greet {
  def greet(): Unit = {
    testPureWasmLibraryPaths()
    println("Making an HTTP GET request to httpbin.org/get ...")

    // 1. Create headers
    val headers = http.Fields()

    // 2. Create outgoing request
    val request = http.OutgoingRequest(headers)
    request.setMethod(http.Method.Get)
    request.setScheme(java.util.Optional.of(http.Scheme.Https))
    request.setAuthority(java.util.Optional.of("httpbin.org"))
    request.setPathWithQuery(java.util.Optional.of("/get"))

    // 3. Send the request via outgoing-handler
    val handleResult = outgoing_handler.handle(request, java.util.Optional.empty())
    val futureResponse = handleResult match {
      case ok: Ok[_] => ok.value.asInstanceOf[http.FutureIncomingResponse]
      case err: Err[_] =>
        println(s"Error sending request: ${err.value}")
        return
    }

    // 4. Poll until the response is ready
    val pollable = futureResponse.subscribe()
    pollable.block()

    // 5. Get the response
    val responseOpt = futureResponse.get()
    if (!responseOpt.isPresent()) {
      println("Error: response not ready")
      return
    }
    val outerResult = responseOpt.get()
    val innerResult = outerResult match {
      case ok: Ok[_] => ok.value.asInstanceOf[Result[http.IncomingResponse, http.ErrorCode]]
      case _: Err[_] =>
        println("Error: already consumed")
        return
    }
    val response = innerResult match {
      case ok: Ok[_] => ok.value.asInstanceOf[http.IncomingResponse]
      case err: Err[_] =>
        println(s"HTTP error: ${err.value}")
        return
    }

    // 6. Read status
    val status = response.status()
    println(s"Status: $status")

    // 7. Read the response body
    val bodyResult = response.consume()
    val incomingBody = bodyResult match {
      case ok: Ok[_] => ok.value.asInstanceOf[http.IncomingBody]
      case _: Err[_] =>
        println("Error: could not consume body")
        return
    }

    val streamResult = incomingBody.stream()
    val stream = streamResult match {
      case ok: Ok[_] => ok.value.asInstanceOf[http.InputStream]
      case _: Err[_] =>
        println("Error: could not get stream")
        return
    }

    // Read body chunks
    val bodyBuilder = new StringBuilder()
    var done = false
    while (!done) {
      stream.blockingRead(65536L) match {
        case ok: Ok[_] =>
          val bytes = ok.value.asInstanceOf[Array[scala.scalajs.wit.unsigned.UByte]]
          val byteArray = bytes.map(_.toByte)
          bodyBuilder.append(new String(byteArray, "UTF-8"))
        case _: Err[_] =>
          done = true
      }
    }

    println(s"Body:\n${bodyBuilder.toString()}")
  }

  private def testPureWasmLibraryPaths(): Unit = {
    // --- BoxesRunTime.equals (used by == on boxed types) ---
    val a: Any = 42
    val b: Any = 42
    val c: Any = 43
    assert(a == b, "BoxesRunTime.equals: equal ints")
    assert(!(a == c), "BoxesRunTime.equals: unequal ints")
    val d: Any = Double.NaN
    assert(!(d == d), "BoxesRunTime.equals: NaN != NaN")
    val e: Any = "hello"
    val f: Any = "hello"
    assert(e == f, "BoxesRunTime.equals: equal strings")

    // --- Symbol cache (uses HashMap under pure Wasm instead of js.Dictionary) ---
    val s1 = Symbol("test")
    val s2 = Symbol("test")
    assert(s1 eq s2, "Symbol: interning via pure Wasm cache")

    // --- Buffer (uses ArrayBuffer under pure Wasm instead of js.WrappedArray) ---
    val buf = scala.collection.mutable.Buffer(1, 2, 3)
    buf += 4
    assert(buf.length == 4, "Buffer: basic operations")
    assert(buf(3) == 4, "Buffer: element access")

    val ibuf = scala.collection.mutable.IndexedBuffer(10, 20)
    ibuf += 30
    assert(ibuf.length == 3, "IndexedBuffer: basic operations")

    // --- ArrayBuilder.make (uses linkTimeIf(isWebAssembly)) ---
    val ab = scala.collection.mutable.ArrayBuilder.make[Int]
    ab += 1
    ab += 2
    ab += 3
    val arr = ab.result()
    assert(arr.length == 3, "ArrayBuilder: length")
    assert(arr(0) == 1 && arr(1) == 2 && arr(2) == 3, "ArrayBuilder: elements")

    println("All pure Wasm library path tests passed!")
  }
}


