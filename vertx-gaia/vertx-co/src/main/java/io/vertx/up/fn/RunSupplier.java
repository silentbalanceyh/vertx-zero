package io.vertx.up.fn;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@FunctionalInterface
public interface RunSupplier<T> {
    T get() throws Throwable;
}
