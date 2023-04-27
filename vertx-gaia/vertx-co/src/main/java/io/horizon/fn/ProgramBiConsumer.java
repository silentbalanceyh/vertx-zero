package io.horizon.fn;

import io.horizon.exception.ProgramException;

@FunctionalInterface
public interface ProgramBiConsumer<T, R> extends EBiConsumer<T, R, ProgramException> {
}
