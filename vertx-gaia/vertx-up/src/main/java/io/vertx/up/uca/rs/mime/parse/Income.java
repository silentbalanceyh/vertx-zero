package io.vertx.up.uca.rs.mime.parse;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.exception.WebException;

/**
 * Incoming message for request
 *
 * @param <T>
 */
public interface Income<T> {
    /**
     * request mime analyzing
     *
     * @param context
     * @param event
     * @return
     * @throws WebException
     */
    T in(RoutingContext context, Event event)
            throws WebException;
}
