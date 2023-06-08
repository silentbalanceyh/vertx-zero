package io.vertx.up.boot.anima;

import io.macrocosm.specification.config.HConfig;
import io.vertx.up.eon.em.container.ServerType;

import java.util.concurrent.ConcurrentMap;

/**
 * Start Up condition for different bottle deployment.
 */
public interface Factor {
    ConcurrentMap<ServerType, Class<?>> endpoint(HConfig config);
}
