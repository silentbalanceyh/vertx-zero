package cn.vertxup.ambient.api.bridge;

import io.aeon.annotations.QaS;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@QaS
public class AppActor {

    @Address(Addr.App.BY_NAME)
    public Future<JsonObject> byName(final String name) {
        return Ux.futureJ();
    }
}
