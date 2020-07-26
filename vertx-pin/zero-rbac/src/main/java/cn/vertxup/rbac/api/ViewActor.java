package cn.vertxup.rbac.api;

import cn.vertxup.rbac.service.view.RuleStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.ke.cv.KeDefault;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;

import javax.inject.Inject;

@Queue
public class ViewActor {

    @Inject
    private transient RuleStub ruleStub;

    @Address(Addr.Authority.VIEW_UPDATE_BY_TYPE)
    public Future<JsonArray> saveViews(final String ownerType,
                                       final String owner,
                                       final JsonArray views) {
        // final String habit     = Ke.keyHabitus(envelop);
        return this.ruleStub.saveViews(ownerType, owner, views, KeDefault.VIEW_DEFAULT);
    }

    @Address(Addr.Rule.FETCH_VIEWS)
    public Future<JsonArray> fetchByKeys(final String ownerType,
                                         final String owner,
                                         final JsonArray keys) {
        return this.ruleStub.fetchViews(ownerType, owner, keys, KeDefault.VIEW_DEFAULT);
    }
}
