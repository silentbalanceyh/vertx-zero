package io.vertx.tp.crud.uca.desk;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.ke.atom.connect.KJoin;
import io.vertx.tp.ke.atom.connect.KPoint;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IxNext {
    private transient final IxIn in;

    private IxNext(final IxIn in) {
        this.in = in;
    }

    public static IxNext on(final IxIn in) {
        return new IxNext(in);
    }

    public Future<JsonObject> runJJJ(final JsonObject input, final JsonObject active) {
        return Ux.future(this.run(input, active));
    }

    private JsonObject run(final JsonObject input, final JsonObject active) {
        if (this.in.canJoin()) {
            final KPoint point = IxPin.point(this.in);
            final KJoin connect = this.in.module().getConnect();
            /*
             * Put joinedKey into data
             * Remove key
             */
            final JsonObject data = input.copy().mergeIn(active, true);
            connect.procFilters(data, point, data);
            data.remove(this.in.module().getField().getKey());
            return data;
        } else {
            /*
             * Returned Active executor result
             */
            return active.copy();
        }
    }

    public Future<JsonArray> runAJA(final JsonArray input, final JsonObject active) {
        Ut.itJArray(input).forEach(data -> this.run(data, active));
        return Ux.future(input);
    }

    public Future<JsonArray> runJAA(final JsonObject input, final JsonArray array) {
        Ut.itJArray(array).forEach(data -> this.run(data, new JsonObject()));
        return Ux.future(array);
    }
}
