package cn.vertxup.rbac.api;

import cn.vertxup.rbac.domain.tables.daos.SViewDao;
import cn.vertxup.rbac.domain.tables.pojos.SAction;
import cn.vertxup.rbac.service.accredit.ActionStub;
import cn.vertxup.rbac.service.view.PersonalStub;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.Addr;
import io.vertx.mod.rbac.cv.em.OwnerType;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Queue
public class ViewMyActor {

    @Inject
    private transient PersonalStub personalStub;
    @Inject
    private transient ActionStub actionStub;

    // ------------------- Personal View
    @Address(Addr.View.VIEW_P_ADD)
    public Future<JsonObject> pViewCreate(final Envelop envelop) {
        /*
         * name, title, projection, criteria
         */
        return this.pAction(envelop).compose(action -> {
            final JsonObject data = Ux.getJson(envelop);
            final String userId = envelop.userId();
            final JsonObject normalized = data.copy();
            normalized.put(KName.USER, userId);
            normalized.mergeIn(envelop.headersX());
            // Bind the resourceId when create new
            normalized.put(KName.RESOURCE_ID, action.getResourceId());
            normalized.put("owner", userId);
            normalized.put("ownerType", OwnerType.USER.name());
            return this.personalStub.create(normalized).compose(Ux::futureJ);
        });
    }

    @Address(Addr.View.VIEW_P_DELETE)
    public Future<Boolean> pViewDelete(final String key) {
        final Set<String> keys = new HashSet<>();
        keys.add(key);
        return this.personalStub.delete(keys);
    }

    @Address(Addr.View.VIEW_P_BY_USER)
    public Future<JsonArray> pViewByUser(final Envelop envelop) {

        return this.pAction(envelop).compose(action -> {
            if (Objects.isNull(action)) {
                return Ux.futureA();
            } else {
                final JsonObject data = Ux.getJson(envelop);
                final String userId = envelop.userId();
                return this.personalStub.byUser(action.getResourceId(), userId,
                        data.getString(KName.POSITION))
                    .compose(Ux::futureA)
                    .compose(Fn.ofJArray(Ir.KEY_CRITERIA, Ir.KEY_PROJECTION, KName.Rbac.ROWS));
            }
        });
    }

    @Address(Addr.View.VIEW_P_EXISTING)
    public Future<Boolean> pViewExisting(final Envelop envelop) {
        return this.pAction(envelop).compose(action -> {
            if (Objects.isNull(action)) {
                return Future.succeededFuture(Boolean.FALSE);
            } else {
                final JsonObject data = Ux.getJson(envelop);
                final String userId = envelop.userId();
                /*
                 * condition
                 */
                final JsonObject criteria = new JsonObject();
                criteria.mergeIn(data.copy());
                criteria.remove(KName.URI);
                criteria.remove(KName.METHOD);
                criteria.put("owner", userId);
                criteria.put("ownerType", OwnerType.USER.name());
                return Ux.Jooq.on(SViewDao.class).existAsync(criteria);
            }
        });
    }

    private Future<SAction> pAction(final Envelop envelop) {
        final JsonObject header = envelop.headersX();
        final String sigma = header.getString(KName.SIGMA);

        final JsonObject data = Ux.getJson(envelop);
        final String uri = data.getString(KName.URI);
        final HttpMethod method = HttpMethod.valueOf(data.getString(KName.METHOD));

        return this.actionStub.fetchAction(uri, method, sigma);
    }

    @Address(Addr.View.VIEW_P_BY_ID)
    public Future<JsonObject> pViewById(final String key) {
        return this.personalStub.byId(key)
            .compose(Ux::futureJ)
            .compose(Fn.ofJObject(Ir.KEY_CRITERIA, Ir.KEY_PROJECTION, "rows"));
    }

    @Address(Addr.View.VIEW_P_UPDATE)
    public Future<JsonObject> pViewUpdate(final Envelop envelop) {
        /*
         * name, title, projection, criteria
         */
        final String key = Ux.getString(envelop);
        final JsonObject data = Ux.getJson1(envelop);
        final String userId = envelop.userId();
        data.put(KName.USER, userId);
        return this.personalStub.update(key, data)
            .compose(Ux::futureJ)
            .compose(Fn.ofJObject(Ir.KEY_CRITERIA, Ir.KEY_PROJECTION, "rows"));
    }


    @Address(Addr.View.VIEW_P_BATCH_DELETE)
    public Future<Boolean> pViewsDelete(final JsonArray keys) {
        return this.personalStub.delete(Ut.toSet(keys));
    }
}
