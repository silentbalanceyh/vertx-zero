package io.horizon.function;

import io.horizon.exception.ZeroException;

@FunctionalInterface
public interface ZeroSupplier<T> extends ESupplier<T, ZeroException> {
}
