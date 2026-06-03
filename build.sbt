import org.scalajs.jsenv.wasmtime.WasmtimeEnv

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.github.scala-wasm"
ThisBuild / scalaVersion := "3.8.4"

lazy val componentSettings = Seq(
  jsEnv := new WasmtimeEnv(
    WasmtimeEnv.Config()
      .withArgs(List(
        "run",
        "-W", "gc,function-references,exceptions",
        "-S", "cli=y",
        "-S", "inherit-env=y",
        "-S", "inherit-network=y",
        "-S", "tcp=y",
        "-S", "http=y"))
      .withEnv(envVars.value)
  ),
  scalaJSWitDirectory := baseDirectory.value / "wit",
  Compile / scalaJSLinkerConfig := {
    val witDir = scalaJSWitDirectory.value
    val witWorld = scalaJSWitWorld.value
    (Compile / scalaJSLinkerConfig).value
      .withPrettyPrint(true)
      .withExperimentalUseWebAssembly(true)
      .withModuleKind(ModuleKind.WasmComponent)
      .withWasmFeatures { features =>
        features // in future, plugin should automatically set these settings
          .withWitDirectory(Some(witDir.getAbsolutePath))
          .withWitWorld(witWorld)
      }
  },
  // Currently, we need to set these to None for tests, as they are not needed for component linking
  // in future, plugin should automatically handle these settings.
  Test / scalaJSLinkerConfig := {
    (Compile / scalaJSLinkerConfig).value
      .withWasmFeatures { features =>
        features
          .withWitDirectory(None)
          .withWitWorld(None)
      }
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
    Compile / wasmEnv := new WasmtimeEnv(
      WasmtimeEnv.Config()
        .withArgs(List(
          "run",
          "-W", "gc,function-references,exceptions",
          "-S", "cli=y",
          "-S", "inherit-env=y",
          "-S", "inherit-network=y",
          "-S", "tcp=y",
          "-S", "http"))
        .withEnv(envVars.value)
    ),
    scalaJSWitBindgenWith := (Global / scalaJSWitBindgenWith).value - "wasi:http",
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
