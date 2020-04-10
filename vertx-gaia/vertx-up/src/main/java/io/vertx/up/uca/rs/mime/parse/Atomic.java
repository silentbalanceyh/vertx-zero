package io.vertx.up.uca.rs.mime.parse;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.exception.WebException;

/**
 * @param <T>
 */
public interface Atomic<T> {

    Epsilon<T> ingest(RoutingContext context,
                      Epsilon<T> income) throws WebException;
}
