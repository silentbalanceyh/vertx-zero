package io.horizon.spi.modeler;

import io.vertx.core.Future;

import java.util.Queue;

/**
 * For `X_NUMBER` usage
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Indent {
    /*
     * Single Number
     */
    Future<String> indent(String code, String sigma);

    /*
     * Update serial definition
     */
    Future<Boolean> reset(String code, String sigma, Long defaultValue);

    default Future<Boolean> reset(final String code, final String sigma) {
        return this.reset(code, sigma, 1L);
    }

    /*
     * Multi Number
     */
    Future<Queue<String>> indent(String code, String sigma, int size);
}
