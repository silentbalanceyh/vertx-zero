package io.vertx.mod.ke.refine;

import io.horizon.eon.VString;
import io.horizon.spi.environment.UnityApp;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.database.DataPool;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.jooq.Configuration;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.vertx.mod.ke.refine.Ke.LOG;

class KeTool {

    private static String DATABASE;

    static String getCatalog() {
        if (Ut.isNil(DATABASE)) {
            final JsonObject config = ZeroStore.option(YmlCore.jooq.__KEY);
            DATABASE = Ut.visitString(config, "provider", "catalog");
        }
        return DATABASE;
    }

    static Configuration getConfiguration() {
        final Database database = Database.getCurrent();
        final DataPool pool = DataPool.create(database);
        return pool.getExecutor().configuration();
    }

    /*
     * 针对每一个App做的执行操作，内置环境借用通道会直接提取App信息
     */
    static <T, R> Future<R> mapApp(final Function<JsonObject, Future<T>> executor, final Function<Set<T>, Future<R>> combiner) {
        return Ux.channelS(UnityApp.class, (appKit) -> {
            final ConcurrentMap<String, JsonObject> appAll = appKit.connect();
            final ConcurrentMap<String, Future<T>> appFuture = new ConcurrentHashMap<>();
            appAll.forEach((appId, appJ) -> appFuture.put(appId, executor.apply(appJ)));
            return Fn.combineM(appFuture).compose(map -> {
                final Set<T> resultT = new HashSet<>(map.values());
                return combiner.apply(resultT);
            });
        });
    }

    static Future<JsonObject> map(final JsonObject data, final String field,
                                  final ConcurrentMap<String, JsonObject> attachmentMap,
                                  final BiFunction<JsonObject, JsonArray, Future<JsonArray>> fileFn) {
        /*
         * Here call add only
         */
        final String key = data.getString(field);
        Objects.requireNonNull(key);
        final ConcurrentMap<String, Future<JsonArray>> futures = new ConcurrentHashMap<>();
        attachmentMap.forEach((fieldF, condition) -> {
            /*
             * Put `key` of data into `modelKey`
             */
            final JsonObject criteria = condition.copy();
            if (Ut.isNotNil(criteria)) {
                criteria.put(VString.EMPTY, Boolean.TRUE);
                criteria.put(KName.MODEL_KEY, key);
                /*
                 * JsonArray normalize
                 */
                final JsonArray dataArray = Ut.valueJArray(data, fieldF);
                Ut.itJArray(dataArray).forEach(json -> json.put(KName.MODEL_KEY, key));
                futures.put(fieldF, fileFn.apply(criteria, dataArray));
            } else {
                /*
                 * Log
                 */
                LOG.Turnel.warn(KeTool.class, "Criteria must be not empty");
            }
        });
        return Fn.combineM(futures).compose(mapData -> {
            mapData.forEach(data::put);
            return Ux.future(data);
        });
    }

    static void banner(final String module) {
        System.out.println("-------------------------------------------------------------");
        System.out.println("|                                                           |");
        System.out.println("|     Zero Extension:  " + module);
        System.out.println("|                                                           |");
        System.out.println("-------------------------------------------------------------");
    }
}
