package io.vertx.tp.is.refine;

import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.uca.command.Fs;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IsFs {
    private static final String FS_DEFAULT = "io.vertx.tp.is.uca.command.FsDefault";

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
        return Ux.thenCombineArray(futures);
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

        final ConcurrentMap<Fs, JsonArray> groupComponent = new ConcurrentHashMap<>();
        /*
         * Default component
         */
        if (!queueDft.isEmpty()) {
            final Fs fs = Ut.singleton(FS_DEFAULT);
            groupComponent.put(fs, queueDft);
        }
        final ConcurrentMap<String, JsonArray> groupIntegrated = Ut.elementGroup(queueIntegrated, KName.Component.RUN_COMPONENT);
        groupIntegrated.forEach((componentCls, dataArray) -> {
            if (!dataArray.isEmpty()) {
                final Class<?> clazz = Ut.clazz(componentCls, null);
                if (Objects.nonNull(clazz) && Ut.isImplement(clazz, Fs.class)) {
                    final Fs fs = Ut.singleton(clazz);
                    groupComponent.put(fs, dataArray);
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
            return Ux.thenCombineArray(futures);
        });
    }

    private static Future<JsonArray> mkdir(final JsonArray queueUp, final List<IDirectory> storeList) {
        if (Ut.isNil(queueUp)) {
            return Ux.futureA();
        }

        final ConcurrentMap<String, IDirectory> storeMap = Ut.elementMap(storeList, IDirectory::getStorePath);
        Ut.itJArray(queueUp).forEach(json -> {
            final String storePath = json.getString(KName.STORE_PATH);
            if (Ut.notNil(storePath)) {
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
            Ut.itJArray(inserted).forEach(json -> Ut.ifJCopy(json, KName.KEY, KName.DIRECTORY_ID));
            return Ux.future(inserted);
        });
    }
}
