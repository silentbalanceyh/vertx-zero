package io.vertx.up.uca.web.limit;

import io.vertx.up.eon.em.container.ServerType;

import java.util.concurrent.ConcurrentMap;

/**
 * Start Up condition for different verticle deployment.
 */
public interface Factor {
    /**
     * Filter
     *
     * @return
     */
    ConcurrentMap<ServerType, Class<?>> agents();
}
