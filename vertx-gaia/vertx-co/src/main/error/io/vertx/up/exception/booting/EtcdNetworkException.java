package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;

public class EtcdNetworkException extends BootingException {

    public EtcdNetworkException(final Class<?> clazz,
                                final String host,
                                final Integer port) {
        super(clazz, host, String.valueOf(port));
    }

    @Override
    public int getCode() {
        return -40039;
    }
}
