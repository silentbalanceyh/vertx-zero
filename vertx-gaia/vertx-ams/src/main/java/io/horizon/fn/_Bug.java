package io.horizon.fn;

import io.horizon.exception.ProgramException;
import io.horizon.specification.uca.HLogger;
import io.horizon.util.HUt;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

/**
 * @author lang : 2023/4/28
 */
class _Bug extends _Async {
    protected _Bug() {
    }

    /**
     * 纯执行器版本（不支持日志记录），可抛 ProgramException 异常
     *
     * @param actuator 执行器
     *
     * @throws ProgramException 可抛出的异常
     */
    public static void bugAt(final ProgramActuator actuator) throws ProgramException {
        HActuator.bugAt(actuator, null);
    }

    /**
     * 纯执行器版本（支持日志记录），可抛 ProgramException 异常
     *
     * @param actuator 执行器
     * @param logger   日志记录器
     *
     * @throws ProgramException 可抛出的异常
     */
    public static void bugAt(final ProgramActuator actuator, final HLogger logger) throws ProgramException {
        HActuator.bugAt(actuator, logger);
    }

    /**
     * （带输入检查）纯执行器版本（支持日志记录），可抛 ProgramException 异常
     *
     * @param actuator 执行器
     * @param logger   日志记录器
     * @param input    输入
     *
     * @throws ProgramException 可抛出的异常
     */
    public static void bugAt(final ProgramActuator actuator, final HLogger logger,
                             final Object... input) throws ProgramException {
        if (HUt.isNotNull(input)) {
            HActuator.bugAt(actuator, logger);
        }
    }

    /**
     * （带输入检查）纯执行器版本（不支持日志记录），可抛 ProgramException 异常
     *
     * @param actuator 执行器
     * @param input    输入
     *
     * @throws ProgramException 可抛出的异常
     */
    public static void bugAt(final ProgramActuator actuator, final Object... input) throws ProgramException {
        if (HUt.isNotNull(input)) {
            HActuator.bugAt(actuator, null);
        }
    }

    /**
     * 带遍历的纯执行器版本（不支持日志记录），遍历 JsonObject，执行二元函数，
     * 注意二元函数的参数和 key = value 是反向的，执行时调用是 (value, key) 模式
     * 可抛 ProgramException 异常
     *
     * @param data     被遍历的JsonObject
     * @param consumer 二元函数
     * @param <T>      遍历到的每个键值对中的 value 类型
     *
     * @throws ProgramException 可抛出的异常
     */
    public static <T> void bugIt(final JsonObject data, final ProgramBiConsumer<T, String> consumer) throws ProgramException {
        HConsumer.bugIt(data, consumer);
    }

    /**
     * （只遍历JsonObject版本）带遍历的纯执行器版本（支持日志记录），遍历 JsonObject，执行二元函数，
     * 可抛 ProgramException 异常
     *
     * @param data     被遍历的JsonArray
     * @param consumer 二元函数
     * @param <T>      遍历到的每个元素中的原始类型
     *
     * @throws ProgramException 可抛出的异常
     */
    public static <T> void bugIt(final JsonArray data, final ProgramBiConsumer<T, Integer> consumer) throws ProgramException {
        HConsumer.bugIt(data, JsonObject.class, consumer);
    }

    /**
     * 带遍历的纯执行器版本（支持日志记录），遍历 JsonObject，执行二元函数，
     * 可抛 ProgramException 异常
     *
     * @param data     被遍历的JsonArray
     * @param clazz    要求遍历元素的匹配类型
     * @param consumer 二元函数
     * @param <T>      遍历到的每个元素中的原始类型
     *
     * @throws ProgramException 可抛出的异常
     */
    public static <T> void bugIt(final JsonArray data, final Class<T> clazz,
                                 final ProgramBiConsumer<T, Integer> consumer) throws ProgramException {
        HConsumer.bugIt(data, clazz, consumer);
    }

    /**
     * 带条件选择（支持日志记录）的 执行器 版本，外层使用 bugAt 封装，保证出任何状况
     *
     * @param condition     条件
     * @param logger        日志记录器
     * @param trueActuator  条件满足时执行的 执行器
     * @param falseActuator 条件不满足时执行的 执行器
     *
     * @throws ProgramException 可抛出的异常
     */
    public static void bugAt(final boolean condition, final HLogger logger,
                             final ProgramActuator trueActuator, final ProgramActuator falseActuator) throws ProgramException {
        if (condition) {
            bugAt(trueActuator, logger);
        } else {
            if (Objects.nonNull(falseActuator)) {
                bugAt(falseActuator, logger);
            }
        }
    }

    /**
     * 带条件选择（不支持日志记录）的 执行器 版本，外层使用 bugAt 封装，保证出任何状况
     *
     * @param condition     条件
     * @param trueActuator  条件满足时执行的 执行器
     * @param falseActuator 条件不满足时执行的 执行器
     *
     * @throws ProgramException 可抛出的异常
     */
    public static void bugAt(final boolean condition,
                             final ProgramActuator trueActuator, final ProgramActuator falseActuator) throws ProgramException {
        bugAt(condition, null, trueActuator, falseActuator);
    }

    /**
     * 带条件选择（支持日志记录）的 执行器 版本，外层使用 bugAt 封装，保证出任何状况
     *
     * @param condition    条件
     * @param logger       日志记录器
     * @param trueActuator 条件满足时执行的 执行器
     *
     * @throws ProgramException 可抛出的异常
     */
    public static void bugAt(final boolean condition, final HLogger logger,
                             final ProgramActuator trueActuator) throws ProgramException {
        bugAt(condition, logger, trueActuator, null);
    }

    /**
     * 带条件选择（不支持日志记录）的 执行器 版本，外层使用 bugAt 封装，保证出任何状况
     *
     * @param condition    条件
     * @param trueActuator 条件满足时执行的 执行器
     *
     * @throws ProgramException 可抛出的异常
     */
    public static void bugAt(final boolean condition,
                             final ProgramActuator trueActuator) throws ProgramException {
        bugAt(condition, null, trueActuator, null);
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
     * @throws ProgramException 可抛出的异常
     */
    public static <T> T bugOr(final boolean condition, final HLogger logger,
                              final ProgramSupplier<T> trueSupplier, final ProgramSupplier<T> falseSupplier) throws ProgramException {
        if (condition) {
            return Objects.nonNull(trueSupplier) ?
                HSupplier.bugOr(null, trueSupplier, logger) : null;
        } else {
            return Objects.nonNull(falseSupplier) ?
                HSupplier.bugOr(null, falseSupplier, logger) : null;
        }
    }

    /**
     * 带条件选择（不支持日志记录）的 Supplier 版本，外层使用 jvmOr 封装，保证出任何状况
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
     * @throws ProgramException 可抛出的异常
     */
    public static <T> T bugOr(final boolean condition,
                              final ProgramSupplier<T> trueSupplier, final ProgramSupplier<T> falseSupplier) throws ProgramException {
        return bugOr(condition, null, trueSupplier, falseSupplier);
    }

    /**
     * 带条件选择（支持日志记录）的 Supplier 版本，外层使用 jvmOr 封装，保证出任何状况
     * 时都可以记录相关日志
     * 1. 条件满足时执行 trueSupplier
     *
     * @param condition    条件
     * @param logger       日志记录器
     * @param trueSupplier 条件满足时执行的 Supplier
     * @param <T>          返回值类型
     *
     * @return T
     * @throws ProgramException 可抛出的异常
     */
    public static <T> T bugOr(final boolean condition, final HLogger logger,
                              final ProgramSupplier<T> trueSupplier) throws ProgramException {
        return bugOr(condition, logger, trueSupplier, null);
    }

    /**
     * 带条件选择（不支持日志记录）的 Supplier 版本，外层使用 jvmOr 封装，保证出任何状况
     * 时都可以记录相关日志
     * 1. 条件满足时执行 trueSupplier
     *
     * @param condition    条件
     * @param trueSupplier 条件满足时执行的 Supplier
     * @param <T>          返回值类型
     *
     * @return T
     * @throws ProgramException 可抛出的异常
     */
    public static <T> T bugOr(final boolean condition,
                              final ProgramSupplier<T> trueSupplier) throws ProgramException {
        return bugOr(condition, null, trueSupplier, null);
    }
}
