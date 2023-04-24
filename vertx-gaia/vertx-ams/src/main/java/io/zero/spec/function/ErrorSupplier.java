package io.zero.spec.function;

import io.zero.fn.error.ESupplier;

/**
 * 带异常抛出的函数接口版本
 * - Error: Throwable
 * - Exception: Exception
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@FunctionalInterface
public interface ErrorSupplier<T> extends ESupplier<T, Throwable> {
}
