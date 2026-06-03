# spin-todo

Spin HTTP + SQLite TODO API.

The component imports `fermyon:spin/sqlite@2.0.0` and exports `wasi:http/incoming-handler@0.2.0`. WIT dependencies are fetched from `wa.dev`.

## Setup

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

## Build

From the repository root:

```sh
sbt spinTodo/fastLinkJS
```

## Refresh WIT Dependencies

From this directory:

```sh
wkg wit fetch
```

## Run

From this directory:

```sh
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
