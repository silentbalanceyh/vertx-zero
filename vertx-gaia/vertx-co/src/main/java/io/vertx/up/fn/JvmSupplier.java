package io.vertx.up.fn;

/**
 * Supplier function interface, this interface could throw out
 * java.lang.Exception for specific use.
 *
 * @param <T>
 */
@FunctionalInterface
public interface JvmSupplier<T> {

    T get() throws Exception;
}
