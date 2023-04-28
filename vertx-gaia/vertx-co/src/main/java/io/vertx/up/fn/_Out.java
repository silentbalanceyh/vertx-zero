package io.vertx.up.fn;

import io.vertx.core.Future;
import io.vertx.up.exception.WebException;

/**
 * @author lang : 2023/4/28
 */
class _Out extends _Combine {
    protected _Out() {
    }

    /**
     * （函数模式专用）针对异步响应 Future.failedFuture 的专用单行处理方法，直接返回 Future（失败的）
     * 的异步处理结果，主要是实现如下代码：
     *
     * <pre><code>
     *     final WebException error = new XXX(...args);
     *     return Future.failedFuture(error);
     * </code></pre>
     *
     * args 的第一个参数通常是 target，后续参数是 message 的占位符参数
     *
     * @param clazz 异常类，必须是 WebException 的子类
     * @param args  异常参数
     * @param <T>   泛型类型
     *
     * @return Future<T>
     */
    public static <T> Future<T> failure(final Class<? extends WebException> clazz, final Object... args) {
        return Other.otherwise(clazz, args);
    }
}
