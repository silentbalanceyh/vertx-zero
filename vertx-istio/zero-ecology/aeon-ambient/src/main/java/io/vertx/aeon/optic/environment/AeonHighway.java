package io.vertx.aeon.optic.environment;

import io.vertx.aeon.specification.app.AbstractHET;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonHighway extends AbstractHET {
    @Override
    public ConcurrentMap<String, JsonObject> initialize() {
        return new ConcurrentHashMap<>();
    }
}
