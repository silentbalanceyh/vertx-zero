package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.daos.SViewDao;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.cv.em.OwnerType;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ViewService implements ViewStub {

    private static final Annal LOGGER = Annal.get(ViewService.class);

    @Override
    public Future<SView> fetchMatrix(final String userId, final String resourceId, final String view) {
        /* Find user matrix */
        final JsonObject filters = this.toFilters(resourceId, view);
        filters.put("owner", userId);
        filters.put("ownerType", OwnerType.USER.name());
        Sc.infoResource(ViewService.LOGGER, AuthMsg.VIEW_PROCESS, "fetch", filters.encode());
        return Ux.Jooq.on(SViewDao.class)
            .fetchOneAsync(new JsonObject().put("criteria", filters));
    }

    @Override
    public Future<JsonObject> saveMatrix(final String userId, final JsonObject viewData,
                                         final JsonArray projection, final JsonObject criteria) {
        final String resourceId = viewData.getString(KName.RESOURCE_ID);
        final String view = viewData.getString(KName.VIEW);
        /* Find user matrix */
        final JsonObject filters = this.toFilters(resourceId, view);
        filters.put("owner", userId);
        filters.put("ownerType", OwnerType.USER.name());
        /* SView projection */
        Sc.infoResource(ViewService.LOGGER, AuthMsg.VIEW_PROCESS, "save", filters.encode());
        return Ux.Jooq.on(SViewDao.class).<SView>fetchOneAsync(filters).compose(queried -> {
            final SView myView;
            if (Objects.isNull(queried)) {
                /*
                 * New View
                 */
                final JsonObject data = filters.copy().mergeIn(viewData);
                data.put(KName.KEY, UUID.randomUUID().toString());
                data.put(KName.ACTIVE, Boolean.TRUE);
                data.put("rows", new JsonObject().encode());
                myView = Ut.deserialize(data, SView.class);
                /*
                 * Creation
                 */
                myView.setCreatedAt(LocalDateTime.now());
                myView.setCreatedBy(userId);
            } else {
                /*
                 * Update Only
                 */
                myView = queried;
            }
            /*
             * Data Updating Part
             */
            if (Ut.notNil(projection)) {
                myView.setProjection(projection.encode());
            }
            if (Ut.notNil(criteria)) {
                myView.setCriteria(criteria.encode());
            }
            /* Auditor Information */
            myView.setUpdatedAt(LocalDateTime.now());
            myView.setUpdatedBy(userId);
            if (Objects.isNull(queried)) {
                return Ux.Jooq.on(SViewDao.class).insertAsync(myView);
            } else {
                return Ux.Jooq.on(SViewDao.class).updateAsync(myView);
            }
        }).compose(upsert -> {
            /*
             * Response Building
             */
            final JsonObject cached = new JsonObject();
            cached.put(Qr.KEY_PROJECTION, Ut.toJArray(upsert.getProjection()));
            cached.put(Qr.KEY_CRITERIA, Ut.toJObject(upsert.getCriteria()));
            return Ux.future(cached);
        });
    }

    @Override
    public Future<List<SView>> fetchMatrix(final JsonArray roleIds, final String resourceId, final String view) {
        /* Find user matrix */
        final JsonObject filters = this.toFilters(resourceId, view);
        filters.put("owner,i", roleIds);
        filters.put("ownerType", OwnerType.ROLE.name());
        return Ux.Jooq.on(SViewDao.class)
            .fetchAndAsync(new JsonObject().put("criteria", filters));
    }

    private JsonObject toFilters(final String resourceId, final String view) {
        final JsonObject filters = new JsonObject();
        filters.put(Strings.EMPTY, Boolean.TRUE);
        filters.put("resourceId", resourceId);
        filters.put("name", view);
        return filters;
    }
}
