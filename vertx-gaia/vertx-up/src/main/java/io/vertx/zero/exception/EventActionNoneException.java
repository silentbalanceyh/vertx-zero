package io.vertx.zero.exception;

import io.vertx.up.atom.agent.Event;
import io.horizon.exception.BootingException;

public class EventActionNoneException extends BootingException {

    public EventActionNoneException(final Class<?> clazz,
                                    final Event event) {
        super(clazz, event.getPath());
    }

    @Override
    public int getCode() {
        return -40008;
    }
}
