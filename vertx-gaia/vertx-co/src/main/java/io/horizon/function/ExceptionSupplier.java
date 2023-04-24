package io.horizon.function;

/**
 * Supplier function interface, this interface could throw out
 * java.lang.Exception for specific use.
 *
 * @param <T>
 */
@FunctionalInterface
public interface ExceptionSupplier<T> extends ESupplier<T, Exception> {

}
