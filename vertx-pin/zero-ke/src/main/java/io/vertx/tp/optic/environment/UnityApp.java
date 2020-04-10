package io.vertx.tp.optic.environment;

import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentMap;

/*
 * Application configuration for X_APP but the implementation should be provided
 * in another project instead of current one.
 */
public interface UnityApp {

    void initialize();

    ConcurrentMap<String, JsonObject> connect();
}
