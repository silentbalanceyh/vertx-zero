package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.error._501NotSupportException;
import io.vertx.up.fn.Fn;

/**
 * I -> T
 *
 * Example: JsonObject -> JsonArray
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Angle {

    static Angle apeak(final boolean isMy) {
        if (isMy) {
            return Fn.poolThread(Pooled.ANGLE_MAP, AngleMy::new, AngleMy.class.getName());
        } else {
            return Fn.poolThread(Pooled.ANGLE_MAP, AngleFull::new, AngleFull.class.getName());
        }
    }

    static Angle all() {
        return Fn.poolThread(Pooled.ANGLE_MAP, AngleAll::new, AngleAll.class.getName());
    }

    default Future<JsonArray> runJAAsync(final JsonObject input, final IxIn in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    default Future<JsonArray> runAJAsync(final JsonArray input, final IxIn in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }
}
