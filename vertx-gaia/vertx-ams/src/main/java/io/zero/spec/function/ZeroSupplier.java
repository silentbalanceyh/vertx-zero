package io.zero.spec.function;


import io.zero.exception.ZeroException;
import io.zero.fn.error.ESupplier;

@FunctionalInterface
public interface ZeroSupplier<T> extends ESupplier<T, ZeroException> {
}
