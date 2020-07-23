package cn.vertxup.rbac.api;

import cn.vertxup.rbac.service.view.ViewStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;

@Queue
public class ViewActor {

    @Inject
    private transient ViewStub viewStub;

    @Address(Addr.View.VIEW_UPDATE_BY_TYPE)
    public Future<JsonObject> updateViewByType(final Envelop envelop) {
        final String ownerType = Ux.getString(envelop).toUpperCase();
        final String key = Ux.getString1(envelop);
        final JsonObject data = Ux.getJson2(envelop);
        // final String habit     = Ke.keyHabitus(envelop);
        return this.viewStub.updateByType(ownerType, key, data);
    }
}
