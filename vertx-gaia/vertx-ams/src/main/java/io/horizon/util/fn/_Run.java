package io.horizon.util.fn;

import io.horizon.fn.Actuator;
import io.horizon.log.HLogger;
import io.horizon.util.HaS;

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
}
