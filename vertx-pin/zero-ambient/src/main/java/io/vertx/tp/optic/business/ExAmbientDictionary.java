package io.vertx.tp.optic.business;

import cn.vertxup.ambient.domain.tables.daos.XCategoryDao;
import cn.vertxup.ambient.domain.tables.daos.XTabularDao;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.refine.At;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.optic.component.Dictionary;
import io.vertx.tp.optic.component.DictionaryPlugin;
import io.vertx.up.commune.config.DictSource;
import io.vertx.up.eon.em.SourceType;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Dictionary implementation class
 */
public class ExAmbientDictionary implements Dictionary {
    @Override
    public Future<ConcurrentMap<String, JsonArray>> fetchAsync(final MultiMap paramMap,
                                                               final List<DictSource> sources) {
        /*
         * Whether sources is empty
         */
        if (Objects.isNull(sources) || sources.isEmpty()) {
            /*
             * Empty processing
             */
            return Ux.future(new ConcurrentHashMap<>());
        } else {
            /*
             * Future merged here
             */
            final List<Future<ConcurrentMap<String, JsonArray>>> futures = new ArrayList<>();
            sources.forEach(source -> futures.add(this.fetchSource(source, paramMap)));
            /*
             * Merged each futures here
             * 1) Tabular ( type -> JsonArray )      size > 0
             * 2) Category ( type -> JsonArray )     size > 0
             * 3) Assist ( type -> JsonArray )       size > 0
             */
            return Ux.thenCompress(futures).compose(dict -> {
                final StringBuilder report = new StringBuilder();
                report.append("[ PT ] Dictionary Total：").append(dict.size());
                dict.forEach((key, array) -> report
                        .append("\n\tkey = ").append(key)
                        .append(", value size = ").append(array.size()));
                At.infoFlow(this.getClass(), report.toString());
                return Ux.future(dict);
            });
        }
    }

    private Future<ConcurrentMap<String, JsonArray>> fetchSource(final DictSource source,
                                                                 final MultiMap paramMap) {
        final SourceType type = source.getSourceType();
        if (SourceType.TABULAR == type) {
            /*
             * TABULAR data source fetching
             */
            return this.fetchTabular(source, paramMap);
        } else if (SourceType.CATEGORY == type) {
            /*
             * CATEGORY data source fetching
             */
            return this.fetchCategory(source, paramMap);
        } else {
            /*
             * Assist
             */
            return this.fetchAssist(source, paramMap);
        }
    }

    private Future<ConcurrentMap<String, JsonArray>> fetchTabular(final DictSource source,
                                                                  final MultiMap paramMap) {
        final String sigma = paramMap.get(KeField.SIGMA);
        return Ux.Jooq.on(XTabularDao.class)
                .fetchAndAsync(this.toFilters(source, sigma))
                .compose(Ux::fnJMapType);
    }

    private Future<ConcurrentMap<String, JsonArray>> fetchCategory(final DictSource source,
                                                                   final MultiMap paramMap) {
        final String sigma = paramMap.get(KeField.SIGMA);
        return Ux.Jooq.on(XCategoryDao.class)
                .fetchAndAsync(this.toFilters(source, sigma))
                .compose(Ux::fnJMapType);
    }

    private Future<ConcurrentMap<String, JsonArray>> fetchAssist(final DictSource source,
                                                                 final MultiMap paramMap) {
        final ConcurrentMap<String, JsonArray> uniqueMap = new ConcurrentHashMap<>();
        final DictionaryPlugin plugin = source.getPlugin();
        if (Objects.isNull(plugin) || Ut.isNil(source.getKey())) {
            return Ux.future(uniqueMap);
        } else {
            /*
             * Bind configuration from source here
             */
            plugin.configuration(source.getPluginConfig());
            /*
             * Then execute and get the data.
             */
            return plugin.fetchAsync(source, paramMap).compose(result -> {
                uniqueMap.put(source.getKey(), result);
                return Ux.future(uniqueMap);
            });
        }
    }

    private JsonObject toFilters(final DictSource source, final String sigma) {
        final JsonObject filters = new JsonObject();
        filters.put(KeField.SIGMA, sigma);
        /*
         * Types
         */
        final Set<String> typeSet = source.getTypes();
        if (!typeSet.isEmpty()) {
            filters.put(KeField.TYPE + ",i", Ut.toJArray(typeSet));
            filters.put("", Boolean.TRUE);
        }
        return filters;
    }
}
