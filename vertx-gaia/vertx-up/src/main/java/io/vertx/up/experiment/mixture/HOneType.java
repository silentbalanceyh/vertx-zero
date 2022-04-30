package io.vertx.up.experiment.mixture;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.up.experiment.specification.KJoin;
import io.vertx.up.experiment.specification.KModule;
import io.vertx.up.experiment.specification.KPoint;
import io.vertx.up.uca.jooq.JqAnalyzer;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class HOneType implements HOne<ConcurrentMap<String, Class<?>>> {
    @Override
    public ConcurrentMap<String, Class<?>> combine(final KModule module, final KModule connect, final MultiMap headers) {
        // 1. Extract Type of module
        final ConcurrentMap<String, Class<?>> typeMap = new ConcurrentHashMap<>();
        final ConcurrentMap<String, Class<?>> moduleMap = this.typedMap(module.getDaoCls());


        // 2. Extract Type of module
        final KJoin join = module.getConnect();
        if (Objects.isNull(join)) {
            typeMap.putAll(this.typedMap(moduleMap, null));
        } else {
            // KPoint Existing
            typeMap.putAll(this.typedMap(moduleMap, join.getSource()));
        }


        // 3. Extract Type of connect
        if (Objects.nonNull(connect)) {
            final ConcurrentMap<String, Class<?>> connectMap = this.typedMap(connect.getDaoCls());
            typeMap.putAll(this.typedMap(connectMap, null));
        }
        return typeMap;
    }

    private ConcurrentMap<String, Class<?>> typedMap(
        final ConcurrentMap<String, Class<?>> typedMap, final KPoint point) {
        final ConcurrentMap<String, Class<?>> typedResult = new ConcurrentHashMap<>();
        if (Objects.isNull(point) || Ut.isNil(point.getSynonym())) {
            // point is null ( Or ) synonym is null
            typedResult.putAll(typedMap);
        } else {
            final JsonObject synonym = point.getSynonym();
            typedMap.forEach((field, type) -> {
                if (synonym.containsKey(field)) {
                    // Renaming
                    final String targetField = synonym.getString(field);
                    typedResult.put(targetField, type);
                } else {
                    // Keep the original
                    typedResult.put(field, type);
                }
            });
        }
        return typedResult;
    }

    private ConcurrentMap<String, Class<?>> typedMap(final Class<?> daoCls) {
        final UxJooq jq = Ux.Jooq.on(daoCls);
        final JqAnalyzer analyzer = jq.analyzer();
        return analyzer.types();
    }
}
