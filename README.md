# Component Model example

An example of compiling Scala to WebAssembly using the [scala-wasm](https://github.com/scala-wasm/scala-wasm) compiler with Component Model support.

## Prerequisites

- wasm-tools: `cargo install wasm-tools`
- wit-bindgen: `cargo install --git https://github.com/scala-wasm/wit-bindgen --branch scala`

## Build and Run

```bash
make run
```

This builds the Scala WASM component (`sbt fastLinkJS`), builds the Rust host, and runs the component.
