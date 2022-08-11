package io.vertx.tp.optic.environment;

import io.vertx.aeon.specification.app.AbstractHET;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AmbientHighway extends AbstractHET {

    @Override
    public ConcurrentMap<String, JsonObject> initialize() {
        final UnityApp app = new UnityAmbient();
        return app.connect();
    }
}
