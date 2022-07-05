package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.daos.SViewDao;
import cn.vertxup.rbac.domain.tables.pojos.SPath;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KValue;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RuleService implements RuleStub {
    @Inject
    private transient VisitStub visitStub;

    @Override
    public Future<JsonArray> procAsync(final List<SPath> paths) {
        final List<SPath> filtered = paths.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return Fn.arrangeT(filtered, RuleRobin::procRule).compose(Ux::futureA);
    }

    @Override
    public Future<JsonArray> fetchViews(final String ownerType, final String ownerId,
                                        final JsonArray keys, final String view) {
        final JsonObject condition = new JsonObject();
        condition.put("owner", ownerId);
        condition.put("ownerType", ownerType);
        condition.put("resourceId,i", keys);
        condition.put(KName.NAME, view);
        return Ux.Jooq.on(SViewDao.class).fetchAndAsync(condition).compose(Ux::futureA)
            .compose(Ut.ifJArray("rows", Qr.KEY_CRITERIA, Qr.KEY_PROJECTION));
    }

    @Override
    public Future<JsonArray> saveViews(final String ownerType, final String ownerId,
                                       final JsonArray views, final String view) {
        final Set<String> keySet = Ut.valueSetString(views, KName.RESOURCE_ID);
        /*
         * owner, ownerType, resourceId, name are unique
         * Because of here:
         *
         * 1. owner, ownerType are both the same
         * 2. view is default name: DEFAULT
         *
         * In this kind of situation, we could consider resourceId as unique key
         */
        final ConcurrentMap<String, JsonObject> newMap = Ut.elementMap(views, KName.RESOURCE_ID);
        return this.fetchViews(ownerType, ownerId, Ut.toJArray(keySet), view).compose(original -> {
            final ConcurrentMap<String, JsonObject> oldMap = Ut.elementMap(original, KName.RESOURCE_ID);
            /*
             * Calculate new data here for processing
             *
             * ADD, DELETE, UPDATE
             *
             * Here mustn't be `DELETE` because in front tier there is no user interface
             * to remove view on owner entity.
             */
            final List<SView> addQueue = new ArrayList<>();
            final List<SView> upQueue = new ArrayList<>();
            newMap.keySet().forEach(resourceId -> {
                if (oldMap.containsKey(resourceId)) {
                    /*
                     * Old and new both contains `resourceId`,
                     * It means UPDATE happen
                     */
                    final JsonObject previous = oldMap.get(resourceId);
                    previous.mergeIn(newMap.get(resourceId));
                    final SView normalized = Ut.deserialize(previous.copy(), SView.class);
                    upQueue.add(normalized);
                } else {
                    /*
                     * Old not, but new contains
                     * ADD happen
                     */
                    final JsonObject added = newMap.get(resourceId);
                    final SView normalized = Ut.deserialize(added.copy(), SView.class);
                    /*
                     * Spec fields that should added default
                     */
                    normalized.setActive(Boolean.TRUE);
                    normalized.setName(KValue.View.VIEW_DEFAULT);
                    addQueue.add(normalized);
                }
            });
            /*
             * Update first and then add new into our queue
             * and database for sync here
             */
            final UxJooq jooq = Ux.Jooq.on(SViewDao.class);
            final JsonArray response = new JsonArray();
            return jooq.updateAsync(upQueue)
                .compose(Ux::futureA)
                .compose(updated -> {
                    /*
                     * Stored data into updated queue
                     */
                    response.addAll(updated);
                    return Ux.future();
                })
                .compose(nil -> jooq.insertAsync(addQueue))
                .compose(Ux::futureA)
                .compose(Ut.ifJArray("rows", Qr.KEY_CRITERIA, Qr.KEY_PROJECTION))
                //                .compose(Ke.mounts("rows", "criteria"))
                //                .compose(result -> {
                //                    Ut.itJArray(result).forEach(json -> Ke.mountArray(json, "projection"));
                //                    return Ux.future(result);
                //                })
                .compose(inserted -> {
                    /*
                     * Stored data into inserted queue here
                     */
                    response.addAll(inserted);
                    return Ux.future(response);
                });
        }).compose(viewData -> {
            /*
             * viewData -> JsonArray to store all views
             * newMap -> ( resourceId = JsonObject )
             * Here JsonObject may contains visitantData when viewData contain ( visitant = true )
             */
            return this.saveVisitants(viewData, newMap);
        });
    }

    /*
     * Save `visitants` in batch
     */
    private Future<JsonArray> saveVisitants(final JsonArray views, final ConcurrentMap<String, JsonObject> resourceMap) {
        final List<Future<JsonObject>> futures = new ArrayList<>();
        Ut.itJArray(views).filter(view -> view.getBoolean("visitant", Boolean.FALSE)).forEach(view -> {
            /*
             * view is S_VIEW record
             * resourceMap is request data here
             */
            final String resourceId = view.getString(KName.RESOURCE_ID);
            if (Ut.notNil(resourceId) && resourceMap.containsKey(resourceId)) {
                final JsonObject requestData = resourceMap.get(resourceId);
                if (requestData.containsKey("visitantData")) {
                    final JsonObject visitantData = requestData.getJsonObject("visitantData");
                    if (Ut.notNil(visitantData)) {
                        futures.add(this.visitStub.saveAsync(visitantData.copy(), view)
                            /*
                             * Processed for views
                             */
                            .compose(processed -> Ux.future(view.put("visitantData", processed)))
                        );
                    }
                }
            }
        });
        return Fn.arrange(futures).compose(nil -> Ux.future(views));
    }
}
