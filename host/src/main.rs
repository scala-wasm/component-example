use wasmtime::component::{Component, HasSelf, Linker, ResourceTable};
use wasmtime::{Config, Engine, Store};
use wasmtime_wasi::{WasiCtx, WasiCtxView, WasiView};

// See: https://docs.rs/wasmtime-wasi/latest/wasmtime_wasi/p2/bindings/sync/index.html
wasmtime::component::bindgen!({
    path: "../wit",
    world: "hello-world",
    with: {
        "wasi": wasmtime_wasi::p2::bindings::sync,
    },
    require_store_data_send: true,
});

struct MyState {
    table: ResourceTable,
    ctx: WasiCtx,
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
    let mut config = Config::default();
    config.wasm_function_references(true);
    config.wasm_gc(true);

    let engine = Engine::new(&config)?;
    let mut linker: Linker<MyState> = Linker::new(&engine);
    wasmtime_wasi::p2::add_to_linker_sync(&mut linker)?;
    scala_wasm::component_example::name::add_to_linker::<_, HasSelf<_>>(&mut linker, |state| {
        state
    })?;

    let wasi = WasiCtx::builder().inherit_stdio().inherit_args().build();
    let state = MyState {
        table: ResourceTable::new(),
        ctx: wasi,
    };
    let mut store = Store::new(&engine, state);

    let component = Component::from_file(&engine, "../main.wasm")?;

    let bindings = HelloWorld::instantiate(&mut store, &component, &linker)?;

    let _ = bindings
        .scala_wasm_component_example_greet()
        .call_greet(&mut store);

    Ok(())
}
