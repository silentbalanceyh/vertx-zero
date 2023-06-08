package io.vertx.up.fn;

import io.horizon.fn.HFn;
import io.vertx.core.Future;
import io.vertx.core.Promise;

import java.util.function.Consumer;

/**
 * @author lang : 2023/4/27
 */
class _Atomic extends HFn {
    protected _Atomic() {
    }

    // ---------------------------------------------------- 原子函数 ----------------------------------------------------

    /**
     * Vert.x 从 3.8 开始支持 Promise 类型，且单节点 Monad 模式全部改成了 Promise 模式，
     * 这两个 pacifier 方法的目的是为了协同 Promise 和 Future 在旧模式下的执行，保证可协同
     * 主要应用于 UxPool / UxJob / UxMongo 中
     *
     * @param consumer Consumer<Promise<T>>
     * @param <T>      T
     *
     * @return Future<T>
     */
    public static <T> Future<T> pack(final Consumer<Promise<T>> consumer) {
        return Then.then(consumer);
    }

    /**
     * Vert.x 从 3.8 开始支持 Promise 类型，且单节点 Monad 模式全部改成了 Promise 模式，
     * 该函数为回调类型的处理，用于处理后期响应，异步节点模式
     *
     * @param result 异步返回结果，通常是 AsyncResult<T>
     * @param future Future<T>
     * @param ex     Throwable
     * @param <T>    T
     */
    public static <T> void pack(final Object result, final Promise<T> future, final Throwable ex) {
        Then.then(result, future, ex);
    }
}
