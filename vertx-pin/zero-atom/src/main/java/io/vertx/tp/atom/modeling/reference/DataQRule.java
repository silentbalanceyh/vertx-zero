package io.vertx.tp.atom.modeling.reference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.codegen.annotations.Fluent;
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
 * ## 「Pojo」QRule Definition Object
 *
 * ### 1. Intro
 *
 * This object could be mapped to `sourceReference` field of `X_ATTRIBUTE`, extracted by `rule` field.
 *
 * ### 2. Format
 *
 * The whole configured json data is as following:
 *
 * ```json
 * // <pre><code class="json">
 *     {
 *         "rule": {
 *             "conditions": {},
 *             "condition": {},
 *             "required": [],
 *             "joined": {},
 *             "diff": []
 *         }
 *     }
 * // </code></pre>
 * ```
 *
 * ### 3. Rule Meaning
 *
 * Each rule is defined as following table.
 *
 * |Rule|Type|Comments|
 * |---|---|:---|
 * |conditions|Input|The criteria condition to fetch data ( Batch ), result is {@link io.vertx.core.json.JsonArray}.|
 * |condition|Input|The criteria condition to fetch data ( Single ), result is {@link io.vertx.core.json.JsonObject}.|
 * |required|Rule|When fetched data source, this rule will check whether each record valid. |
 * |diff|Rule|When fetched data array, this rule will combine by fields, compress the duplicated records. |
 * |joined|Rule|When fetched data array, this rule will connect current record to fetched Array. |
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DataQRule implements Serializable {
    /**
     * The field `condition`.
     */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject condition;
    /**
     * The field `conditions`.
     */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject conditions;

    /**
     * The rule `required`.
     */
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray required;

    /**
     * The rule `joined`.
     */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject joined;

    /**
     * The rule `diff`.
     */
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray diff;

    /**
     * The class type of current attribute defined, two in current version.
     *
     * {@link io.vertx.core.json.JsonArray} or {@link io.vertx.core.json.JsonObject}
     */
    @JsonIgnore
    private transient Class<?> type;

    /**
     * @return {@link io.vertx.core.json.JsonObject}
     */
    public JsonObject getCondition() {
        return this.condition;
    }

    /**
     * @param condition {@link io.vertx.core.json.JsonObject}
     */
    public void setCondition(final JsonObject condition) {
        this.condition = condition;
    }

    /**
     * @return {@link io.vertx.core.json.JsonObject}
     */
    public JsonObject getConditions() {
        return this.conditions;
    }

    /**
     * @param conditions {@link io.vertx.core.json.JsonObject}
     */
    public void setConditions(final JsonObject conditions) {
        this.conditions = conditions;
    }

    /**
     * @return {@link io.vertx.core.json.JsonArray}
     */
    public JsonArray getRequired() {
        return this.required;
    }

    /**
     * @param required {@link io.vertx.core.json.JsonArray}
     */
    public void setRequired(final JsonArray required) {
        this.required = required;
    }

    /**
     * @return {@link io.vertx.core.json.JsonObject}
     */
    public JsonObject getJoined() {
        return this.joined;
    }

    /**
     * @param joined {@link io.vertx.core.json.JsonObject}
     */
    public void setJoined(final JsonObject joined) {
        this.joined = joined;
    }

    /**
     * @return {@link io.vertx.core.json.JsonArray}
     */
    public JsonArray getDiff() {
        return this.diff;
    }

    /**
     * @param diff {@link io.vertx.core.json.JsonArray}
     */
    public void setDiff(final JsonArray diff) {
        this.diff = diff;
    }

    /**
     * @return {@link java.lang.Class}
     */
    public Class<?> type() {
        return this.type;
    }

    /**
     * 「Fluent」Set the field `type`.
     *
     * @param type {@link java.lang.Class}
     *
     * @return this
     */
    @Fluent
    public DataQRule type(final Class<?> type) {
        this.type = type;
        return this;
    }

    /**
     * Returned the `key = value` of `joined` rule, flat the object to array.
     *
     * @return {@link java.util.List}<{@link io.vertx.up.atom.Kv}>
     */
    public List<Kv<String, String>> joined() {
        final List<Kv<String, String>> joinedKeys = new ArrayList<>();
        Ut.<String>itJObject(this.joined, (value, key) -> joinedKeys.add(Kv.create(key, value)));
        return joinedKeys;
    }

    /**
     * Build criteria condition based on data record.
     *
     * @param record {@link io.vertx.up.commune.Record} Input data record
     *
     * @return {@link io.vertx.core.json.JsonObject}
     */
    public JsonObject condition(final Record record) {
        final JsonObject tpl = new JsonObject();
        this.condition(this.condition).forEach(field -> {
            // Target field
            final String target = this.condition.getString(field);
            if (Objects.nonNull(record)) {
                // Null Pointer for record
                final Object value = record.get(target);
                if (Objects.nonNull(value)) {
                    tpl.put(field, value);
                }
            }
        });
        // If null of "", the AND operator will be set.
        tpl.put(Strings.EMPTY, this.condition.getBoolean(Strings.EMPTY, Boolean.FALSE));
        Ao.infoUca(this.getClass(), "Single condition building: {0}", tpl.encode());
        return tpl;
    }

    /**
     * Build criteria condition based on data records.
     *
     * @param records {@link io.vertx.up.commune.Record}[] Input data records.
     *
     * @return {@link io.vertx.core.json.JsonObject}
     */
    public JsonObject condition(final Record[] records) {
        final JsonObject tpl = new JsonObject();
        this.condition(this.conditions).forEach(field -> {
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

    /**
     * Internal usage of function stream to process condition safety
     *
     * @param condition {@link io.vertx.core.json.JsonObject} The raw criteria json.
     *
     * @return {@link java.util.stream.Stream}<{@link java.lang.String}>
     */
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
