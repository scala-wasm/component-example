# Component Model example


## Publish local scala-wasm/scala-wasm

```scala
sbt:Scala.js> ir2_12/publishLocal;linkerInterface2_12/publishLocal;linker2_12/publishLocal;testAdapter2_12/publishLocal;sbtPlugin/publishLocal;javalib/publishLocal;javalibintf/publishLocal

sbt:Scala.js> library2_12/publishLocal;testInterface2_12/publishLocal;testBridge2_12/publishLocal;jUnitRuntime2_12/publishLocal;jUnitPlugin2_12/publishLocal

sbt:Scala.js> ++2.12.20 compiler2_12/publishLocal
```

