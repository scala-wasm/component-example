SCALA_MODULE := scala/target/scala-2.13/rust-compose-scala-fastopt/main.wasm
RUST_MODULE := rust-greeter/target/wasm32-wasip1/release/rust_greeter.wasm
OUT := out.wasm

fetch:
	wkg wit fetch

build-scala:
	cd .. && sbt rustComposeScala/fastLinkJS

build-rust:
	cd rust-greeter && cargo component build --target wasm32-wasip2 -r

compose:
	wac plug --plug $(RUST_MODULE) $(SCALA_MODULE) -o $(OUT)

run: compose
	wasmtime -W gc,function-references,exceptions $(OUT)

all: fetch build-scala build-rust run

clean:
	rm -f $(OUT)

.PHONY: fetch build-scala build-rust compose run all clean
