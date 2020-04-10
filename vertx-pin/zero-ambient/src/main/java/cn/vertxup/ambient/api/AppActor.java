package cn.vertxup.ambient.api;

import cn.vertxup.ambient.service.AppStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;

import javax.inject.Inject;

@Queue
public class AppActor {

    @Inject
    private transient AppStub appStub;

    @Address(Addr.App.BY_NAME)
    public Future<JsonObject> byName(final String name) {
        return appStub.fetchByName(name);
    }

    @Address(Addr.App.BY_ID)
    public Future<JsonObject> byId(final String appId) {
        return appStub.fetchById(appId);
    }

    @Address(Addr.Menu.BY_APP_ID)
    public Future<JsonArray> fetchMenus(final String appId) {
        return appStub.fetchMenus(appId);
    }
}
