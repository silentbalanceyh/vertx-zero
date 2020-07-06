package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface ViewStub {

    Future<SView> fetchMatrix(String user, String resourceId, String view);

    Future<SView> saveMatrix(String user, String resourceId, String view, JsonArray projection);

    Future<List<SView>> fetchMatrix(JsonArray role, String resourceId, String view);

    Future<JsonObject> updateByType(String ownerType, String key, JsonObject data, String habit);

    Future<Boolean> deleteById(String key);
}
