package io.vertx.tp.ambient.uca.dict;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.refine.At;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.GlossaryType;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.UxPool;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class DpmTool {

    static final ConcurrentMap<GlossaryType, Dpm> POOL_DPM = new ConcurrentHashMap<GlossaryType, Dpm>() {
        {
            this.put(GlossaryType.ASSIST, Ut.instance(DpmAssist.class));
            this.put(GlossaryType.CATEGORY, Ut.instance(DpmCategory.class));
            this.put(GlossaryType.TABULAR, Ut.instance(DpmTabular.class));
        }
    };

    /**
     * Build condition for `X_CATEGORY, X_TABULAR` etc.
     *
     * @param params  {@link MultiMap} The parameters map that came from vert.x
     * @param typeSet {@link Set<String>} The definition of dict source.
     *
     * @return {@link JsonObject} Return to json data with criteria format
     */
    static JsonObject condition(final MultiMap params, final Set<String> typeSet) {
        /* Result */
        final JsonObject condition = new JsonObject();
        /* Sigma for each application */
        final String sigma = params.get(KName.SIGMA);
        condition.put(KName.SIGMA, sigma);

        /* Types */
        if (!typeSet.isEmpty()) {
            condition.put(KName.TYPE + ",i", Ut.toJArray(typeSet));
            condition.put(Strings.EMPTY, Boolean.TRUE);
        }
        return condition;
    }

    static Future<JsonArray> cachedBy(final String key, final Supplier<Future<JsonArray>> executor) {
        final UxPool pool = Ux.Pool.on(Constants.Pool.DIRECTORY);
        return pool.<String, JsonArray>get(key).compose(queried -> {
            if (Objects.isNull(queried)) {
                return executor.get()
                        // 15 min
                        .compose(actual -> pool.put(key, actual, 900))
                        .compose(Kv::value);
            } else {
                At.infoFlow(DpmTool.class, " [ PT ] Cached Hit = {0}", key);
                return Ux.future(queried);
            }
        });
    }

    static Future<ConcurrentMap<String, JsonArray>> cachedDict(
            final Set<String> typeSet,
            final Function<Set<String>, Future<ConcurrentMap<String, JsonArray>>> executor) {
        final UxPool pool = Ux.Pool.on(Constants.Pool.DIRECTORY);
        return pool.<String, JsonArray>get(typeSet).compose(cachedMap -> {
            /*
             *  Calculated the
             */
            final ConcurrentMap<String, JsonArray> processed = new ConcurrentHashMap<>();
            final Set<String> newSet = new HashSet<>();
            cachedMap.forEach((key, value) -> {
                if (Ut.isNil(value)) {
                    newSet.add(key);
                } else {
                    processed.put(key, value);
                }
            });
            if (newSet.isEmpty()) {
                return Ux.future(processed);
            } else {
                return executor.apply(newSet).compose(queried -> {
                    final ConcurrentMap<String, Future<JsonArray>> futureMap = new ConcurrentHashMap<>();
                    queried.forEach((key, data) -> futureMap.put(key, pool.put(key, data, 900)
                            .compose(Kv::value)));
                    return Ux.thenCombine(futureMap);
                }).compose(newAdded -> {
                    processed.putAll(newAdded);
                    return Ux.future(processed);
                });
            }
        });
    }
}
