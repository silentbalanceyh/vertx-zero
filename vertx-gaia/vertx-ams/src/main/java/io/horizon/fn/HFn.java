package io.horizon.fn;

import io.horizon.log.HLogger;

/**
 * HFn采用了Java中的静态原型链结构
 *
 * @author lang : 2023/4/27
 */
public class HFn extends _Run {
    protected HFn() {
    }

    /**
     * 纯执行器版本（不支持日志记录器），执行失败时抛出 Exception 异常
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

    /**
     * 纯执行器版本（支持日志记录器），执行失败时抛出 Exception 异常
     *
     * @param actuator 执行器
     * @param logger   日志记录器
     */
    public static void failAt(final ExceptionActuator actuator, final HLogger logger) {
        HActuator.failAt(actuator, logger);
    }
}
