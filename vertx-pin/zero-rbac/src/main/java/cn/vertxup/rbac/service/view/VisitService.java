package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.daos.SViewDao;
import cn.vertxup.rbac.domain.tables.daos.SVisitantDao;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import cn.vertxup.rbac.domain.tables.pojos.SVisitant;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KValue;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class VisitService implements VisitStub {

    @Override
    public Future<JsonObject> fetchVisitant(final String ownerType, final String ownerId,
                                            final JsonObject request) {
        final JsonObject input = Ut.sureJObject(request);
        Sc.infoView(this.getClass(), "Visitant Request: {0}", input);
        final String resourceId = input.getString(KName.RESOURCE_ID);
        final String type = input.getString(KName.TYPE);
        if (Ut.isNilOr(resourceId, type)) {
            return Ux.futureJ();
        } else {
            /*
             * Check the branch between
             *
             * identifier / configKey
             * */
            final String identifier = request.getString(KName.IDENTIFIER);
            final String configKey = request.getString("configKey");
            if (Ut.isNil(identifier) && Ut.isNil(configKey)) {
                /*
                 * identifier
                 */
                return Ux.futureJ();
            } else {
                /*
                 * basic condition to fetch view
                 */
                final JsonObject condition = new JsonObject();
                condition.put("owner", ownerId);
                condition.put("ownerType", ownerType);
                condition.put(KName.NAME, KValue.View.VIEW_DEFAULT);
                condition.put(KName.RESOURCE_ID, resourceId);
                Sc.infoView(this.getClass(), "Visitant View: {0}", condition.encode());
                return Ux.Jooq.on(SViewDao.class).<SView>fetchOneAsync(condition).compose(view -> {
                    if (Objects.isNull(view)) {
                        /*
                         * No view defined
                         */
                        return Ux.futureJ();
                    } else {
                        /*
                         * new condition for visitant
                         */
                        final JsonObject criteria = new JsonObject();
                        criteria.put("viewId", view.getKey());
                        criteria.put(KName.TYPE, type);
                        criteria.put(KName.SIGMA, view.getSigma());
                        if (Ut.notNil(identifier)) {
                            /*
                             * identifier as condition
                             * in this kind of situation, here are the definition for `identifier`
                             * because the returned result is `JsonObject`( unique record ), here we
                             * must provide additional condition ( configKey = DEFAULT ), it means
                             * master form in your interface.
                             */
                            criteria.put(KName.IDENTIFIER, identifier);
                            criteria.put("configKey", KValue.View.VIEW_DEFAULT);
                        } else {
                            /*
                             * configKey as condition only, because `configKey` provided and it is often
                             * UUID format, in this situation, the identifier is not needed
                             */
                            criteria.put("configKey", configKey);
                        }
                        Sc.infoView(this.getClass(), "Visitant Record: {0}", criteria.encode());
                        return Ux.Jooq.on(SVisitantDao.class).fetchOneAsync(criteria)
                            .compose(Ux::futureJ)
                            .compose(Ut.ifJObject(
                                "aclVisible",
                                "aclView",
                                "aclVariety",
                                "aclVow",
                                "aclVerge"
                            ));
                    }
                });
            }
        }
    }

    @Override
    public Future<JsonObject> saveAsync(final JsonObject request, final JsonObject view) {
        /*
         * Copy shared fields:
         * active
         * language
         * sigma
         * viewId
         */
        Ut.jsonCopy(request, view,
            KName.SIGMA, KName.LANGUAGE, KName.ACTIVE);
        request.put("viewId", view.getValue(KName.KEY));
        /*
         * Distinguish INSERT / UPDATE
         */
        final JsonObject criteria = new JsonObject();
        Ut.jsonCopy(criteria, request,
            "viewId", KName.TYPE, KName.SIGMA);
        /*
         * If `configKey` provide
         */
        final String configKey = request.getString("configKey");
        if (Ut.isNil(configKey)) {
            criteria.put(KName.IDENTIFIER, request.getValue(KName.IDENTIFIER));
            criteria.put("configKey", KValue.View.VIEW_DEFAULT);
        } else {
            criteria.put("configKey", configKey);
        }
        Sc.infoView(this.getClass(), "Visitant Upsert: {0}, Data: {1}",
            criteria.encode(), request.encode());
        Ut.ifString(request,
            "aclVisible",
            "aclView",
            "aclVariety",
            "aclVow",
            "aclVerge"
        );
        final SVisitant visitant = Ut.deserialize(request, SVisitant.class);
        return Ux.Jooq.on(SVisitantDao.class)
            /*
             * Insert and update
             */
            .upsertAsync(criteria, visitant)
            .compose(Ux::futureJ)
            .compose(Ut.ifJObject(
                "aclVisible",
                "aclView",
                "aclVariety",
                "aclVow",
                "aclVerge"
            ));
    }
}
