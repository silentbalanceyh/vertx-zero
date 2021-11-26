package cn.vertxup.ambient.api.application;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class MenuActor {

    @Address(Addr.Menu.MY_FETCH)
    public Future<JsonArray> fetchMy(final JsonObject condition) {
        return Ux.futureA();
    }

    @Address(Addr.Menu.MY_SAVE)
    public Future<JsonArray> saveMy(final JsonObject data) {
        return Ux.futureA();
    }
}
