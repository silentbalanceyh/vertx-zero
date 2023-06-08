package io.vertx.up.fn;

import io.horizon.fn.ErrorSupplier;
import io.vertx.core.Future;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Unique interface to call function in zero framework.
 * 基本格式：
 * --- [] 代表集合，
 * --- () 代表异步
 * 基础标记位处理：
 * 1. 参数类型标记位：
 * --- J: JsonObject
 * --- A: JsonArray
 * --- T: 泛型T
 * --- B: Boolean
 * --- M: Map哈希表
 * --- L: 列表 List
 * --- G: 分组专用
 * 2. 函数标记位（按参数可重载）
 * --- Supplier:    Or
 * --- Function:    Of
 * --- Predicate:   x
 * --- Consumer:    At
 * --- Actuator:    Act
 * 3. 函数异常标记位矩阵：
 * ------------ |  Throwable  |  Exception  |  ZeroException  |  ZeroRunException
 * - Supplier - |
 * - Function - |
 * - Consumer - |
 * - Predicate  |
 */
@SuppressWarnings("all")
public final class Fn extends _Out {
    private Fn() {
    }

    // 安全执行确认：Supplier<T> -> Consumer<T>
    public static <T> void monad(final Supplier<T> supplier, final Consumer<T> consumer) {
        Monad.safeT(supplier, consumer);
    }

    public static <T> T monad(final ErrorSupplier<T> supplier, final T defaultValue) {
        return Monad.monad(supplier, defaultValue);
    }

    public static <T> Future<T> monadAsync(final ErrorSupplier<Future<T>> supplier, final T defaultValue) {
        return Monad.monadAsync(supplier, defaultValue);
    }
}
