package io.vertx.up.runtime;

import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ZeroCodex {

    private static final ConcurrentMap<String, JsonObject> CODEX =
        new ConcurrentHashMap<>();

    public static ConcurrentMap<String, JsonObject> getCodex() {
        return CODEX;
    }

    public static JsonObject getCodex(final String key) {
        return CODEX.get(key);
    }
}
