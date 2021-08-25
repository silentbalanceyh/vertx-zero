package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.up.fn.Fn;

/**
 * I -> I
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Agonic {

    static Agonic creation() {
        return Fn.poolThread(Pooled.AGONIC_MAP, CAgonic::new, CAgonic.class.getName());
    }

    Future<JsonObject> runAsync(JsonObject input, IxIn module);

    Future<JsonArray> runAsync(JsonArray input, IxIn module);
}
