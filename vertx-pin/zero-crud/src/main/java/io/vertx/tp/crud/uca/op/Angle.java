package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.up.fn.Fn;

/**
 * I -> T
 *
 * Example: JsonObject -> JsonArray
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Angle {

    static Angle full() {
        return Fn.poolThread(Pooled.ANGLE_MAP, FullAngle::new, FullAngle.class.getName());
    }

    Future<JsonArray> runAsync(JsonObject input, IxIn module);
}
