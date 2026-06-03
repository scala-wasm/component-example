#[allow(warnings)]
mod bindings;

use crate::bindings::exports::scala_wasm::rust_compose::greeter::Guest;

struct Component;

impl Guest for Component {
    fn greet(name: String) -> String {
        let mut buffer = Vec::new();
        let message = format!("Hello from Rust, {name}!");
        ferris_says::say(&message, 80, &mut buffer).unwrap();
        String::from_utf8(buffer).unwrap()
    }
}

bindings::export!(Component with_types_in bindings);
