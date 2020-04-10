package io.vertx.up.fn;

import io.vertx.up.exception.ZeroException;

@FunctionalInterface
public interface ZeroSupplier<T> {

    T get() throws ZeroException;
}
