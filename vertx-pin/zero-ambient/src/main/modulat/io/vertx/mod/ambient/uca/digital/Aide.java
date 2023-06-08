package io.vertx.mod.ambient.uca.digital;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Aide {
    /*
     * (N) FIELD = ? AND TYPE IN (?,?)
     *     FIELD = ? AND TYPE = ?
     * (1) FIELD = ? AND TYPE = ? AND CODE = ?
     */
    Future<JsonArray> fetch(String field, JsonArray types);

    Future<JsonObject> fetch(String field, String type, String code);
}
