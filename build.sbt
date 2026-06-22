import org.scalajs.jsenv.wasmtime.WasmtimeEnv
import org.scalajs.linker.interface.ESVersion

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.github.scala-wasm"
ThisBuild / scalaVersion := "2.13.18"

lazy val componentSettings = Seq(
  wasmEnv := new WasmtimeEnv(
    WasmtimeEnv.Config()
      .withArgs(List(
        "run",
        "-W", "gc,function-references,exceptions",
        "-S", "cli",
        "-S", "inherit-env",
        "-S", "inherit-network",
        "-S", "tcp",
        "-S", "http"))
      .withEnv(envVars.value)
  ),
  scalaJSWitDirectory := baseDirectory.value / "wit",
  Compile / scalaJSLinkerConfig := {
    val witDir = scalaJSWitDirectory.value
    val witWorld = scalaJSWitWorld.value
    (Compile / scalaJSLinkerConfig).value
      .withPrettyPrint(true)
      .withESFeatures { features =>
        features
          .withUseWebAssembly(true)
          .withESVersion(ESVersion.ES2022)
      }
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
