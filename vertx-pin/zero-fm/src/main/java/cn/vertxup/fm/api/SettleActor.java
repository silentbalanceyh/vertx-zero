package cn.vertxup.fm.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class SettleActor {

    @Me
    @Address(Addr.Settle.UP_PAYMENT)
    public Future<JsonObject> upPayment(final boolean isRunUp,      // S Bill
                                        final JsonObject data) {
        // Items Processing

        return null;
    }
}
