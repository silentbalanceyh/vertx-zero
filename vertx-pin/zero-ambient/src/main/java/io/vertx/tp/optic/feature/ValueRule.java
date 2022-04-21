package io.vertx.tp.optic.feature;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ValueRule implements Valve {
    @Override
    public Future<JsonObject> execAsync(final JsonObject data, final JsonObject config) {
        return null;
    }
}
