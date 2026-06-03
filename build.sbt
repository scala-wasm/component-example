import org.scalajs.jsenv.wasmtime.WasmtimeEnv

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.github.scala-wasm"
ThisBuild / scalaVersion := "2.13.18"

lazy val componentSettings = Seq(
  jsEnv := new WasmtimeEnv(
    WasmtimeEnv.Config()
      .withArgs(List(
        "run",
        "-W", "gc,function-references,exceptions",
        "-S", "cli,inherit-env,inherit-network,tcp,http"))
      .withEnv(envVars.value)
  ),
  scalaJSWitDirectory := baseDirectory.value / "wit",
  scalaJSLinkerConfig := {
    scalaJSLinkerConfig.value
      .withPrettyPrint(true)
      .withExperimentalUseWebAssembly(true)
      .withModuleKind(ModuleKind.WasmComponent)
  }
)

lazy val helloworld = project
  .in(file("helloworld"))
  .enablePlugins(ScalaJSPlugin, ScalaJSJUnitPlugin)
  .settings(componentSettings)
  .settings(
    name := "helloworld",
    scalaJSUseMainModuleInitializer := true,
    scalaJSWitWorld := Some("command")
  )

lazy val spinTodo = project
  .in(file("spin-todo"))
  .enablePlugins(ScalaJSPlugin)
  .settings(componentSettings)
  .settings(
    name := "spin-todo",
    moduleName := "spin-todo",
    resolvers += "Sonatype Central Snapshots" at "https://central.sonatype.com/repository/maven-snapshots/",
    libraryDependencies += "org.typelevel" %%% "jawn-ast" % "1.6.0-240-05f7211-SNAPSHOT",
    scalaJSWitWorld := Some("todo"),
    scalaJSWitPackage := Some("spintodo")
  )

lazy val wasiHttpClient = project
  .in(file("wasi-http-client"))
  .enablePlugins(ScalaJSPlugin)
  .settings(componentSettings)
  .settings(
    name := "wasi-http-client",
    scalaJSUseMainModuleInitializer := true,
    scalaJSWitWorld := Some("client")
  )

lazy val rustComposeScala = project
  .in(file("rust-compose/scala"))
  .enablePlugins(ScalaJSPlugin)
  .settings(componentSettings)
  .settings(
    name := "rust-compose-scala",
    moduleName := "rust-compose-scala",
    scalaJSWitDirectory := baseDirectory.value / "../wit",
    scalaJSWitWorld := Some("scala"),
    scalaJSWitPackage := Some("rustcompose")
  )
