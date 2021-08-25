package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxIn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CAgonic implements Agonic {
    @Override
    public Future<JsonObject> runAsync(final JsonObject input, final IxIn module) {
        return null;
    }

    @Override
    public Future<JsonArray> runAsync(final JsonArray input, final IxIn module) {
        return null;
    }
}
