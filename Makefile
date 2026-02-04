SCALA_OUT := target/scala-2.12/scala-wasm-component-example-fastopt/main.wasm

build-rust:
	cd host && cargo build -r

run: build-rust
	cd host && cargo run -r -- ../$(SCALA_OUT)

clean:
	rm -f $(SCALA_OUT)
