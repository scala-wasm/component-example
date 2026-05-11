name := "scala-wasm-component-example"
version := "0.1.0-SNAPSHOT"
organization := "io.github.scala-wasm"
scalaVersion := "2.13.17"

enablePlugins(ScalaJSPlugin)

scalaJSLinkerConfig ~= {
  _.withPrettyPrint(true) // for debugging, it's okay with false
  // For generating WebAssembly
   .withExperimentalUseWebAssembly(true)
   .withModuleKind(ModuleKind.WasmComponent)
   // For generating Component Model compatible Wasm binary
   .withWasmFeatures(
      _.withWitDirectory(Some("wit"))) // for wasm-tools to tell which directory to embed
}
