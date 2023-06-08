package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class WorkerMissingException extends BootingException {

    public WorkerMissingException(final Class<?> clazz,
                                  final String address) {
        super(clazz, address);
    }

    @Override
    public int getCode() {
        return -40014;
    }
}
