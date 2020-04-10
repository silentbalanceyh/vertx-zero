package cn.vertxup.ambient.api;

import cn.vertxup.ambient.service.DatumStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;

import javax.inject.Inject;

@Queue
public class DatumActor {

    @Inject
    private transient DatumStub stub;

    @Address(Addr.Datum.CATEGORY_TYPE)
    public Future<JsonArray> categoryByType(final String appId, final String type) {
        return stub.categories(appId, type);
    }

    @Address(Addr.Datum.CATEGORY_TYPES)
    public Future<JsonArray> fetchCategories(final String appId, final JsonArray types) {
        return stub.categories(appId, types);
    }

    @Address(Addr.Datum.CATEGORY_CODE)
    public Future<JsonObject> fetchCategory(final String appId, final String type, final String code) {
        return stub.category(appId, type, code);
    }

    @Address(Addr.Datum.TABULAR_TYPE)
    public Future<JsonArray> tabularByType(final String appId, final String type) {
        return stub.tabulars(appId, type);
    }

    @Address(Addr.Datum.TABULAR_TYPES)
    public Future<JsonArray> fetchTabulars(final String appId, final JsonArray types) {
        return stub.tabulars(appId, types);
    }

    @Address(Addr.Datum.TABULAR_CODE)
    public Future<JsonObject> fetchTabular(final String appId, final String type, final String code) {
        return stub.tabular(appId, type, code);
    }
}
