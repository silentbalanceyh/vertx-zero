package io.vertx.up.uca.micro.discovery.multipart;

import io.vertx.core.Handler;

/**
 * Pump definition for fix issue
 */
public interface Pipe<T> {

    void doRequest(Handler<T> handler);
}
