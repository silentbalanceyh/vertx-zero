package io.vertx.tp.optic.plugin;

import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.up.commune.exchange.DictFabric;

@SuppressWarnings("unchecked")
public interface DataPlugin<T> {

    default T bind(final DataAtom atom) {
        return (T) this;
    }

    default T bind(final DictFabric fabric) {
        return (T) this;
    }
}
