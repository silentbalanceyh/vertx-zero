package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.daos.SViewDao;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.cv.em.OwnerType;
import io.vertx.tp.rbac.permission.ScHabitus;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.eon.Strings;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.UUID;

public class ViewService implements ViewStub {

    private static final Annal LOGGER = Annal.get(ViewService.class);

    @Override
    public Future<SView> fetchMatrix(final String userId, final String resourceId, final String view) {
        /* Find user matrix */
        final JsonObject filters = toFilters(resourceId, view);
        filters.put("owner", userId);
        filters.put("ownerType", OwnerType.USER.name());
        Sc.infoResource(LOGGER, AuthMsg.VIEW_PROCESS, "fetch", filters.encode());
        return Ux.Jooq.on(SViewDao.class)
                .fetchOneAsync(new JsonObject().put("criteria", filters));
    }

    @Override
    public Future<SView> saveMatrix(final String userId, final String resourceId,
                                    final String view, final JsonArray projection) {
        /* Find user matrix */
        final JsonObject filters = toFilters(resourceId, view);
        filters.put("owner", userId);
        filters.put("ownerType", OwnerType.USER.name());
        /* SView projection */
        Sc.infoResource(LOGGER, AuthMsg.VIEW_PROCESS, "save", filters.encode());
        final SView myView = toView(filters, projection);
        return Ux.Jooq.on(SViewDao.class)
                .upsertAsync(filters, myView);
    }

    @Override
    public Future<List<SView>> fetchMatrix(final JsonArray roleIds, final String resourceId, final String view) {
        /* Find user matrix */
        final JsonObject filters = toFilters(resourceId, view);
        filters.put("owner,i", roleIds);
        filters.put("ownerType", OwnerType.ROLE.name());
        return Ux.Jooq.on(SViewDao.class)
                .fetchAndAsync(new JsonObject().put("criteria", filters));
    }

    @Override
    public Future<JsonObject> updateByType(String ownerType, String key, JsonObject data, String habit) {
        final SView view = Ux.fromJson(mountIn(data.put("ownerType", ownerType.toUpperCase())), SView.class);
        return this.deleteById(key)
                .compose(result -> Ux.Jooq.on(SViewDao.class)
                        .insertAsync(view)
                        .compose(Ux::fnJObject)
                        .compose(item -> {
                            // clear current user's habit
                            ScHabitus.initialize(habit).clear();
                            return Ux.future(this.mountOut(item));
                        }));
    }

    @Override
    public Future<Boolean> deleteById(String key) {
        return Ux.Jooq.on(SViewDao.class).deleteByIdAsync(key);
    }

    private JsonObject toFilters(final String resourceId, final String view) {
        final JsonObject filters = new JsonObject();
        filters.put(Strings.EMPTY, Boolean.TRUE);
        filters.put("resourceId", resourceId);
        filters.put("name", view);
        return filters;
    }

    private SView toView(final JsonObject filters, final JsonArray projection) {
        final JsonObject data = filters.copy()
                .put(Inquiry.KEY_PROJECTION, projection.encode());
        data.put(KeField.KEY, UUID.randomUUID().toString());
        data.put(KeField.ACTIVE, Boolean.TRUE);
        data.put("rows", new JsonObject().encode());
        data.put(Inquiry.KEY_CRITERIA, new JsonObject().encode());
        return Ut.deserialize(data, SView.class);
    }

    private JsonObject mountIn(final JsonObject data) {
        Ke.mountString(data, KeField.Rbac.PROJECTION);
        Ke.mountString(data, KeField.Rbac.CRITERIA);
        Ke.mountString(data, KeField.Rbac.ROWS);
        Ke.mountString(data, KeField.METADATA);
        return data;
    }
    private JsonObject mountOut(final JsonObject data) {
        Ke.mountArray(data,  KeField.Rbac.PROJECTION);
        Ke.mount(data, KeField.Rbac.CRITERIA);
        Ke.mount(data, KeField.Rbac.ROWS);
        Ke.mount(data, KeField.METADATA);
        return data;
    }
}
