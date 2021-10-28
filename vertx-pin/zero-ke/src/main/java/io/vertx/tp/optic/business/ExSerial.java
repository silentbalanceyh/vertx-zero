package io.vertx.tp.optic.business;

import io.vertx.core.Future;

import java.util.List;

/*
 * EcSerial for serial generation in current environment
 * 1) it will impact crud module
 * 2) Also it could be used in different part
 */
public interface ExSerial {
    /*
     * Generate single serial
     */
    Future<String> serial(String sigma, String code);

    /*
     * Update serial definition
     */
    Future<Boolean> reset(String sigma, String code, Long defaultValue);

    default Future<Boolean> reset(final String sigma, final String code) {
        return this.reset(sigma, code, 1L);
    }

    /*
     * Generate multi serials
     */
    Future<List<String>> serial(String sigma, String code, Integer counter);
}
