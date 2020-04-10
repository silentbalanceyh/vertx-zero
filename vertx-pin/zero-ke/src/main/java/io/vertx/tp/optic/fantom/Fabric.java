package io.vertx.tp.optic.fantom;

import io.vertx.core.Future;

public interface Fabric<T> {

    Future<T> combine(T input);
}
