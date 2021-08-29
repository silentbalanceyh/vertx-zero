package cn.vertxup.rbac.api;

import cn.vertxup.rbac.domain.tables.daos.SViewDao;
import cn.vertxup.rbac.domain.tables.pojos.SAction;
import cn.vertxup.rbac.service.accredit.ActionStub;
import cn.vertxup.rbac.service.view.PersonalStub;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.tp.rbac.cv.em.OwnerType;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Queue
public class ViewPersonalActor {

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
        final JsonObject data = Ux.getJson(envelop);
        final String userId = Ke.keyUser(envelop);
        data.put(KName.USER, userId);
        data.mergeIn(envelop.headersX());
        return this.personalStub.create(data).compose(Ux::futureJ);
    }

    @Address(Addr.View.VIEW_P_DELETE)
    public Future<Boolean> pViewDelete(final String key) {
        final Set<String> keys = new HashSet<>();
        keys.add(key);
        return this.personalStub.delete(keys);
    }

    @Address(Addr.View.VIEW_P_BY_USER)
    public Future<JsonArray> pViewByUser(final Envelop envelop) {
        final String userId = Ke.keyUser(envelop);

        return this.pAction(envelop).compose(action -> {
            if (Objects.isNull(action)) {
                return Ux.futureA();
            } else {
                return this.personalStub.byUser(action.getResourceId(), userId)
                    .compose(Ux::futureA)
                    .compose(Ut.ifJArray(Qr.KEY_CRITERIA, Qr.KEY_PROJECTION, "rows"));
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
                final String userId = Ke.keyUser(envelop);
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
            .compose(Ut.ifJObject(Qr.KEY_CRITERIA, Qr.KEY_PROJECTION, "rows"));
    }

    @Address(Addr.View.VIEW_P_UPDATE)
    public Future<JsonObject> pViewUpdate(final Envelop envelop) {
        /*
         * name, title, projection, criteria
         */
        final String key = Ux.getString(envelop);
        final JsonObject data = Ux.getJson1(envelop);
        final String userId = Ke.keyUser(envelop);
        data.put(KName.USER, userId);
        return this.personalStub.update(key, data).compose(Ux::futureJ);
    }


    @Address(Addr.View.VIEW_P_BATCH_DELETE)
    public Future<Boolean> pViewsDelete(final JsonArray keys) {
        return this.personalStub.delete(Ut.toSet(keys));
    }
}
