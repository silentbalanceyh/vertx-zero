package io.zero.spec.function;

import io.zero.fn.error.ESupplier;

/**
 * Supplier function interface, this interface could throw out
 * java.lang.Exception for specific use.
 *
 * @param <T>
 */
@FunctionalInterface
public interface ExceptionSupplier<T> extends ESupplier<T, Exception> {

}
