package io.horizon.fn;

import io.horizon.specification.uca.HLogger;
import io.horizon.util.HUt;

/**
 * @author lang : 2023/4/28
 */
class _Fail extends _Bug {
    protected _Fail() {
    }

    /**
     * 纯执行器版本（不支持日志记录），可抛 Exception 异常
     *
     * @param actuator 执行器
     */
    public static void failAt(final ExceptionActuator actuator) {
        HActuator.failAt(actuator, null);
    }

    /**
     * 纯执行器版本（支持日志记录），可抛 Exception 异常
     *
     * @param actuator 执行器
     * @param logger   日志记录器
     */
    public static void failAt(final ExceptionActuator actuator, final HLogger logger) {
        HActuator.failAt(actuator, logger);
    }

    /**
     * （带输入检查）纯执行器版本（支持日志记录），可抛 Exception 异常
     *
     * @param actuator 执行器
     * @param logger   日志记录器
     * @param input    输入
     */
    public static void failAt(final ExceptionActuator actuator, final HLogger logger,
                              final Object... input) {
        if (HUt.isNotNull(input)) {
            HActuator.failAt(actuator, logger);
        }
    }

    /**
     * （带输入检查）纯执行器版本（不支持日志记录），可抛 Exception 异常
     *
     * @param actuator 执行器
     * @param input    输入
     */
    public static void failAt(final ExceptionActuator actuator, final Object... input) {
        if (HUt.isNotNull(input)) {
            HActuator.failAt(actuator, null);
        }
    }

    /**
     * Supplier函数的Exception版本（不支持日志记录），执行失败时抛出 Exception 异常
     *
     * @param supplier Supplier函数
     * @param <T>      返回值类型
     *
     * @return T
     */
    public static <T> T failOr(final ExceptionSupplier<T> supplier) {
        return HSupplier.failOr(null, supplier, (HLogger) null);
    }

    /**
     * Supplier函数的 Exception 版本（支持日志记录），执行失败时抛出 Exception 异常
     *
     * @param supplier Supplier函数
     * @param logger   日志记录器
     * @param <T>      返回值类型
     *
     * @return T
     */
    public static <T> T failOr(final ExceptionSupplier<T> supplier, final HLogger logger) {
        return HSupplier.failOr(null, supplier, logger);
    }


    /**
     * （参数非空检查）Supplier函数的 Exception 版本（默认值 null），可抛 Exception 异常
     *
     * @param supplier Supplier函数
     * @param input    待检查的输入信息
     * @param <T>      返回值类型
     *
     * @return T
     */
    public static <T> T failOr(final ExceptionSupplier<T> supplier, final Object... input) {
        return failOr(null, supplier, input);
    }

    /**
     * （参数非空检查）Supplier函数的 Exception 版本（支持默认值），可抛 Exception 异常
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
        if (HUt.isNotNull(input)) {
            return HSupplier.failOr(defaultValue, supplier, (HLogger) null);
        } else {
            return defaultValue;
        }
    }

    /**
     * Supplier函数的 Exception 版本（支持默认值），可抛 Exception 异常
     *
     * @param defaultValue 默认值
     * @param supplier     Supplier函数
     * @param <T>          返回值类型
     *
     * @return T
     */
    public static <T> T failOr(final T defaultValue, final ExceptionSupplier<T> supplier) {
        return HSupplier.failOr(defaultValue, supplier, (HLogger) null);
    }

    /**
     * Supplier函数的 Exception 版本（支持默认值 / 日志记录器），可抛 Exception 异常
     *
     * @param defaultValue 默认值
     * @param supplier     Supplier函数
     * @param logger       日志记录器
     * @param <T>          返回值类型
     *
     * @return T
     */
    public static <T> T failOr(final T defaultValue, final ExceptionSupplier<T> supplier,
                               final HLogger logger) {
        return HSupplier.failOr(defaultValue, supplier, logger);
    }
}
