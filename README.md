# Component Model example

An example of compiling Scala to WebAssembly using the [scala-wasm](https://github.com/scala-wasm/scala-wasm) compiler with Component Model support.

## Prerequisites

- wasm-tools: `cargo install wasm-tools`
- wit-bindgen: `cargo install --git https://github.com/scala-wasm/wit-bindgen --branch scala`

## Build

Build the Scala project to generate the WASM component:

```bash
sbt fastLinkJS
```

This generates a component at `target/scala-2.12/scala-wasm-component-example-fastopt/main.wasm`.

## Run with the Rust host

Build and run the Rust host:

```bash
cd host
cargo build --release
./target/release/host ../target/scala-2.12/scala-wasm-component-example-fastopt/main.wasm
```

or you can just `make run`
