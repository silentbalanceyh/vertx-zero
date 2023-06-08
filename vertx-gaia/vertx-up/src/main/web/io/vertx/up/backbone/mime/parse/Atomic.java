package io.vertx.up.backbone.mime.parse;

import io.horizon.exception.WebException;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;

/**
 * @param <T>
 */
public interface Atomic<T> {

    Epsilon<T> ingest(RoutingContext context,
                      Epsilon<T> income) throws WebException;
}
