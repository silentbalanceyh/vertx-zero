package io.horizon.spi.plugin;

import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.up.atom.exchange.DFabric;

@SuppressWarnings("unchecked")
public interface DataPlugin<T> {

    default T bind(final DataAtom atom) {
        return (T) this;
    }

    default T bind(final DFabric fabric) {
        return (T) this;
    }
}
