package io.zero.fn.error;

/**
 * @author lang : 2023/4/24
 */
public interface ESupplier<T, E extends Throwable> {
    T get() throws E;
}
