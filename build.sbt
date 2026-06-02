import org.scalajs.jsenv.wasmtime.WasmtimeEnv

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.github.scala-wasm"
ThisBuild / scalaVersion := "2.13.18"

enablePlugins(ScalaJSPlugin, ScalaJSJUnitPlugin)

name := "scala-wasm-component-example"

scalaJSUseMainModuleInitializer := true

jsEnv := new WasmtimeEnv(
  WasmtimeEnv.Config()
    .withArgs(List(
      "run",
      "-W", "gc,function-references,exceptions",
      "-S", "cli,inherit-env,inherit-network,tcp"))
    .withEnv(envVars.value)
)

scalaJSWitDirectory := baseDirectory.value / "wit"
scalaJSWitWorld := Some("command")

scalaJSLinkerConfig := {
  scalaJSLinkerConfig.value
    .withPrettyPrint(true)
    .withExperimentalUseWebAssembly(true)
    .withModuleKind(ModuleKind.WasmComponent)
}
