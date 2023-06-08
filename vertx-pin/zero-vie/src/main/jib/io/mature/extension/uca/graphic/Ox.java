package io.mature.extension.uca.graphic;

import io.horizon.eon.em.typed.ChangeFlag;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

interface Pool {

    ConcurrentMap<ChangeFlag, ConcurrentHashMap<String, Pixel>> POOL_NODE = new ConcurrentHashMap<ChangeFlag, ConcurrentHashMap<String, Pixel>>() {
        {
            this.put(ChangeFlag.ADD, new ConcurrentHashMap<>());
            this.put(ChangeFlag.UPDATE, new ConcurrentHashMap<>());
            this.put(ChangeFlag.DELETE, new ConcurrentHashMap<>());
        }
    };

    ConcurrentMap<ChangeFlag, Function<String, Pixel>> POOL_NODE_SUPPLIER = new ConcurrentHashMap<ChangeFlag, Function<String, Pixel>>() {
        {
            this.put(ChangeFlag.ADD, NodeAddPixel::new);
            this.put(ChangeFlag.UPDATE, NodeUpdatePixel::new);
            this.put(ChangeFlag.DELETE, NodeDeletePixel::new);
        }
    };
    ConcurrentMap<ChangeFlag, ConcurrentHashMap<String, Pixel>> POOL_EDGE = new ConcurrentHashMap<ChangeFlag, ConcurrentHashMap<String, Pixel>>() {
        {
            this.put(ChangeFlag.ADD, new ConcurrentHashMap<>());
            this.put(ChangeFlag.UPDATE, new ConcurrentHashMap<>());
            this.put(ChangeFlag.DELETE, new ConcurrentHashMap<>());
        }
    };
    ConcurrentMap<ChangeFlag, Function<String, Pixel>> POOL_EDGE_SUPPLIER = new ConcurrentHashMap<ChangeFlag, Function<String, Pixel>>() {
        {
            this.put(ChangeFlag.ADD, EdgeAddPixel::new);
            this.put(ChangeFlag.UPDATE, EdgeUpdatePixel::new);
            this.put(ChangeFlag.DELETE, EdgeDeletePixel::new);
        }
    };
}
