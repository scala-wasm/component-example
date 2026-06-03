# Scala Wasm Component Examples

Small examples for compiling Scala to WebAssembly components with [scala-wasm](https://github.com/scala-wasm/scala-wasm).

## Prerequisites

- wasmtime
- wasm-tools
- wit-bindgen: `cargo install --git https://github.com/scala-wasm/wit-bindgen --tag scala-wasm-wasm.4`
- sbt

For the Rust composition example:

- cargo-component
- wac

For the Spin TODO example:

- wkg
- spin canary

```sh
curl -fsSL https://spinframework.dev/downloads/install.sh | bash -s -- -v canary
```

Configure `wkg` to use `wa.dev`:

```toml
default_registry = "wa.dev"

[namespace_registries]
fermyon = "wa.dev"
wasi = "wa.dev"
```

## Examples

### helloworld

Minimal command component with a JUnit smoke test.

```sh
sbt helloworld/run helloworld/test
```

### spin-todo

Spin HTTP + SQLite TODO API. Its WIT imports `fermyon:spin/sqlite@2.0.0` and exports `wasi:http/incoming-handler@0.2.0`; dependencies are fetched from `wa.dev`.

```sh
sbt spinTodo/fastLinkJS
```

To refresh WIT dependencies:

```sh
cd spin-todo
wkg wit fetch
```

To run with Spin:

```sh
cd spin-todo
spin up --build \
  --experimental-wasm-feature gc \
  --experimental-wasm-feature exceptions \
  --experimental-wasm-feature function-references \
  --experimental-wasm-feature reference-types \
  --sqlite @migration.sql
```

Example requests:

```sh
curl -s http://127.0.0.1:3000/todos
curl -s http://127.0.0.1:3000/todos -d '{"title":"Try Scala-wasm on Spin"}'
curl -s -X DELETE http://127.0.0.1:3000/todos/1
```

### wasi-http-client

Command component that imports `wasi:http/outgoing-handler@0.2.0` and sends an HTTPS GET request with Wasmtime.

```sh
sbt wasiHttpClient/run
```

To refresh WIT dependencies:

```sh
cd wasi-http-client
wkg wit fetch
```

### rust-compose

Cross-language composition example. Scala imports a custom `greeter` interface, Rust exports it with `cargo component`, and `wac plug` links them into one runnable component.

```sh
cd rust-compose
make all
```
