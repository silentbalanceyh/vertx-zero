package io.vertx.tp.is;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.optic.business.ExIo;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractExPath implements ExIo {
    private static final String FS_DEFAULT = "io.vertx.tp.is.FsDefault";

    protected Future<JsonArray> componentRun(final JsonArray data, final BiFunction<Fs, JsonArray, Future<JsonArray>> runner) {
        final ConcurrentMap<Fs, JsonArray> componentMap = this.componentGroup(data);
        final List<Future<JsonArray>> futures = new ArrayList<>();
        componentMap.forEach((fs, dataEach) -> futures.add(runner.apply(fs, dataEach)));
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
    protected ConcurrentMap<Fs, JsonArray> componentGroup(final JsonArray data) {
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
