package io.horizon.fn;

import io.horizon.exception.ZeroException;

@FunctionalInterface
public interface ZeroBiConsumer<T, R> extends EBiConsumer<T, R, ZeroException> {
}
