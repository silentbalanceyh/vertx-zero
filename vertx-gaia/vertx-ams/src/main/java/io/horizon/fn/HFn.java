package io.horizon.fn;

import io.horizon.exception.AbstractException;
import io.horizon.specification.component.HLogger;

import java.util.function.Supplier;

/**
 * HFn采用了Java中的静态原型链结构
 *
 * @author lang : 2023/4/27
 */
public class HFn extends _Run {
    protected HFn() {
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

    /**
     * 特殊方法（不支持日志记录器），执行失败处理的抽象异常直接以日志方式输出
     *
     * @param supplier Supplier函数
     * @param runCls   抽象异常类
     * @param args     抽象异常参数
     * @param <T>      返回值类型
     *
     * @return T
     */
    public static <T> T failOr(final Supplier<T> supplier,
                               final Class<? extends AbstractException> runCls, final Object... args) {
        return HSupplier.failOr(supplier, runCls, args);
    }
}
