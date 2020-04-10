package io.vertx.up.fn;

import io.vertx.up.exception.ZeroException;

@FunctionalInterface
public interface ZeroBiConsumer<T, R> {

    void accept(T input, R second) throws ZeroException;
}
