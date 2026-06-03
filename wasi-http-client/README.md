# wasi-http-client

Command component that imports `wasi:http/outgoing-handler@0.2.0` and sends an HTTPS GET request with Wasmtime.

## Run

From the repository root:

```sh
sbt wasiHttpClient/run
```

## Refresh WIT Dependencies

From this directory:

```sh
wkg wit fetch
```
