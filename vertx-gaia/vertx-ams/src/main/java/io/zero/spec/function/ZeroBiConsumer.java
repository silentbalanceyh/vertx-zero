package io.zero.spec.function;

import io.zero.exception.ZeroException;
import io.zero.fn.error.EBiConsumer;

@FunctionalInterface
public interface ZeroBiConsumer<T, R> extends EBiConsumer<T, R, ZeroException> {
}
