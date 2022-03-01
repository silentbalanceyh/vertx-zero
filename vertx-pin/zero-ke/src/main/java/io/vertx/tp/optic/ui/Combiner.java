package io.vertx.tp.optic.ui;

import io.vertx.core.Future;

public interface Combiner<T> {

    Future<T> combine(T input);
}
