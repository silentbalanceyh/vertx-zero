package io.horizon.util.fn;

import io.horizon.fn.ExceptionSupplier;

/**
 * @author lang : 2023/4/28
 */
class _Fail extends _Bug {
    protected _Fail() {
    }

    /**
     * （参数非空检查）Supplier函数的JVM版本（默认值 null），可抛 Exception 异常
     *
     * @param supplier Supplier函数
     * @param input    待检查的输入信息
     * @param <T>      返回值类型
     *
     * @return T
     */
    public static <T> T failOr(final ExceptionSupplier<T> supplier, final Object... input) {
        return HSupplier.failOr(null, supplier, input);
    }

    /**
     * （参数非空检查）Supplier函数的JVM版本（支持默认值），可抛 Exception 异常
     *
     * @param defaultValue 默认值
     * @param supplier     Supplier函数
     * @param input        待检查的输入信息
     * @param <T>          返回值类型
     *
     * @return T
     */
    public static <T> T failOr(final T defaultValue, final ExceptionSupplier<T> supplier,
                               final Object... input) {
        return HSupplier.failOr(defaultValue, supplier, input);
    }
}
