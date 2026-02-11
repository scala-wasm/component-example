resolvers += "Sonatype Central Snapshots" at "https://central.sonatype.com/repository/maven-snapshots/"

name := "scala-wasm-component-example"
version := "0.1.0-SNAPSHOT"
scalaOrganization := "io.github.scala-wasm"
scalaVersion := "3.8.3-RC1-bin-SNAPSHOT"

// Workaround scalaOrganization should work out of the box
// Copied from zinc-lm-integration/src/main/scala/sbt/internal/inc/ZincLmUtil.scala
// fetchDefaultBridgeModule
scalaCompilerBridgeBinaryJar := {
  val sv = scalaVersion.value
  val bridgeModule = "io.github.scala-wasm" % "scala3-sbt-bridge" % sv
  val descriptor = dependencyResolution.value.wrapDependencyInModule(bridgeModule)
  val jar = dependencyResolution.value
    .update(
      descriptor,
      updateConfiguration.value,
      (update / unresolvedWarningConfiguration).value,
      streams.value.log
    )
    .toOption
    .flatMap { report =>
      report.select(
        configurationFilter(Compile.name),
        moduleFilter(bridgeModule.organization, bridgeModule.name, bridgeModule.revision),
        artifactFilter(extension = "jar", classifier = "")
      ).headOption
    }
  Some(jar.getOrElse(sys.error(s"Could not resolve $bridgeModule")))
}

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
