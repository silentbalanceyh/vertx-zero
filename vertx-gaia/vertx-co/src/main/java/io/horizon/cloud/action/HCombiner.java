package io.horizon.cloud.action;

import io.vertx.core.Future;

public interface HCombiner<T> {

    Future<T> combine(T input);
}
