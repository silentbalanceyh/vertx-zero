package cn.vertxup.ambient.api;

import cn.vertxup.ambient.service.ModelStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.config.XHeader;
import jakarta.inject.Inject;

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

    @Address(Addr.Module.MODELS)
    public Future<JsonArray> fetchModels(final String sigma) {
        return this.stub.fetchModels(sigma);
    }

    @Address(Addr.Module.MODEL_FIELDS)
    public Future<JsonArray> fetchAttrs(final String identifier,
                                        final XHeader header) {
        return this.stub.fetchAttrs(identifier, header.getSigma());
    }
}
