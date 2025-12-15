name := "scala-wasm-component-example"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.12.20"

enablePlugins(ScalaJSPlugin)

scalaJSLinkerConfig ~= {
  _.withPrettyPrint(true) // for debugging, it's okay with false
  // For generating WebAssembly
   .withExperimentalUseWebAssembly(true)
   .withModuleKind(ModuleKind.ESModule)
   // For generating Component Model compatible Wasm binary
   .withWasmFeatures(
     _.withTargetPureWasm(true) // component model requires Wasm module not to import any JS stuffs.
      .withComponentModel(true)
      .withExceptionHandling(true)) // wasmtime 37 supports EH
}
