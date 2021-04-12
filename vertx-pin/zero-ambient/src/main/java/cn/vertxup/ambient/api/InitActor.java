package cn.vertxup.ambient.api;

import cn.vertxup.ambient.service.InitStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.config.Database;
import io.vertx.up.util.Ut;

import javax.inject.Inject;

@Queue
public class InitActor {

    @Inject
    private transient InitStub stub;

    @Address(Addr.Init.INIT)
    public Future<JsonObject> initApp(final String appId, final JsonObject data) {
        return this.stub.initCreation(appId, data);
    }

    @Address(Addr.Init.PREPARE)
    public Future<JsonObject> prepare(final String appName) {
        return this.stub.prerequisite(appName);
    }

    @Address(Addr.Init.CONNECT)
    public JsonObject connect(final JsonObject data) {
        final Database database = Ut.deserialize(data, Database.class);
        final boolean checked = database.test();
        return Ke.Result.bool(checked);
    }
}
