package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.error._501NotSupportException;
import io.vertx.up.fn.Fn;

/**
 * I -> I
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Agonic {

    static Agonic create() {
        return Fn.poolThread(Pooled.AGONIC_MAP, AgonicCreate::new, AgonicCreate.class.getName());
    }

    static Agonic search() {
        return Fn.poolThread(Pooled.AGONIC_MAP, AgonicSearch::new, AgonicSearch.class.getName());
    }

    static Agonic count() {
        return Fn.poolThread(Pooled.AGONIC_MAP, AgonicCount::new, AgonicCount.class.getName());
    }

    static Agonic get() {
        return Fn.poolThread(Pooled.AGONIC_MAP, AgonicByID::new, AgonicByID.class.getName());
    }

    Future<JsonObject> runAsync(JsonObject input, IxIn in);

    default Future<JsonArray> runBAsync(final JsonArray input, final IxIn in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }
}
