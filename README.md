# Scala Wasm example

An example of compiling Scala to WebAssembly using the [scala-wasm](https://github.com/scala-wasm/scala-wasm) compiler.

## Prerequisites

- wasmtime
- wasm-tools
- wit-bindgen: `cargo install --git https://github.com/scala-wasm/wit-bindgen --tag scala-wasm-wasm.4`
- sbt

## Build and Run

```bash
sbt run
```

This runs the Scala Wasm program with Wasmtime.

## Test

```bash
sbt test
```

This runs the app project's test target with Wasmtime.
