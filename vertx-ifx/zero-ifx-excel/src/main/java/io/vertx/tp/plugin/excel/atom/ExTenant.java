package io.vertx.tp.plugin.excel.atom;

import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.excel.ExcelClient;
import io.vertx.tp.plugin.excel.ExcelInfix;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExTenant implements Serializable {

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject global;

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray source;

    private transient ConcurrentMap<String, JsonObject> mapping;

    public JsonObject getGlobal() {
        return Objects.isNull(this.global) ? new JsonObject() :
            this.global.copy();
    }

    public void setGlobal(final JsonObject global) {
        this.global = global;
    }

    public JsonArray getSource() {
        return this.source;
    }

    public void setSource(final JsonArray source) {
        this.source = source;
    }

    public ConcurrentMap<String, JsonObject> getMapping() {
        return this.mapping;
    }

    public void setMapping(final ConcurrentMap<String, JsonObject> mapping) {
        this.mapping = mapping;
    }

    public ConcurrentMap<String, String> mapping(final String tableName) {
        final ConcurrentMap<String, String> map = new ConcurrentHashMap<>();
        if (Objects.nonNull(this.mapping)) {
            final JsonObject mappingJson = this.mapping.getOrDefault(tableName, new JsonObject());
            Ut.<String>itJObject(mappingJson, (value, field) -> map.put(field, value));
        }
        return map;
    }

    /*
     * Collect the whole dictionary
     * Data Format
     * name =
     *      from = to
     *
     */
    public Future<ConcurrentMap<String, JsonObject>> dictionary() {
        final List<Future<Kv<String, JsonObject>>> futures = new ArrayList<>();
        if (Objects.nonNull(this.source)) {
            Ut.itJArray(this.source, String.class, (expr, index) ->
                futures.add(this.dictionary(expr)));
        }
        return Ux.thenCombineT(futures).compose(result -> {
            final ConcurrentMap<String, JsonObject> dataResult = new ConcurrentHashMap<>();
            if (Objects.nonNull(result)) {
                result.stream().filter(Objects::nonNull)
                    .forEach(kv -> dataResult.put(kv.getKey(), kv.getValue()));
            }
            return Ux.future(dataResult);
        });
    }

    private Future<Kv<String, JsonObject>> dictionary(final String expression) {
        final String[] segments = expression.split(",");
        if (5 <= segments.length) {
            final String filename = segments[0];
            final ExcelClient client = ExcelInfix.createClient();
            return client.ingestAsync(filename).compose(tables -> {
                // Table filter
                final String table = segments[1];
                final Set<ExTable> tableSet = new HashSet<>();
                if (Ut.notNil(table)) {
                    tableSet.addAll(tables.stream()
                        .filter(Objects::nonNull)
                        .filter(each -> table.equals(each.getName()))
                        .collect(Collectors.toSet()));
                }
                return Ux.future(tableSet);
            }).compose(tableSet -> {
                // Data, Criteria Function
                final String condition = 5 < segments.length ? segments[5] : Strings.EMPTY;
                final String[] kv = condition.split("=");
                final Predicate<JsonObject> condFn;
                if (2 == kv.length && Objects.nonNull(kv[0])) {
                    condFn = json -> kv[1].equals(json.getValue(kv[0]));
                } else {
                    condFn = json -> true;
                }
                // Data Filter
                final List<Future<JsonArray>> futures = new ArrayList<>();
                tableSet.stream()
                    .map(Ux::future)
                    .map(single -> single.compose(each -> {
                        final List<ExRecord> records = each.get();
                        final JsonArray data = new JsonArray();
                        records.stream()
                            .filter(Objects::nonNull)
                            .map(ExRecord::toJson)
                            .filter(condFn)
                            .forEach(data::add);
                        return Ux.future(data);
                    }))
                    .forEach(futures::add);
                return Ux.thenCombineArray(futures);
            }).compose(dataArray -> {
                // Result
                final String key = segments[4];
                final String from = segments[2];
                final String to = segments[3];
                final JsonObject data = new JsonObject();
                Ut.itJArray(dataArray).forEach(json -> {
                    if (json.containsKey(from) && json.containsKey(to)) {
                        data.put(json.getString(from), json.getValue(to));
                    }
                });
                return Ux.future(Kv.create(key, data));
            });
        } else {
            return Ux.future();
        }
    }
}
