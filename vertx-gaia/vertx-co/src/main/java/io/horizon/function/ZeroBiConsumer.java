package io.horizon.function;

import io.horizon.exception.ZeroException;

@FunctionalInterface
public interface ZeroBiConsumer<T, R> extends EBiConsumer<T, R, ZeroException> {
}
