package io.vertx.up.exception.web;

import io.horizon.exception.WebException;

public class _500DeliveryErrorException extends WebException {

    public _500DeliveryErrorException(final Class<?> clazz,
                                      final String address,
                                      final String message) {
        super(clazz, address, message);
    }

    @Override
    public int getCode() {
        return -60002;
    }
}
