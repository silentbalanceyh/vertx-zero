package cn.vertxup.ambient.api;

import cn.vertxup.ambient.service.DatumStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import jakarta.inject.Inject;

@Queue
public class DatumActor {

    @Inject
    private transient DatumStub stub;

    @Address(Addr.Datum.CATEGORY_TYPE)
    public Future<JsonArray> fetchCategory(final String appId, final String type, final Boolean includeLeaf) {
        return this.stub.treeApp(appId, type, includeLeaf);
    }

    @Address(Addr.Datum.CATEGORY_TYPES)
    public Future<JsonArray> fetchCategory(final String appId, final JsonArray types) {
        return this.stub.treeApp(appId, types);
    }

    @Address(Addr.Datum.CATEGORY_CODE)
    public Future<JsonObject> fetchCategory(final String appId, final String type, final String code) {
        return this.stub.treeApp(appId, type, code);
    }

    @Address(Addr.Datum.TABULAR_TYPE)
    public Future<JsonArray> fetchTabular(final String appId, final String type) {
        return this.stub.dictApp(appId, type);
    }

    @Address(Addr.Datum.TABULAR_TYPES)
    public Future<JsonArray> fetchTabular(final String appId, final JsonArray types) {
        return this.stub.dictApp(appId, types);
    }

    @Address(Addr.Datum.TABULAR_CODE)
    public Future<JsonObject> fetchTabular(final String appId, final String type, final String code) {
        return this.stub.dictApp(appId, type, code);
    }
}
