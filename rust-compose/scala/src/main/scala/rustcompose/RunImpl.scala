package rustcompose

import scala.scalajs.wit
import scala.scalajs.wit.annotation._

import rustcompose.exports.wasi.cli.Run
import rustcompose.scala_wasm.rust_compose.greeter.greet

@WitImplementation
object RunImpl extends Run {
  override def run(): wit.Result[Unit, Unit] = {
    println(greet("Scala"))
    new wit.Ok(())
  }
}
