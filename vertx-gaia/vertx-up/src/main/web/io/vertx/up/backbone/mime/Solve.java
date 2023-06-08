package io.vertx.up.backbone.mime;

import io.horizon.exception.WebException;

/**
 * # 「Co」Zero Solve
 *
 * This component will be called by specific `Resolver` internally.
 *
 * @param <T> Generic class
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Solve<T> {
    /**
     * Resolving workflow in `Resolver`
     *
     * @param content The body content of Class format
     *
     * @return The deserialization generic pojo class here
     * @throws WebException exception of Web request
     */
    T resolve(String content);
}
