package io.horizon.fn;

import io.horizon.exception.ProgramException;

@FunctionalInterface
public interface ProgramSupplier<T> extends ESupplier<T, ProgramException> {
}
