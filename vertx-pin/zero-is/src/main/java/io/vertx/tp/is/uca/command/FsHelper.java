package io.vertx.tp.is.uca.command;

import cn.vertxup.integration.domain.tables.daos.IDirectoryDao;
import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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
public class FsHelper {
    private static final String FS_DEFAULT = "io.vertx.tp.is.uca.command.FsDefault";

    // ---------------------- Component Processing ----------------------

    public static Future<JsonObject> componentRun(final JsonObject data, final Function<Fs, Future<JsonObject>> fsRunner) {
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

    public static Future<JsonArray> componentRun(final JsonArray data, final BiFunction<Fs, JsonArray, Future<JsonArray>> fsRunner) {
        final ConcurrentMap<Fs, JsonArray> componentMap = componentGroup(data);
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
    private static ConcurrentMap<Fs, JsonArray> componentGroup(final JsonArray data) {
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

    // ---------------------- Directory Query ----------------------
    public static Future<List<IDirectory>> directoryQuery(final JsonArray data, final String storeField) {
        final String sigma = Ut.valueString(data, KName.SIGMA);
        final JsonArray names = Ut.valueJArray(data, storeField);
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.SIGMA, sigma);
        condition.put(KName.STORE_PATH + ",i", names);
        return Ux.Jooq.on(IDirectoryDao.class).fetchAsync(condition);
    }

    public static Future<JsonArray> directoryQuery(final JsonObject condition) {
        return Ux.Jooq.on(IDirectoryDao.class)
            .fetchJAsync(condition)
            .compose(Ut.ifJArray(KName.METADATA, KName.VISIT_GROUP, KName.VISIT_ROLE, KName.VISIT_MODE));
    }

    // ---------------------- Batch: Directory Compare ----------------------
    public static ConcurrentMap<ChangeFlag, JsonArray> directoryDiff(final JsonArray input, final List<IDirectory> directories) {
        /*
         *  IDirectory
         */
        final ConcurrentMap<String, IDirectory> directoryMap = Ut.elementMap(directories, IDirectory::getStorePath);
        final JsonArray queueAD = new JsonArray();
        final JsonArray queueUP = new JsonArray();

        Ut.itJArray(input).forEach(json -> {
            final String path = json.getString(KName.STORE_PATH);
            if (directoryMap.containsKey(path)) {
                // UPDATE Queue
                final JsonObject normalized = Ux.toJson(directoryMap.getOrDefault(path, null));
                queueUP.add(normalized);
            } else {
                // ADD Queue
                queueAD.add(json);
            }
        });
        return new ConcurrentHashMap<>() {
            {
                this.put(ChangeFlag.ADD, queueAD);
                this.put(ChangeFlag.UPDATE, queueUP);
            }
        };
    }
}
