resolvers += "Sonatype Central Snapshots" at "https://central.sonatype.com/repository/maven-snapshots/"

name := "scala-wasm-component-example"
version := "0.1.0-SNAPSHOT"
scalaOrganization := "io.github.scala-wasm"
scalaVersion := "3.8.3-RC1-wasm-bin-SNAPSHOT"

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
      .withWitDirectory(Some("wit"))) // for wasm-tools to tell which directory to embed
}
