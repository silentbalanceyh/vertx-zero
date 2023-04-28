package io.horizon.util.fn;

import io.horizon.exception.ProgramException;
import io.horizon.fn.ProgramActuator;
import io.horizon.fn.ProgramBiConsumer;
import io.horizon.log.HLogger;
import io.horizon.util.HaS;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author lang : 2023/4/28
 */
class _Bug {
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
        if (HaS.isNotNull(input)) {
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
        if (HaS.isNotNull(input)) {
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
}
