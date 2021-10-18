package cn.vertxup.rbac.api;

import cn.vertxup.rbac.service.view.RuleStub;
import cn.vertxup.rbac.service.view.VisitStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.KValue;

import javax.inject.Inject;

@Queue
public class ViewActor {

    @Inject
    private transient RuleStub ruleStub;

    @Inject
    private transient VisitStub visitStub;

    @Address(Addr.View.VIEW_UPDATE_BY_TYPE)
    public Future<JsonArray> saveViews(final String ownerType,
                                       final String owner,
                                       final JsonArray views) {
        // final String habit     = Ke.keyHabitus(envelop);
        return this.ruleStub.saveViews(ownerType, owner, views, KValue.View.VIEW_DEFAULT);
    }

    @Address(Addr.Rule.FETCH_VIEWS)
    public Future<JsonArray> fetchByKeys(final String ownerType,
                                         final String owner,
                                         final JsonArray keys) {
        return this.ruleStub.fetchViews(ownerType, owner, keys, KValue.View.VIEW_DEFAULT);
    }

    @Address(Addr.Rule.FETCH_VISITANT)
    public Future<JsonObject> fetchVisitant(final String ownerType,
                                            final String owner,
                                            final JsonObject body) {
        return this.visitStub.fetchVisitant(ownerType, owner, body);
    }
}
