package io.horizon.util.fn;

import io.horizon.exception.ProgramException;
import io.horizon.fn.ProgramActuator;
import io.horizon.log.HLogger;
import io.horizon.util.HaS;

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
}
