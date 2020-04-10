package io.vertx.zero.exception;

import io.vertx.up.exception.UpException;

public class RpcAgentAbsenceException extends UpException {

    public RpcAgentAbsenceException(final Class<?> clazz,
                                    final Class<?> interfaceCls) {
        super(clazz, interfaceCls);
    }

    @Override
    public int getCode() {
        return -40048;
    }
}
