package cn.vertxup.fm.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.annotations.Sigma;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class BillActor {

    @Address(Addr.Bill.IN_PRE)
    @Sigma
    public Future<JsonObject> inPre(final JsonObject data) {

        return Ux.future(data);
    }
}
