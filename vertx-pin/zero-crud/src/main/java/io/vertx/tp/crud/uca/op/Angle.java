package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.up.fn.Fn;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * I -> T
 *
 * Example: JsonObject -> JsonArray
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Angle {

    static Angle full() {
        return Fn.poolThread(Pool.ANGLE_MAP, FullAngle::new, FullAngle.class.getName());
    }

    Future<JsonArray> procAsync(JsonObject input, IxIn module);
}

interface Pool {
    ConcurrentMap<String, Angle> ANGLE_MAP =
            new ConcurrentHashMap<>();
}
