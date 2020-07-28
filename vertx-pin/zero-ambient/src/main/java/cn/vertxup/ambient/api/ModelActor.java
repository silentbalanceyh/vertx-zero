package cn.vertxup.ambient.api;

import cn.vertxup.ambient.service.ModelStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;

import javax.inject.Inject;

@Queue
public class ModelActor {

    @Inject
    private transient ModelStub stub;

    @Address(Addr.Module.BY_NAME)
    public Future<JsonObject> fetchModule(
            final String appId,
            final String entry) {
        return this.stub.fetchModule(appId, entry);
    }
}
