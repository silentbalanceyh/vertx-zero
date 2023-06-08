package io.mature.extension.migration.modeling;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EmptyRevision implements Revision {
    @Override
    public Future<ConcurrentMap<String, JsonObject>> captureAsync(final ConcurrentMap<String, String> keyMap) {
        return Ux.future(new ConcurrentHashMap<>());
    }
}
