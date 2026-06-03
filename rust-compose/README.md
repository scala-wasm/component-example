# rust-compose

Cross-language composition example.

Scala imports a custom `greeter` interface, Rust exports it with `cargo component`, and `wac plug` links them into one runnable component.

## Run

From this directory:

```sh
make all
```

`make all` fetches WIT dependencies, builds the Scala component, builds the Rust component, composes them, and runs the result with Wasmtime.
