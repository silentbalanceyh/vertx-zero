package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.up.commune.Envelop;
import io.vertx.up.fn.Fn;

/**
 * I -> I
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Agonic {

    static Agonic creation() {
        return Fn.poolThread(Pooled.AGONIC_MAP, AgonicCreate::new, AgonicCreate.class.getName());
    }

    static Agonic search() {
        return Fn.poolThread(Pooled.AGONIC_MAP, AgonicSearch::new, AgonicSearch.class.getName());
    }

    Future<Envelop> runAsync(JsonObject input, IxIn in);

    Future<Envelop> runBAsync(JsonArray input, IxIn in);
}
