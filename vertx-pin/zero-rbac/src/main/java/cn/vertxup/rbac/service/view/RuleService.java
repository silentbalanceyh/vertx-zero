package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.daos.SResourceDao;
import cn.vertxup.rbac.domain.tables.daos.SViewDao;
import cn.vertxup.rbac.domain.tables.pojos.SPacket;
import cn.vertxup.rbac.domain.tables.pojos.SPath;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.aeon.specification.secure.HValve;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScOwner;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.tp.rbac.ruler.AdmitValve;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KValue;
import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RuleService implements RuleStub {
    private static final Cc<String, HValve> CC_VALVE = Cc.openThread();
    @Inject
    private transient VisitStub visitStub;

    @Override
    public Future<JsonArray> regionAsync(final List<SPath> paths) {
        /*
         * Major Path Configuration
         * 1. Not null and `runComponent` is not null
         * 2. `parentId` is null
         * 3. Sort By `uiSort`
         */
        final List<SPath> filtered = paths.stream()
            .filter(Objects::nonNull)
            .filter(item -> Ut.isNil(item.getParentId()))
            .sorted(Comparator.comparing(SPath::getUiSort))
            .collect(Collectors.toList());

        /*
         * Children Grouped
         */
        final ConcurrentMap<String, List<SPath>> grouped =
            Ut.elementGroup(paths.stream()
                .filter(Objects::nonNull)
                .filter(item -> Ut.notNil(item.getParentId()))
                .collect(Collectors.toList()), SPath::getParentId);
        return Fn.combineT(filtered, input -> Sc.cacheAdmit(input, path -> {
            /*
             * Extract `runComponent` to build `HValve` and then run it based on configured
             * Information here.
             */
            final Class<?> clazz = Ut.clazz(path.getRunComponent(), AdmitValve.class);
            if (Objects.isNull(clazz)) {
                return Ux.future();
            }
            final String cacheKey = path.getSigma() + Strings.SLASH + path.getCode();
            final HValve value = CC_VALVE.pick(() -> Ut.instance(clazz), cacheKey);
            final JsonObject pathJ = Ux.toJson(path);
            /*
             * JsonObject Configuration for SPath here
             */
            Fn.ifJObject(pathJ,
                // UI Configuration
                KName.UI_CONFIG,
                KName.UI_CONDITION,
                KName.UI_SURFACE,
                // DM Configuration
                KName.DM_CONDITION,
                KName.DM_CONFIG,
                // metadata / mapping
                KName.METADATA,
                KName.MAPPING
            );
            /*
             * Build map based on `code` for Area usage
             * `children` of pathJ
             */
            if (grouped.containsKey(path.getKey())) {
                final JsonArray children = Ux.toJson(grouped.getOrDefault(path.getKey(), new ArrayList<>()));
                pathJ.put(KName.CHILDREN, children);
            }
            return value.configure(pathJ);
        })).compose(Ux::futureA);
    }

    /*
     * 此处已经在上层提取了相关规则集 SPacket，最终构造数据格式
     */
    @Override
    public Future<JsonObject> regionAsync(final List<SPacket> packets, final ScOwner owner) {
        // 根据 resource codes 读取所有要操作的资源记录
        final Set<String> restCodes = packets.stream()
            .filter(Objects::nonNull)
            .map(SPacket::getResource)
            .collect(Collectors.toSet());

        final String sigma = Ut.valueString(packets, SPacket::getSigma);

        // 提取资源
        final JsonObject condition = Ux.whereAnd()
            .put(KName.SIGMA, sigma)
            .put(KName.CODE + ",i", Ut.toJArray(restCodes));
        return Ux.Jooq.on(SResourceDao.class).<SResource>fetchAsync(condition)
            .compose(resources -> {
                // 根据资源记录读取所需视图集
                final ConcurrentMap<String, SResource> resourceMap =
                    Ut.elementMap(resources, SResource::getCode);

                // resource -> json
                final ConcurrentMap<String, Future<JsonObject>> futures =
                    new ConcurrentHashMap<>();
                packets.forEach(packet -> {
                    // HEyelet Capture
                    final SResource resource = resourceMap.getOrDefault(packet.getResource(), null);
                    if (Objects.nonNull(resource)) {
                        futures.put(resource.getCode(), RuleKit.regionJ(packet, resource, owner));
                    }
                });
                return Fn.combineM(futures).compose(map -> Ux.future(Ut.toJObject(map)));
            });
    }

    @Override
    public Future<JsonArray> fetchViews(final String ownerType, final String ownerId,
                                        final JsonArray keys, final String view) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.OWNER, ownerId);
        condition.put(KName.OWNER_TYPE, ownerType);
        condition.put("resourceId,i", keys);
        condition.put(KName.NAME, view);
        return Ux.Jooq.on(SViewDao.class).fetchAndAsync(condition).compose(Ux::futureA)
            .compose(Fn.ifJArray("rows", Qr.KEY_CRITERIA, Qr.KEY_PROJECTION));
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
                .compose(Fn.ifJArray("rows", Qr.KEY_CRITERIA, Qr.KEY_PROJECTION))
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
        return Fn.combineA(futures).compose(nil -> Ux.future(views));
    }
}
