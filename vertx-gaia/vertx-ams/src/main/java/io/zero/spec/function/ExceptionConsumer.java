package io.zero.spec.function;

import io.zero.fn.error.EConsumer;

/**
 * @author lang : 2023/4/24
 */
@FunctionalInterface
public interface ExceptionConsumer<T> extends EConsumer<T, Exception> {
}
