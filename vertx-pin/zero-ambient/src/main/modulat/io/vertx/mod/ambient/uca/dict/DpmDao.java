package io.vertx.mod.ambient.uca.dict;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.exchange.DSource;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.uca.cache.RapidKey;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DpmDao implements Dpm {
    @Override
    public Future<ConcurrentMap<String, JsonArray>> fetchAsync(final DSource source, final MultiMap params) {
        final UxJooq jooq = this.dao(source);
        if (Objects.isNull(jooq) || Ut.isNil(source.getKey())) {
            return Ux.future(new ConcurrentHashMap<>());
        } else {
            return Rapid.<String, JsonArray>t(RapidKey.DIRECTORY, KWeb.ARGS.V_DATA_EXPIRED)
                .cached(source.getKey(), () -> jooq.fetchJAsync(this.condition(params)))
                .compose(result -> {
                    final ConcurrentMap<String, JsonArray> uniqueMap = new ConcurrentHashMap<>();
                    uniqueMap.put(source.getKey(), result);
                    return Ux.future(uniqueMap);
                });
        }
    }

    @Override
    public ConcurrentMap<String, JsonArray> fetch(final DSource source, final MultiMap params) {
        final UxJooq jooq = this.dao(source);
        if (Objects.isNull(jooq) || Ut.isNil(source.getKey())) {
            return new ConcurrentHashMap<>();
        }
        final JsonArray result = jooq.fetchJ(this.condition(params));
        final ConcurrentMap<String, JsonArray> uniqueMap = new ConcurrentHashMap<>();
        uniqueMap.put(source.getKey(), result);
        return uniqueMap;
    }

    private JsonObject condition(final MultiMap params) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.SIGMA, params.get(KName.SIGMA));
        return condition;
    }

    private UxJooq dao(final DSource source) {
        final ConcurrentMap<String, JsonArray> uniqueMap = new ConcurrentHashMap<>();
        final Class<?> daoCls = source.getComponent();
        if (Objects.isNull(daoCls) || Ut.isNil(source.getKey())) {
            return null;
        } else {
            final JsonObject config = source.getPluginConfig();
            final UxJooq jooq = Ux.Jooq.on(daoCls);
            if (config.containsKey("pojo")) {
                jooq.on(config.getString("pojo"));
            }
            return jooq;
        }
    }
}
