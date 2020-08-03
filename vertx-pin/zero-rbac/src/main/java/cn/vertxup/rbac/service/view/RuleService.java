package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.daos.SViewDao;
import cn.vertxup.rbac.domain.tables.pojos.SPath;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeDefault;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.jq.UxJooq;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class RuleService implements RuleStub {
    @Override
    public Future<JsonArray> procAsync(final List<SPath> paths) {
        final List<SPath> filtered = paths.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return Ux.thenCombineT(filtered, RuleRobin::procRule).compose(Ux::fnJArray);
    }

    @Override
    public Future<JsonArray> fetchViews(final String ownerType, final String ownerId,
                                        final JsonArray keys, final String view) {
        final JsonObject condition = new JsonObject();
        condition.put("owner", ownerId);
        condition.put("ownerType", ownerType);
        condition.put("resourceId,i", keys);
        condition.put("name", view);
        return Ux.Jooq.on(SViewDao.class).fetchAndAsync(condition).compose(Ux::fnJArray)
                .compose(Ke.mounts("rows", "criteria"))
                .compose(result -> {
                    Ut.itJArray(result).forEach(json -> Ke.mountArray(json, "projection"));
                    return Ux.future(result);
                });
    }

    @Override
    public Future<JsonArray> saveViews(final String ownerType, final String ownerId,
                                       final JsonArray views, final String view) {
        final Set<String> keySet = Ut.mapString(views, KeField.RESOURCE_ID);
        /*
         * owner, ownerType, resourceId, name are unique
         * Because of here:
         *
         * 1. owner, ownerType are both the same
         * 2. view is default name: DEFAULT
         *
         * In this kind of situation, we could consider resourceId as unique key
         */
        final ConcurrentMap<String, JsonObject> newMap = Ut.elementMap(views, KeField.RESOURCE_ID);
        return this.fetchViews(ownerType, ownerId, Ut.toJArray(keySet), view).compose(original -> {
            final ConcurrentMap<String, JsonObject> oldMap = Ut.elementMap(original, KeField.RESOURCE_ID);
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
                    normalized.setKey(UUID.randomUUID().toString());
                    normalized.setActive(Boolean.TRUE);
                    normalized.setName(KeDefault.VIEW_DEFAULT);
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
                    .compose(Ux::fnJArray)
                    .compose(updated -> {
                        /*
                         * Stored data into updated queue
                         */
                        response.addAll(updated);
                        return Ux.future();
                    })
                    .compose(nil -> jooq.insertAsync(addQueue))
                    .compose(Ux::fnJArray)
                    .compose(Ke.mounts("rows", "criteria"))
                    .compose(result -> {
                        Ut.itJArray(result).forEach(json -> Ke.mountArray(json, "projection"));
                        return Ux.future(result);
                    })
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
            final JsonArray visitantArr = new JsonArray();
            Ut.itJArray(viewData)
                    .filter(viewItem -> viewItem.containsKey("visitant"))
                    .filter(viewItem -> viewItem.getBoolean("visitant"))
                    .forEach(viewItem -> {

                        // Resource Id
                        final String resourceId = viewItem.getString(KeField.RESOURCE_ID);
                        if (Ut.notNil(resourceId) && newMap.containsKey(resourceId)) {
                            final JsonObject input = newMap.get(resourceId);

                            // Request data that contains `visitantData`
                            if (input.containsKey("visitantData")) {

                                // visitantData to generate visitant
                                final JsonObject visitantData = input.getJsonObject("visitantData");
                                if (Ut.notNil(visitantData)) {
                                    visitantData.put("viewId", viewItem.getString(KeField.KEY));
                                    System.err.println(visitantData.encodePrettily());
                                }
                            }
                        }
                    });
            return Ux.future(viewData);
        });
    }
}
