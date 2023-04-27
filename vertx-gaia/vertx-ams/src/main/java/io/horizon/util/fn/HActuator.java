package io.horizon.util.fn;

import io.horizon.fn.Actuator;
import io.horizon.fn.ErrorActuator;
import io.horizon.fn.ExceptionActuator;
import io.horizon.fn.ProgramActuator;
import io.horizon.log.HLogger;

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

    static void bugAt(final ProgramActuator actuator, final HLogger logger) {
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
