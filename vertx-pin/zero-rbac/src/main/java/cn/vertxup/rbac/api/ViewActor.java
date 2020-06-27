package cn.vertxup.rbac.api;

import cn.vertxup.rbac.service.view.ViewStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;

import javax.inject.Inject;

@Queue
public class ViewActor {

    @Inject
    private transient ViewStub viewStub;

    @Address(Addr.View.VIEW_UPDATE_BY_TYPE)
    public Future<JsonObject> updateViewByType(final String ownerType, final String key, final JsonObject data) {
        return viewStub.updateByType(ownerType, key, data);
    }
}
