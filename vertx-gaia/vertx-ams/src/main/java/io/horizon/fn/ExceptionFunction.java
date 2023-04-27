package io.horizon.fn;

/**
 * @author lang : 2023/4/24
 */
@FunctionalInterface
public interface ExceptionFunction<I, O> extends EFunction<I, O, Exception> {
}
