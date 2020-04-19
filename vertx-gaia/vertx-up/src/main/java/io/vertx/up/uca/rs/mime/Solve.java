package io.vertx.up.uca.rs.mime;

import io.vertx.up.exception.WebException;

/**
 * # 「Co」Zero Solve
 *
 * This component will be called by specific `Resolver` internally.
 *
 * @param <T> Generic class
 *
 * @author <a href="http://www.origin-x.cn">lang</a>
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
