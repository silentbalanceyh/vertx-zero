package io.horizon.fn;

import io.horizon.exception.ZeroException;

@FunctionalInterface
public interface ZeroSupplier<T> extends ESupplier<T, ZeroException> {
}
