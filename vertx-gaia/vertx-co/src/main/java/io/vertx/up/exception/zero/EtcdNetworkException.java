package io.vertx.up.exception.zero;

import io.vertx.up.exception.UpException;

public class EtcdNetworkException extends UpException {

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
