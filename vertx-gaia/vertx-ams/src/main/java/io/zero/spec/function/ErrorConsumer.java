package io.zero.spec.function;

import io.zero.fn.error.EConsumer;

/**
 * @author lang : 2023/4/24
 */
@FunctionalInterface
public interface ErrorConsumer<T> extends EConsumer<T, Throwable> {
}
