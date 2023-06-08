package io.aeon.experiment.mixture;

import io.aeon.experiment.specification.KModule;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.shape.KJoin;
import io.vertx.up.atom.shape.KPoint;
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
        final ConcurrentMap<String, Class<?>> moduleMap = this.loadType(module);
        final ConcurrentMap<String, Class<?>> typeMap = new ConcurrentHashMap<>(moduleMap);

        // 2. Extract Type of connect
        if (Objects.nonNull(connect)) {
            // Connect Bind
            ConcurrentMap<String, Class<?>> connectMap = this.loadType(connect);
            // 3. Point Target Processing for synonym
            final KPoint target = module.getConnect(connect.identifier());
            if (Objects.nonNull(target)) {
                connectMap = this.synonym(connectMap, target);
            }
            typeMap.putAll(connectMap);
        }
        return typeMap;
    }

    private ConcurrentMap<String, Class<?>> synonym(
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

    /*
     * Source Processed
     * 1. DaoCls -> type map
     * 2. `connect` = null
     *    -- Put all current module type map into result.
     *    `connect` not null
     *    -- Put all source synonym information ( Renamed ) into type map
     * 3. Combine and get final result
     */
    private ConcurrentMap<String, Class<?>> loadType(final KModule module) {
        final ConcurrentMap<String, Class<?>> typeMap = new ConcurrentHashMap<>();
        final ConcurrentMap<String, Class<?>> moduleMap = this.loadType(module.getDaoCls());
        final KJoin join = module.getConnect();
        if (Objects.isNull(join)) {
            typeMap.putAll(this.synonym(moduleMap, null));
        } else {
            // KPoint Existing
            typeMap.putAll(this.synonym(moduleMap, join.getSource()));
        }
        return typeMap;
    }

    private ConcurrentMap<String, Class<?>> loadType(final Class<?> daoCls) {
        final UxJooq jq = Ux.Jooq.on(daoCls);
        final JqAnalyzer analyzer = jq.analyzer();
        return analyzer.types();
    }
}
