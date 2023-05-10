package io.vertx.zero.exception;

import io.horizon.exception.BootingException;
import io.vertx.up.atom.agent.Event;

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
