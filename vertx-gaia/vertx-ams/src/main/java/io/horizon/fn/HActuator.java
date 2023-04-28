package io.horizon.fn;

import io.horizon.exception.ProgramException;
import io.horizon.uca.log.HLogger;

/**
 * @author lang : 2023/4/28
 */
class HActuator {
    private HActuator() {
    }

    static void jvmAt(final ErrorActuator actuator, final HLogger logger) {
        HFunction.jvmAt(null, (t) -> {
            actuator.execute();
            return null;
        }, logger);
    }

    static void bugAt(final ProgramActuator actuator, final HLogger logger) throws ProgramException {
        HFunction.bugAt(null, (t) -> {
            actuator.execute();
            return null;
        }, logger);
    }

    static void failAt(final ExceptionActuator actuator, final HLogger logger) {
        HFunction.failAt(null, (t) -> {
            actuator.execute();
            return null;
        }, logger);
    }

    static void runAt(final Actuator actuator, final HLogger logger) {
        HFunction.jvmAt(null, (t) -> {
            actuator.execute();
            return null;
        }, logger);
    }
}
