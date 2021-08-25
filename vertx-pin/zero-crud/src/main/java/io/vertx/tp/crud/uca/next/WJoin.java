package io.vertx.tp.crud.uca.next;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.ke.atom.connect.KJoin;
import io.vertx.tp.ke.atom.connect.KPoint;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WJoin {
    private transient final IxIn in;

    private WJoin(final IxIn in) {
        this.in = in;
    }

    public static WJoin on(final IxIn in) {
        return new WJoin(in);
    }

    public Future<JsonObject> runAsync(final JsonObject input, final JsonObject active) {
        final KPoint point = IxPin.point(this.in);
        final KJoin connect = this.in.module().getConnect();
        /*
         * Put joinedKey into data
         * Remove key
         */
        final JsonObject data = input.copy().mergeIn(active, true);
        connect.procFilters(data, point, data);
        data.remove(this.in.module().getField().getKey());
        return Ux.future(data);
    }
}
