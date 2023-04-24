package io.zero.spec.function;

import io.zero.fn.error.EFunction;

/**
 * @author lang : 2023/4/24
 */
@FunctionalInterface
public interface ExceptionFunction<I, O> extends EFunction<I, O, Exception> {
}
