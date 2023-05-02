package io.vertx.up.uca.sectio;

import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface After {

    Set<ChangeFlag> types();

    /*
     * After Operation
     */
    default Future<JsonObject> afterAsync(final JsonObject data, final JsonObject config) {
        return Ux.future(data);
    }

    default Future<JsonArray> afterAsync(final JsonArray data, final JsonObject config) {
        return Ux.future(data);
    }
}
