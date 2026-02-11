SCALA_OUT := target/scala-3.8.3-RC1-bin-SNAPSHOT/scala-wasm-component-example-fastopt/main.wasm

build-rust:
	cd host && cargo build -r

run: build-rust
	cd host && cargo run -r -- ../$(SCALA_OUT)

clean:
	rm -f $(SCALA_OUT)
