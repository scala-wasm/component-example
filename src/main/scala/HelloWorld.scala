package example

import scalajs.component.annotation._
import scalajs.{component => cm}

/** Endpoints of the Component Model interface corresponds to
 *
 *  package scala-wasm:component-example;
 *  world hello-world { ... }
 */
object HelloWorld {
  // greet: func();
  @ComponentExport("scala-wasm:component-example/greet", "greet")
  def greet(): Unit = {
    val msg = s"Hello ${name()}"
    println(msg)
  }

  // name: func() -> string;
  @ComponentImport("scala-wasm:component-example/name", "name")
  def name(): String = cm.native
}

