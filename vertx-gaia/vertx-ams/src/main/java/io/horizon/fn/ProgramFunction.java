package io.horizon.fn;

import io.horizon.exception.ProgramException;

/**
 * @author lang : 2023/4/28
 */
@FunctionalInterface
public interface ProgramFunction<I, O> extends EFunction<I, O, ProgramException> {
}
