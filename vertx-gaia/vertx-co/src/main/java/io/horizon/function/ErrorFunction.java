package io.horizon.function;

/**
 * @author lang : 2023/4/24
 */
@FunctionalInterface
public interface ErrorFunction<I, O> extends EFunction<I, O, Throwable> {
}
