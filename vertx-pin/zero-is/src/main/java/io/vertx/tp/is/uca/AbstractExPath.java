package io.vertx.tp.is.uca;

import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.business.ExIo;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractExPath implements ExIo {
    private static final String FS_DEFAULT = "io.vertx.tp.is.uca.FsDefault";

    protected Future<JsonArray> compress(final JsonArray data) {
        return FsKit.queryDirectory(data, KName.STORE_PATH).compose(queried -> {
            final Set<String> storePath = queried.stream()
                .filter(Objects::nonNull)
                .map(IDirectory::getStorePath).collect(Collectors.toSet());
            final JsonArray compressed = new JsonArray();
            Ut.itJArray(data).forEach(json -> {
                final String path = json.getString(KName.STORE_PATH);
                if (!storePath.contains(path)) {
                    compressed.add(json);
                }
            });
            return Ux.future(compressed);
        });
    }

    protected Future<JsonArray> componentRun(final JsonArray data, final BiFunction<Fs, JsonArray, Future<JsonArray>> fsRunner) {
        final ConcurrentMap<Fs, JsonArray> componentMap = this.componentGroup(data);
        final List<Future<JsonArray>> futures = new ArrayList<>();
        componentMap.forEach((fs, dataEach) -> futures.add(fsRunner.apply(fs, dataEach.copy())));
        return Ux.thenCombineArray(futures);
    }

    // ---------------------- mkdir -----------------------

    protected Future<JsonArray> commandMkdir(final JsonArray queueAd, final JsonObject config) {
        if (Ut.isNil(queueAd)) {
            return Ux.futureA();
        }
        // Group queueAd, Re-Calculate `directoryId` here.
        return this.componentRun(queueAd, (fs, dataGroup) -> fs.mkdir(dataGroup, config)).compose(inserted -> {
            /*
             * storePath = key
             */
            Ut.itJArray(inserted).forEach(json -> Ut.ifJCopy(json, KName.KEY, KName.DIRECTORY_ID));
            return Ux.future(inserted);
        });
    }

    protected Future<JsonArray> commandMkdir(final JsonArray queueUp, final List<IDirectory> storeList) {
        if (Ut.isNil(queueUp)) {
            return Ux.futureA();
        }

        final ConcurrentMap<String, IDirectory> storeMap = Ut.elementMap(storeList, IDirectory::getStorePath);
        Ut.itJArray(queueUp).forEach(json -> {
            final String storePath = json.getString(KName.STORE_PATH);
            if (Ut.notNil(storePath)) {
                final IDirectory store = storeMap.get(storePath);
                if (Objects.nonNull(store)) {
                    json.put(KName.DIRECTORY_ID, store.getKey());
                }
            }
        });
        return Ux.future(queueUp);
    }

    // ---------------------- shared ----------------------
    /*
     * data structure of each json
     * {
     *      "storeRoot": "xxxx",
     *      "storePath": "Actual Path",
     *      "integrationId": "If integrated by directory"
     * }
     */
    private ConcurrentMap<Fs, JsonArray> componentGroup(final JsonArray data) {
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
}
