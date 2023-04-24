package io.horizon.function;

/**
 * @author lang : 2023/4/24
 */
@FunctionalInterface
public interface ErrorConsumer<T> extends EConsumer<T, Throwable> {
}
