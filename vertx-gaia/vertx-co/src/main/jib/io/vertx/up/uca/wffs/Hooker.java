package io.vertx.up.uca.wffs;

import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Hooker {

    Hooker bind(JsonObject config);

    boolean async();

    Future<JsonObject> execAsync(JsonObject data);

    default Future<JsonArray> execAsync(final JsonArray data) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }
}
