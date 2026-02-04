package example

import scalajs.wit.annotation._
import scalajs.wit._

import componentmodel.exports.scala_wasm.component_example

/*op
t Endpoints of the Component Model interface corresponds to
 *
 *  package scala-wasm:component-example;
 *  world hello-world { ... }
 */
@WitImplementation
object GreetImpl extends component_example.Greet {
  def greet(): Unit = println("hello world")
}