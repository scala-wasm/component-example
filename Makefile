build-scala:
	sbt fastLinkJS

build-rust:
	cd host && cargo build -r

run: build-scala build-rust
	cd host && cargo run -r -- ../target/scala-*/scala-wasm-component-example-fastopt/main.wasm

clean:
	rm -rf target host/target
