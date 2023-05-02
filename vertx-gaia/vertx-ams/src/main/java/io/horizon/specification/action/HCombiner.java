package io.horizon.specification.action;

import io.vertx.core.Future;

public interface HCombiner<T> {

    Future<T> combine(T input);
}
