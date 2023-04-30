package io.vertx.up.fn;


import io.horizon.exception.ProgramException;
import io.horizon.fn.ProgramActuator;
import io.horizon.uca.log.Annal;

final class Wait {
    private static final Annal LOGGER = Annal.get(Wait.class);

    private Wait() {
    }

    static void wrapper(final Annal logger, final ProgramActuator actuator) {
        try {
            actuator.execute();
        } catch (final ProgramException ex) {
            logger.fatal(ex);
            //            Annal.?ure(logger, () -> logger.checked(ex));
            //            throw new AbstractException(ex.getMessage()) {
            //            };
        } catch (final Throwable ex) {
            logger.fatal(ex);
            //            Annal.?ure(logger, () -> logger.jvm(ex));
        }
    }
}
