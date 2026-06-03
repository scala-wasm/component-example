# Scala Wasm Component Examples

Examples for compiling Scala to WebAssembly components with [scala-wasm](https://github.com/scala-wasm/scala-wasm).

## Prerequisites

- [Wasmtime](https://wasmtime.dev/)
- [wasm-tools](https://github.com/bytecodealliance/wasm-tools)
- (scala-wasm version of) [wit-bindgen](https://github.com/scala-wasm/wit-bindgen)
  - `cargo install --git https://github.com/scala-wasm/wit-bindgen --tag scala-wasm-wasm.4`
- [cargo-component](https://github.com/bytecodealliance/cargo-component)
- [wac](https://github.com/bytecodealliance/wac)
- [wkg](https://github.com/bytecodealliance/wasm-pkg-tools)
- [Spin canary](https://developer.fermyon.com/spin/v4/install)

## Examples

- [helloworld](helloworld): minimal command component with a JUnit smoke test.
- [spin-todo](spin-todo): Spin HTTP + SQLite TODO API.
- [wasi-http-client](wasi-http-client): command component that sends an HTTP GET request.
- [rust-compose](rust-compose): cross-language component composition with Scala and Rust.

See each directory README for build and run steps.
