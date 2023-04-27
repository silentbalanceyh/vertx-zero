package io.horizon.util.fn;

import io.horizon.fn.ErrorActuator;
import io.horizon.fn.ErrorSupplier;
import io.horizon.log.HLogger;

/**
 * @author lang : 2023/4/28
 */
class _Jvm extends _Fail {
    protected _Jvm() {
    }

    /**
     * Supplier函数的JVM版本（支持日志记录器），可抛 Throwable 异常
     *
     * @param defaultValue 默认值
     * @param supplier     Supplier函数
     * @param logger       日志记录器
     * @param <T>          返回值类型
     *
     * @return T
     */
    public static <T> T jvmOr(final T defaultValue, final ErrorSupplier<T> supplier,
                              final HLogger logger) {
        return HSupplier.jvmOr(defaultValue, supplier, logger);
    }

    /**
     * Supplier函数的JVM版本（不支持日志记录器），可抛 Throwable 异常
     *
     * @param defaultValue 默认值
     * @param supplier     Supplier函数
     * @param <T>          返回值类型
     *
     * @return T
     */
    public static <T> T jvmOr(final T defaultValue, final ErrorSupplier<T> supplier) {
        return HSupplier.jvmOr(defaultValue, supplier, null);
    }

    /**
     * Supplier函数的JVM版本（支持日志记录器），可抛 Throwable 异常
     *
     * @param supplier Supplier函数
     * @param logger   日志记录器
     * @param <T>      返回值类型
     *
     * @return T
     */
    public static <T> T jvmOr(final ErrorSupplier<T> supplier, final HLogger logger) {
        return HSupplier.jvmOr(null, supplier, logger);
    }

    /**
     * Supplier函数的JVM版本（不支持日志记录器），可抛 Throwable 异常
     *
     * @param supplier Supplier函数
     * @param <T>      返回值类型
     *
     * @return T
     */
    public static <T> T jvmOr(final ErrorSupplier<T> supplier) {
        return HSupplier.jvmOr(null, supplier, null);
    }

    /**
     * （参数非空检查）Supplier函数的JVM版本（默认值 null），可抛 Throwable 异常
     *
     * @param supplier Supplier函数
     * @param input    待检查的输入信息
     * @param <T>      返回值类型
     *
     * @return T
     */
    public static <T> T jvmNOr(final ErrorSupplier<T> supplier, final Object... input) {
        return HSupplier.jvmNOr(null, supplier, input);
    }

    /**
     * （参数非空检查）Supplier函数的JVM版本（支持默认值），可抛 Throwable 异常
     *
     * @param defaultValue 默认值
     * @param supplier     Supplier函数
     * @param input        待检查的输入信息
     * @param <T>          返回值类型
     *
     * @return T
     */
    public static <T> T jvmNOr(final T defaultValue, final ErrorSupplier<T> supplier,
                               final Object... input) {
        return HSupplier.jvmNOr(defaultValue, supplier, input);
    }

    /**
     * 纯执行器版本（支持日志记录器），可定义抛 Throwable 异常
     *
     * @param actuator 执行器
     * @param logger   日志记录器
     */
    public static void jvmAt(final ErrorActuator actuator, final HLogger logger) {
        HActuator.jvmAt(actuator, logger);
    }

    /**
     * 纯执行器版本（不支持日志记录器），可定义抛 Throwable 异常
     *
     * @param actuator 执行器
     */
    public static void jvmAt(final ErrorActuator actuator) {
        HActuator.jvmAt(actuator, null);
    }
}
