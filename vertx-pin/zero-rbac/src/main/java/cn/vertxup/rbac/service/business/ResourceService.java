package cn.vertxup.rbac.service.business;

import cn.vertxup.rbac.domain.tables.daos.SActionDao;
import cn.vertxup.rbac.domain.tables.daos.SResourceDao;
import cn.vertxup.rbac.domain.tables.pojos.SAction;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Optional;
import java.util.UUID;

public class ResourceService implements ResourceStub {

    @Override
    public Future<JsonObject> fetchResource(final String resourceId) {
        return Ux.Jooq.on(SResourceDao.class)
            .fetchByIdAsync(resourceId)
            .compose(Ux::futureJ)
            .compose(resource -> Ux.Jooq.on(SActionDao.class)
                .fetchOneAsync(KName.RESOURCE_ID, resourceId)
                .compose(Ux::futureJ)
                .compose(action -> Ux.future(resource.put("action", action))));
    }

    @Override
    public Future<JsonObject> createResource(final JsonObject params) {
        final SResource sResource = Ux.fromJson(params, SResource.class);

        return Ux.Jooq.on(SResourceDao.class)
            .insertAsync(sResource)
            .compose(Ux::futureJ)
            .compose(resource -> {
                // handle action node if present
                if (params.containsKey("action") && Ut.isNotNil(params.getJsonObject("action"))) {
                    final SAction sAction = Ux.fromJson(params.getJsonObject("action"), SAction.class);
                    // verify important fields
                    sAction.setKey(Optional.ofNullable(sAction.getKey()).orElse(UUID.randomUUID().toString()))
                        .setActive(Optional.ofNullable(sAction.getActive()).orElse(Boolean.TRUE))
                        .setResourceId(Optional.ofNullable(sAction.getResourceId()).orElse(resource.getString(KName.KEY)))
                        .setLevel(Optional.ofNullable(sAction.getLevel()).orElse(resource.getInteger("level")))
                        .setSigma(Optional.ofNullable(sAction.getSigma()).orElse(resource.getString(KName.SIGMA)))
                        .setLanguage(Optional.ofNullable(sAction.getLanguage()).orElse(resource.getString(KName.LANGUAGE)));
                    return Ux.Jooq.on(SActionDao.class)
                        .insertAsync(sAction)
                        .compose(Ux::futureJ)
                        .compose(action -> Ux.future(resource.put("action", action)));
                } else {
                    return Ux.future(resource);
                }
            });
    }

    @Override
    public Future<JsonObject> updateResource(final String resourceId, final JsonObject params) {
        final SResource sResource = Ux.fromJson(params, SResource.class);

        return Ux.Jooq.on(SResourceDao.class)
            .upsertAsync(resourceId, sResource)
            .compose(Ux::futureJ)
            .compose(resource -> {
                // handle action node if present
                if (params.containsKey("action") && Ut.isNotNil(params.getJsonObject("action"))) {
                    final SAction sAction = Ux.fromJson(params.getJsonObject("action"), SAction.class);
                    return Ux.Jooq.on(SActionDao.class)
                        .upsertAsync(new JsonObject().put(KName.RESOURCE_ID, resourceId), sAction)
                        .compose(Ux::futureJ)
                        .compose(action -> Ux.future(resource.put("action", action)));
                } else {
                    return Ux.future(resource);
                }
            });
    }

    @Override
    public Future<Boolean> deleteResource(final String resourceId) {
        return Ux.Jooq.on(SActionDao.class)
            .deleteByAsync(new JsonObject().put(KName.RESOURCE_ID, resourceId))
            .compose(result -> Ux.Jooq.on(SResourceDao.class)
                .deleteByIdAsync(resourceId));
    }
}
