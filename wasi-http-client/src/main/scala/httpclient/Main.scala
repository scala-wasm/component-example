package httpclient

object Main {
  def main(args: Array[String]): Unit = {
    val response = HttpClient.get("httpbin.org", "/get")
    val body = HttpClient.readBody(response)

    println(s"Status: ${response.status()}")
    println(body.take(200))
  }
}
