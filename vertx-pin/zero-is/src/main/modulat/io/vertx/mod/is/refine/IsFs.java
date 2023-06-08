package io.vertx.mod.is.refine;

import cn.vertxup.integration.domain.tables.daos.IDirectoryDao;
import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.is.uca.command.Fs;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IsFs {
    private static final String FS_DEFAULT = "io.vertx.mod.command.uca.is.FsDefault";

    static Future<JsonObject> run(final JsonObject data, final Function<Fs, Future<JsonObject>> fsRunner) {
        final String componentCls = data.getString(KName.Component.RUN_COMPONENT);
        if (Ut.isNil(componentCls)) {
            return Ux.future(data);
        }
        final Class<?> clazz = Ut.clazz(componentCls, null);
        if (Objects.nonNull(clazz) && Ut.isImplement(clazz, Fs.class)) {
            final Fs fs = Ut.singleton(clazz);
            return fsRunner.apply(fs);
        } else {
            return Ux.future(data);
        }
    }


    static Future<JsonArray> run(final JsonArray data, final BiFunction<Fs, JsonArray, Future<JsonArray>> fsRunner) {
        final ConcurrentMap<Fs, JsonArray> componentMap = fsGroup(data);
        final List<Future<JsonArray>> futures = new ArrayList<>();
        componentMap.forEach((fs, dataEach) -> futures.add(fsRunner.apply(fs, dataEach.copy())));
        return Fn.compressA(futures);
    }

    /*
     * The input map
     * -- storePath = directoryId
     *
     * Here `storePath` is file or directory storePath
     */
    static Future<ConcurrentMap<Fs, Set<String>>> group(final ConcurrentMap<String, String> fileMap) {
        // directoryId = Set<String> ( storePath )
        final ConcurrentMap<String, Set<String>> directoryMap = Ut.inverseSet(fileMap);

        // Fetch directories by Set<String> ( keys )
        final JsonObject criteria = new JsonObject();
        criteria.put(KName.KEY + ",i", Ut.toJArray(directoryMap.keySet()));
        return IsDir.query(criteria).compose(directories -> {

            // Grouped List<IDirectory> by `runComponent`, transfer to runComponent = Set<String> ( keys )
            final ConcurrentMap<String, List<String>> grouped =
                Ut.elementGroup(directories, IDirectory::getRunComponent, IDirectory::getKey);

            /*
             * Connect to Map
             * 1. runComponent = Set<String> ( keys )
             * 2. key = Set<String> ( storePath )
             */
            final ConcurrentMap<Fs, List<String>> fsGroup = group(grouped, List::isEmpty);
            final ConcurrentMap<Fs, Set<String>> resultMap = new ConcurrentHashMap<>();
            fsGroup.forEach((fs, keyList) -> {
                final Set<String> storeSet = new HashSet<>();
                keyList.forEach(key -> {
                    final Set<String> subSet = directoryMap.get(key);
                    if (Objects.nonNull(subSet) && !subSet.isEmpty()) {
                        storeSet.addAll(subSet);
                    }
                });
                if (!storeSet.isEmpty()) {
                    resultMap.put(fs, storeSet);
                }
            });
            return Ux.future(resultMap);
        });
    }

    /*
     * data structure of each json
     * {
     *      "storeRoot": "xxxx",
     *      "storePath": "Actual Path",
     *      "integrationId": "If integrated by directory"
     * }
     */
    private static ConcurrentMap<Fs, JsonArray> fsGroup(final JsonArray data) {
        /*
         * Group data
         * 1. All the integrationId = null, extract `storeRoot` from data.
         * 2. The left records contains integrationId, grouped by `integrationId`.
         */
        final JsonArray queueDft = new JsonArray();
        final JsonArray queueIntegrated = new JsonArray();
        Ut.itJArray(data).forEach(json -> {
            if (json.containsKey(KName.Component.RUN_COMPONENT)) {
                queueIntegrated.add(json);
            } else {
                queueDft.add(json);
            }
        });

        final ConcurrentMap<String, JsonArray> groupIntegrated = Ut.elementGroup(queueIntegrated, KName.Component.RUN_COMPONENT);
        final ConcurrentMap<Fs, JsonArray> groupComponent = group(groupIntegrated, JsonArray::isEmpty);

        /*
         * Default component Compact
         */
        if (!queueDft.isEmpty()) {
            final Fs fs = Ut.singleton(FS_DEFAULT);
            final JsonArray dataRef = groupComponent.getOrDefault(fs, new JsonArray());
            dataRef.addAll(queueDft);
            groupComponent.put(fs, dataRef);
        }
        return groupComponent;
    }

    static Future<Fs> component(final String directoryId) {
        final Fs fsDft = Ut.singleton(FS_DEFAULT);
        if (Objects.isNull(directoryId)) {
            return Ux.future(fsDft);
        }
        return Ux.Jooq.on(IDirectoryDao.class).<IDirectory>fetchByIdAsync(directoryId).compose(directory -> {
            if (Objects.isNull(directory)) {
                return Ux.future(fsDft);
            }
            final String componentCls = directory.getRunComponent();
            if (Ut.isNil(componentCls)) {
                return Ux.future(fsDft);
            }
            final Class<?> clazz = Ut.clazz(componentCls, null);
            if (Objects.nonNull(clazz) && Ut.isImplement(clazz, Fs.class)) {
                return Ux.future(Ut.singleton(clazz));
            } else {
                return Ux.future(fsDft);
            }
        });
    }

    static ConcurrentMap<Fs, Set<String>> combine(final ConcurrentMap<Fs, Set<String>> directoryMap,
                                                  final ConcurrentMap<Fs, Set<String>> fileMap) {
        /*
         * Combine two map
         */
        final ConcurrentMap<Fs, Set<String>> combine = new ConcurrentHashMap<>(directoryMap);
        fileMap.forEach((fs, set) -> {
            final Set<String> valueSet;
            if (combine.containsKey(fs)) {
                valueSet = combine.getOrDefault(fs, new HashSet<>());
            } else {
                valueSet = new HashSet<>();
            }
            valueSet.addAll(set);
            combine.put(fs, valueSet);
        });
        return combine;
    }

    static <V> ConcurrentMap<Fs, V> group(final ConcurrentMap<String, V> map, final Predicate<V> fnKo) {
        final ConcurrentMap<Fs, V> groupComponent = new ConcurrentHashMap<>();
        map.forEach((componentCls, value) -> {
            if (!fnKo.test(value)) {
                final Class<?> clazz = Ut.clazz(componentCls, null);
                if (Objects.nonNull(clazz) && Ut.isImplement(clazz, Fs.class)) {
                    final Fs fs = Ut.singleton(clazz);
                    groupComponent.put(fs, value);
                }
            }
        });
        return groupComponent;
    }

    static Future<JsonArray> document(final JsonArray data, final JsonObject config) {
        return IsDir.query(data, KName.STORE_PATH, false).compose(queried -> {
            final ConcurrentMap<ChangeFlag, JsonArray> compared = IsDir.diff(data, queried);
            /*
             * 1. ADD queue ( attach `directoryId` in processed )
             * 2. UPDATE queue ( attach `directoryId` )
             * 3. NONE queue ( existing )
             */
            final List<Future<JsonArray>> futures = new ArrayList<>();
            futures.add(mkdir(compared.getOrDefault(ChangeFlag.ADD, new JsonArray()), config));
            futures.add(mkdir(compared.getOrDefault(ChangeFlag.UPDATE, new JsonArray()), queried));
            futures.add(Ux.future(compared.getOrDefault(ChangeFlag.NONE, new JsonArray())));
            return Fn.compressA(futures);
        });
    }

    private static Future<JsonArray> mkdir(final JsonArray queueUp, final List<IDirectory> storeList) {
        if (Ut.isNil(queueUp)) {
            return Ux.futureA();
        }

        final ConcurrentMap<String, IDirectory> storeMap = Ut.elementMap(storeList, IDirectory::getStorePath);
        Ut.itJArray(queueUp).forEach(json -> {
            final String storePath = json.getString(KName.STORE_PATH);
            if (Ut.isNotNil(storePath)) {
                final IDirectory store = storeMap.get(storePath);
                if (Objects.nonNull(store)) {
                    // directoryId
                    json.put(KName.DIRECTORY_ID, store.getKey());
                    /*
                     * visit information
                     * -- visit ( owner )
                     * -- visitMode
                     * -- visitRole
                     * -- visitGroup
                     */
                    json.put(KName.VISIT_MODE, Ut.toJArray(store.getVisitMode()));
                }
            }
        });
        return Ux.future(queueUp);
    }

    private static Future<JsonArray> mkdir(final JsonArray queueAd, final JsonObject config) {
        if (Ut.isNil(queueAd)) {
            return Ux.futureA();
        }
        // Injection `runComponent` to replace the default `runComponent`
        if (config.containsKey(KName.Component.RUN_COMPONENT)) {
            Ut.itJArray(queueAd).forEach(json -> json.put(KName.Component.RUN_COMPONENT, config.getString(KName.Component.RUN_COMPONENT)));
        }
        // Group queueAd, Re-Calculate `directoryId` here.
        return run(queueAd, (fs, dataGroup) -> fs.synchronize(dataGroup, config)).compose(inserted -> {
            /* storePath = key */
            Ut.itJArray(inserted).forEach(json -> Ut.valueCopy(json, KName.KEY, KName.DIRECTORY_ID));
            return Ux.future(inserted);
        });
    }
}
