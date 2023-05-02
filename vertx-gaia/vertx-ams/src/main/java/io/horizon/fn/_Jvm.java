package io.horizon.fn;

import io.horizon.specification.uca.HLogger;
import io.horizon.util.HUt;

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
        return HSupplier.jvmOr(defaultValue, supplier, (HLogger) null);
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
        return HSupplier.jvmOr(null, supplier, (HLogger) null);
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
    public static <T> T jvmOr(final ErrorSupplier<T> supplier, final Object... input) {
        return jvmOr(null, supplier, input);
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
    public static <T> T jvmOr(final T defaultValue, final ErrorSupplier<T> supplier,
                              final Object... input) {
        if (HUt.isNotNull(input)) {
            return HSupplier.jvmOr(defaultValue, supplier, (HLogger) null);
        } else {
            return defaultValue;
        }
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

    /**
     * 纯执行器版本（支持日志记录器），可定义抛 Throwable 异常
     * 针对参数非空检查的执行器版本
     *
     * @param actuator 执行器
     * @param logger   日志记录器
     * @param input    待检查的输入信息
     */
    public static void jvmAt(final ErrorActuator actuator, final HLogger logger,
                             final Object... input) {
        if (HUt.isNotNull(input)) {
            HActuator.jvmAt(actuator, logger);
        }
    }

    /**
     * 纯执行器版本（不支持日志记录器），可定义抛 Throwable 异常
     * 针对参数非空检查的执行器版本
     *
     * @param actuator 执行器
     * @param input    待检查的输入信息
     */
    public static void jvmAt(final ErrorActuator actuator, final Object... input) {
        if (HUt.isNotNull(input)) {
            HActuator.jvmAt(actuator, null);
        }
    }
}
