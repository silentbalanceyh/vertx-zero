package cn.vertxup.jet.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.cv.JtAddr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.runtime.soul.UriAeon;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@Queue
public class UriActor {

    @Address(JtAddr.Aeon.NEW_ROUTE)
    public Future<Boolean> createUri(final JsonObject body) {
        UriAeon.mountRoute(body);
        return Future.succeededFuture(Boolean.TRUE);
    }
}
