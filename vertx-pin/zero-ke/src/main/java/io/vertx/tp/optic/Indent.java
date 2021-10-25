package io.vertx.tp.optic;

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
     * Multi Number
     */
    Future<Queue<String>> indent(String code, String sigma, int size);
}
