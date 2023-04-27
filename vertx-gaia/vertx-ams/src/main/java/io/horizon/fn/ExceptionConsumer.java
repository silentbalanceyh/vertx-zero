package io.horizon.fn;

/**
 * @author lang : 2023/4/24
 */
@FunctionalInterface
public interface ExceptionConsumer<T> extends EConsumer<T, Exception> {
}
