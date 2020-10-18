package io.vertx.tp.atom.modeling.reference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.atom.Kv;
import io.vertx.up.commune.Record;
import io.vertx.up.eon.Strings;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class DataQRule implements Serializable {
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject condition;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject conditions;

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray required;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject joined;

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray diff;

    @JsonIgnore
    private transient Class<?> type;

    public JsonObject getCondition() {
        return this.condition;
    }

    public void setCondition(final JsonObject condition) {
        this.condition = condition;
    }

    public JsonObject getConditions() {
        return this.conditions;
    }

    public void setConditions(final JsonObject conditions) {
        this.conditions = conditions;
    }

    public JsonArray getRequired() {
        return this.required;
    }

    public void setRequired(final JsonArray required) {
        this.required = required;
    }

    public JsonObject getJoined() {
        return this.joined;
    }

    public void setJoined(final JsonObject joined) {
        this.joined = joined;
    }

    public JsonArray getDiff() {
        return this.diff;
    }

    public void setDiff(final JsonArray diff) {
        this.diff = diff;
    }

    public Class<?> type() {
        return this.type;
    }

    public DataQRule type(final Class<?> type) {
        this.type = type;
        return this;
    }

    public List<Kv<String, String>> joined() {
        final List<Kv<String, String>> joinedKeys = new ArrayList<>();
        Ut.<String>itJObject(this.joined, (value, key) -> joinedKeys.add(Kv.create(key, value)));
        return joinedKeys;
    }

    /*
     * 条件构造，只支持一个等级（不递归）
     */
    public JsonObject condition(final Record record) {
        final JsonObject tpl = new JsonObject();
        this.condition(this.condition).forEach(field -> {
            // 目标字段
            final String target = this.condition.getString(field);
            if (Objects.nonNull(record)) {
                // Null Pointer for record
                final Object value = record.get(target);
                if (Objects.nonNull(value)) {
                    tpl.put(field, value);
                }
            }
        });
        tpl.put(Strings.EMPTY, this.condition.getBoolean(Strings.EMPTY, Boolean.FALSE));
        Ao.infoUca(this.getClass(), "Single condition building: {0}", tpl.encode());
        return tpl;
    }

    public JsonObject condition(final Record[] records) {
        final JsonObject tpl = new JsonObject();
        this.condition(this.conditions).forEach(field -> {
            // 目标字段
            final String target = this.conditions.getString(field);
            final Set<Object> values = Arrays.stream(records)
                    .map(record -> record.get(target))
                    .filter(Objects::nonNull).collect(Collectors.toSet());
            tpl.put(field, Ut.toJArray(values));
        });
        tpl.put(Strings.EMPTY, this.conditions.getBoolean(Strings.EMPTY, Boolean.FALSE));
        Ao.infoUca(this.getClass(), "Batch condition building: {0}", tpl.encode());
        return tpl;
    }

    private Stream<String> condition(final JsonObject condition) {
        return condition.fieldNames().stream()
                // 过滤条件
                .filter(Ut::notNil)
                // 过滤空
                .filter(field -> Objects.nonNull(condition.getValue(field)))
                // 过滤非String类型
                .filter(field -> condition.getValue(field) instanceof String);
    }
}
