package io.vertx.zero.exception;

import io.vertx.up.atom.agent.Event;
import io.vertx.up.exception.UpException;

public class EventActionNoneException extends UpException {

    public EventActionNoneException(final Class<?> clazz,
                                    final Event event) {
        super(clazz, event.getPath());
    }

    @Override
    public int getCode() {
        return -40008;
    }
}
