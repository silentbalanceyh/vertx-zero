package cn.vertxup.rbac.service.accredit;

import cn.vertxup.rbac.domain.tables.daos.SActionDao;
import cn.vertxup.rbac.domain.tables.daos.SResourceDao;
import cn.vertxup.rbac.domain.tables.pojos.SAction;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

public class ResourceService implements ResourceStub {

    @Override
    public Future<JsonObject> fetchResource(String resourceId) {
        return Ux.Jooq.on(SResourceDao.class)
                .findByIdAsync(resourceId)
                .compose(Ux::fnJObject)
                .compose(resource -> Ux.Jooq.on(SActionDao.class)
                        .fetchOneAsync(KeField.RESOURCE_ID, resource)
                            .compose(Ux::fnJObject)
                            .compose(action -> Ux.future(resource.put("action", action))));
    }

    @Override
    public Future<JsonObject> createResource(JsonObject params) {
        final SResource sResource = Ux.fromJson(params, SResource.class);

        return Ux.Jooq.on(SResourceDao.class)
                .insertAsync(sResource)
                .compose(Ux::fnJObject)
                .compose(resource -> {
                    // handle action node if present
                    if (params.containsKey("action") && Ut.notNil(params.getJsonObject("action"))) {
                        final SAction sAction = Ux.fromJson(params.getJsonObject("action"), SAction.class);
                        // TODO get all fields ready
                        sAction.setResourceId(resource.getString(KeField.KEY));

                        return Ux.Jooq.on(SActionDao.class)
                                .insertAsync(sAction)
                                .compose(Ux::fnJObject)
                                .compose(action -> Ux.future(resource.put("action", action)));
                    } else {
                        return Ux.future(resource);
                    }
                });
    }

    @Override
    public Future<JsonObject> updateResource(String resourceId, JsonObject params) {
        final SResource sResource = Ux.fromJson(params, SResource.class);

        return Ux.Jooq.on(SResourceDao.class)
                .upsertAsync(resourceId, sResource)
                .compose(Ux::fnJObject)
                .compose(resource -> {
                    // handle action node if present
                    if (params.containsKey("action") && Ut.notNil(params.getJsonObject("action"))) {
                        final SAction sAction = Ux.fromJson(params.getJsonObject("action"), SAction.class);
                        return Ux.Jooq.on(SActionDao.class)
                                .upsertAsync(sAction.getKey(), sAction)
                                .compose(Ux::fnJObject)
                                .compose(action -> Ux.future(resource.put("action", action)));
                    } else {
                        return Ux.future(resource);
                    }
                });
    }

    @Override
    public Future<Boolean> deleteResource(String resourceId) {
        return Ux.Jooq.on(SActionDao.class)
                .deleteAsync(new JsonObject().put(KeField.RESOURCE_ID, resourceId))
                .compose(result -> Ux.Jooq.on(SResourceDao.class)
                        .deleteByIdAsync(resourceId));
    }
}
