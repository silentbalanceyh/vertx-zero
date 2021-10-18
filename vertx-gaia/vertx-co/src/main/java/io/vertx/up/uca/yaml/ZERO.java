package io.vertx.up.uca.yaml;

import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Storage {
    /**
     * Data for each up.god.file
     */
    ConcurrentMap<String, JsonObject> CONFIG
        = new ConcurrentHashMap<>();
}

interface Key {
    /**
     * Vertx Zero configuration
     */
    String ZERO = "zero";
    /**
     * External Zero configuration
     */
    String LIME = "lime";
}

interface Info {

    String UNIFORM = "Uniform resources hitted to {0}, skipped keys {1}.";
}
