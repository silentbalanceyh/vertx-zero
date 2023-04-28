package io.horizon.util.fn;

import io.horizon.fn.Actuator;
import io.horizon.log.HLogger;
import io.horizon.util.HaS;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author lang : 2023/4/28
 */
class _Run extends _Jvm {
    protected _Run() {
    }

    /**
     * 纯执行器版本（支持日志记录器）
     *
     * @param actuator 执行器
     * @param logger   日志记录器
     */
    public static void runAt(final Actuator actuator, final HLogger logger) {
        HActuator.runAt(actuator, logger);
    }

    /**
     * 纯执行器版本（不支持日志记录器）
     *
     * @param actuator 执行器
     */
    public static void runAt(final Actuator actuator) {
        HActuator.runAt(actuator, null);
    }

    /**
     * （带输入检查）纯执行器版本（支持日志记录器）
     *
     * @param actuator 执行器
     * @param logger   日志记录器
     * @param input    输入
     */
    public static void runAt(final Actuator actuator, final HLogger logger,
                             final Object... input) {
        if (HaS.isNotNull(input)) {
            HActuator.runAt(actuator, logger);
        }
    }

    /**
     * （带输入检查）纯执行器版本（不支持日志记录器）
     *
     * @param actuator 执行器
     * @param input    输入
     */
    public static void runAt(final Actuator actuator, final Object... input) {
        if (HaS.isNotNull(input)) {
            HActuator.runAt(actuator, null);
        }
    }

    /**
     * Supplier函数的普通版，返回值为 null 时可取默认值
     *
     * @param supplier Supplier函数
     * @param <T>      返回值类型
     *
     * @return T
     */
    public static <T> T runOr(final Supplier<T> supplier) {
        return HSupplier.runOr(null, supplier);
    }

    /**
     * Supplier函数的普通版，返回值为 null 时可取默认值
     *
     * @param defaultValue 默认值
     * @param supplier     Supplier函数
     * @param <T>          返回值类型
     *
     * @return T
     */
    public static <T> T runOr(final T defaultValue, final Supplier<T> supplier) {
        return HSupplier.runOr(defaultValue, supplier);
    }

    /**
     * （带输入检查）Supplier函数的普通版，返回值为 null 时可取默认值
     *
     * @param supplier Supplier函数
     * @param input    输入
     * @param <T>      返回值类型
     *
     * @return T
     */
    public static <T> T runOr(final Supplier<T> supplier, final Object... input) {
        if (HaS.isNotNull(input)) {
            return HSupplier.runOr(null, supplier);
        } else {
            return null;
        }
    }

    /**
     * （带输入检查）Supplier函数的普通版，返回值为 null 时可取默认值
     *
     * @param supplier Supplier函数
     * @param input    输入（比较特殊的 String[]）
     * @param <T>      返回值类型
     *
     * @return T
     */
    public static <T> T runOr(final Supplier<T> supplier, final String... input) {
        if (HaS.isNotNil(input)) {
            return HSupplier.runOr(null, supplier);
        } else {
            return null;
        }
    }

    /**
     * （带输入检查）Supplier函数的普通版，返回值为 null 时可取默认值
     *
     * @param supplier Supplier函数
     * @param input    输入
     * @param <T>      返回值类型
     *
     * @return T
     */
    public static <T> T runOr(final T defaultValue, final Supplier<T> supplier,
                              final Object... input) {
        if (HaS.isNotNull(input)) {
            return HSupplier.runOr(defaultValue, supplier);
        } else {
            return defaultValue; // HSupplier.runOr(null, supplier);
        }
    }

    /**
     * （带输入检查）Supplier函数的普通版，返回值为 null 时可取默认值
     *
     * @param supplier Supplier函数
     * @param input    输入 （比较特殊的 String[]）
     * @param <T>      返回值类型
     *
     * @return T
     */

    public static <T> T runOr(final T defaultValue, final Supplier<T> supplier,
                              final String... input) {
        if (HaS.isNotNil(input)) {
            return HSupplier.runOr(defaultValue, supplier);
        } else {
            return defaultValue; // return HSupplier.runOr(null, supplier);
        }
    }

    /**
     * 带条件选择（支持日志记录）的 Supplier 版本，外层使用 jvmOr 封装，保证出任何状况
     * 时都可以记录相关日志
     * 1. 条件满足时执行 trueSupplier
     * 2. 条件满足时执行 falseSupplier
     *
     * @param condition     条件
     * @param logger        日志记录器
     * @param trueSupplier  条件满足时执行的 Supplier
     * @param falseSupplier 条件不满足时执行的 Supplier
     * @param <T>           返回值类型
     *
     * @return T
     */
    public static <T> T runOr(final boolean condition, final HLogger logger,
                              final Supplier<T> trueSupplier, final Supplier<T> falseSupplier) {
        if (condition) {
            return jvmOr(null, trueSupplier::get, logger);
        } else {
            return Objects.nonNull(falseSupplier) ?
                jvmOr(null, falseSupplier::get, logger) : null;
        }
    }

    /**
     * 带条件选择（支持日志记录）的 Supplier 版本，外层使用 jvmOr 封装，保证出任何状况
     *
     * @param condition    条件
     * @param logger       日志记录器
     * @param trueSupplier 条件满足时执行的 Supplier
     * @param <T>          返回值类型
     *
     * @return T
     */
    public static <T> T runOr(final boolean condition, final HLogger logger,
                              final Supplier<T> trueSupplier) {
        return runOr(condition, logger, trueSupplier, null);
    }

    /**
     * 带条件选择（支持日志记录）的 Supplier 版本，外层使用 jvmOr 封装，保证出任何状况
     * 时都可以记录相关日志
     * 1. 条件满足时执行 trueSupplier
     * 2. 条件满足时执行 falseSupplier
     *
     * @param condition     条件
     * @param trueSupplier  条件满足时执行的 Supplier
     * @param falseSupplier 条件不满足时执行的 Supplier
     * @param <T>           返回值类型
     *
     * @return T
     */
    public static <T> T runOr(final boolean condition,
                              final Supplier<T> trueSupplier, final Supplier<T> falseSupplier) {
        return runOr(condition, null, trueSupplier, falseSupplier);
    }

    /**
     * 带条件选择（支持日志记录）的 Supplier 版本，外层使用 jvmOr 封装，保证出任何状况
     *
     * @param condition    条件
     * @param trueSupplier 条件满足时执行的 Supplier
     * @param <T>          返回值类型
     *
     * @return T
     */
    public static <T> T runOr(final boolean condition, final Supplier<T> trueSupplier) {
        return runOr(condition, null, trueSupplier, null);
    }

    /**
     * 带条件选择（支持日志记录）的 执行器 版本，外层使用 jvmAt 封装，保证出任何状况
     *
     * @param condition     条件
     * @param logger        日志记录器
     * @param trueActuator  条件满足时执行的 执行器
     * @param falseActuator 条件不满足时执行的 执行器
     */
    public static void runAt(final boolean condition, final HLogger logger,
                             final Actuator trueActuator, final Actuator falseActuator) {
        if (condition) {
            jvmAt(trueActuator::execute, logger);
        } else {
            if (Objects.nonNull(falseActuator)) {
                jvmAt(falseActuator::execute, logger);
            }
        }
    }

    /**
     * 带条件选择（支持日志记录）的 执行器 版本，外层使用 jvmAt 封装，保证出任何状况
     *
     * @param condition    条件
     * @param logger       日志记录器
     * @param trueActuator 条件满足时执行的 执行器
     */
    public static void runAt(final boolean condition, final HLogger logger,
                             final Actuator trueActuator) {
        runAt(condition, logger, trueActuator, null);
    }

    /**
     * 带条件选择（支持日志记录）的 执行器 版本，外层使用 jvmAt 封装，保证出任何状况
     *
     * @param condition     条件
     * @param trueActuator  条件满足时执行的 执行器
     * @param falseActuator 条件不满足时执行的 执行器
     */
    public static void runAt(final boolean condition,
                             final Actuator trueActuator, final Actuator falseActuator) {
        runAt(condition, null, trueActuator, falseActuator);
    }

    /**
     * 带条件选择（支持日志记录）的 执行器 版本，外层使用 jvmAt 封装，保证出任何状况
     *
     * @param condition    条件
     * @param trueActuator 条件满足时执行的 执行器
     */
    public static void runAt(final boolean condition, final Actuator trueActuator) {
        runAt(condition, null, trueActuator, null);
    }
}
