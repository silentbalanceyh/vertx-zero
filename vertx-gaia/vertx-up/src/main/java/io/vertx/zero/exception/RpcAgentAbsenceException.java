package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

public class RpcAgentAbsenceException extends BootingException {

    public RpcAgentAbsenceException(final Class<?> clazz,
                                    final Class<?> interfaceCls) {
        super(clazz, interfaceCls);
    }

    @Override
    public int getCode() {
        return -40048;
    }
}
