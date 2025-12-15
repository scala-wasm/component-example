SCALA_MODULE := target/scala-2.12/scala-wasm-component-example-fastopt/main.wasm
WORLD := hello-world
MAIN_MODULE := main.wasm
WASM_TOOLS := wasm-tools
WKG := wkg

fetch:
	$(WKG) wit fetch

embed:
	$(WASM_TOOLS) component embed wit $(SCALA_MODULE) -o $(MAIN_MODULE) -w $(WORLD) --encoding utf16

component: embed
	$(WASM_TOOLS) component new $(MAIN_MODULE) -o $(MAIN_MODULE)

clean:
	rm -f $(MAIN_MODULE)
