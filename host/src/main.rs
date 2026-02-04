use wasmtime::component::{Component, HasSelf, Linker, ResourceTable};
use wasmtime::{Config, Engine, Store};
use wasmtime_wasi::{WasiCtx, WasiCtxBuilder, WasiCtxView, WasiView};

wasmtime::component::bindgen!({
    path: "../wit",
    world: "hello-world",
});

struct MyState {
    ctx: WasiCtx,
    table: ResourceTable,
}

impl WasiView for MyState {
    fn ctx(&mut self) -> WasiCtxView<'_> {
        WasiCtxView {
            ctx: &mut self.ctx,
            table: &mut self.table,
        }
    }
}

impl scala_wasm::component_example::name::Host for MyState {
    fn name(&mut self) -> String {
        "Scala".to_string()
    }
}

fn main() -> wasmtime::Result<()> {
    let args: Vec<String> = std::env::args().collect();
    let wasm_path = args.get(1).expect("Usage: host <path-to-wasm-component>");

    let mut config = Config::default();
    config.wasm_function_references(true);
    config.wasm_gc(true);
    config.wasm_exceptions(true);

    let engine = Engine::new(&config)?;
    let mut linker: Linker<MyState> = Linker::new(&engine);
    wasmtime_wasi::p2::add_to_linker_sync(&mut linker)?;
    scala_wasm::component_example::name::add_to_linker::<_, HasSelf<MyState>>(&mut linker, |state| state)?;

    let wasi = WasiCtxBuilder::new().inherit_stdio().inherit_args().build();
    let state = MyState {
        ctx: wasi,
        table: ResourceTable::new(),
    };
    let mut store = Store::new(&engine, state);

    let component = Component::from_file(&engine, wasm_path)?;

    let bindings = HelloWorld::instantiate(&mut store, &component, &linker)?;

    let _ = bindings
        .scala_wasm_component_example_greet()
        .call_greet(&mut store);

    Ok(())
}
