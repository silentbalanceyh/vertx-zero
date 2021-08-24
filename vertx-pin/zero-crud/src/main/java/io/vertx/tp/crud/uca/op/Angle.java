package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * I -> T
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Angle {
    
    Future<JsonArray> procAsync(JsonObject input);
}
