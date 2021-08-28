package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface ViewStub {
    /*
     * Fetch view that belong to user
     * 1) user : user id
     * 2) resourceId
     * 3) view = KeDefault.VIEW_DEFAULT
     */
    Future<SView> fetchMatrix(String user, String resourceId, String view);

    /*
     * Returned:
     * {
     *     "projection": [],
     *     "criteria": {}
     * }
     */
    Future<JsonObject> saveMatrix(String user, JsonObject viewData,
                                  JsonArray projection, JsonObject criteria);

    Future<List<SView>> fetchMatrix(JsonArray role, String resourceId, String view);

}
