package io.modello.atom.normalize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.horizon.eon.VString;
import io.horizon.uca.log.Annal;
import io.horizon.util.HUt;
import io.modello.specification.HRecord;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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
 * |unique|Rule|When fetched data source, this rule will compress duplicated records ( pick first ). |
 * |diff|Compare|When fetched data array, this rule will combine by fields, compress the duplicated records. |
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RRule implements Serializable {
    private static final Annal LOGGER = Annal.get(RRule.class);
    /**
     * The field `condition`.
     */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject condition;
    /**
     * The field `conditions`.
     */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject conditions;

    /**
     * The rule `required`.
     */
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray required;

    /**
     * The rule 'unique`.
     */
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray unique;

    /**
     * The rule `diff`.
     */
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray diff;

    /**
     * The class type of current attribute defined, two in current version.
     *
     * {@link io.vertx.core.json.JsonArray} or {@link io.vertx.core.json.JsonObject}
     */
    @JsonIgnore
    private Class<?> type;

    /**
     * @return {@link java.util.Set}
     */
    public Set<String> getUnique() {
        if (HUt.isNotNil(this.unique)) {
            return HUt.toSet(this.unique);
        } else {
            return new HashSet<>();
        }
    }

    /**
     * @param unique {@link io.vertx.core.json.JsonArray}
     */
    public void setUnique(final JsonArray unique) {
        this.unique = unique;
    }

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
     * @return {@link java.util.Set}
     */
    public Set<String> getRequired() {
        if (HUt.isNotNil(this.required)) {
            return HUt.toSet(this.required);
        } else {
            return new HashSet<>();
        }
    }

    /**
     * @param required {@link io.vertx.core.json.JsonArray}
     */
    public void setRequired(final JsonArray required) {
        this.required = required;
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
    public RRule type(final Class<?> type) {
        this.type = type;
        return this;
    }

    /**
     * Return to daoKey of current item.
     *
     * @return {@link java.lang.String}
     */
    public String keyDao() {
        return "condition:" + this.condition.hashCode() + ":" + "conditions:" + this.conditions.hashCode();
    }

    /**
     * Build criteria condition based on data record.
     *
     * @param record {@link HRecord} Input data record
     *
     * @return {@link io.vertx.core.json.JsonObject}
     */
    public JsonObject condition(final HRecord record) {
        final JsonObject tpl = new JsonObject();
        this.condition(this.condition).forEach(field -> {
            // Target field
            final String target = this.condition.getString(field);
            if (Objects.nonNull(record)) {
                // Null Pointer for record
                final Object value = record.get(target);
                if (Objects.nonNull(value) && HUt.isNotNil(value.toString())) {
                    tpl.put(field, value);
                }
            }
        });
        if (HUt.isNotNil(tpl)) {
            LOGGER.info("[ EMF ] Single condition building: {0}", tpl.encode());
        }
        // If null of "", the AND operator will be set.
        tpl.put(VString.EMPTY, this.condition.getBoolean(VString.EMPTY, Boolean.TRUE));
        return tpl;
    }

    /**
     * Build criteria condition based on data records.
     *
     * @param records {@link HRecord}[] Input data records.
     *
     * @return {@link io.vertx.core.json.JsonObject}
     */
    public JsonObject condition(final HRecord[] records) {
        final JsonObject tpl = new JsonObject();
        this.condition(this.conditions).forEach(field -> {
            final String target = this.conditions.getString(field);
            final Set<Object> values = Arrays.stream(records)
                .map(record -> record.get(target))
                .filter(Objects::nonNull).collect(Collectors.toSet());
            final JsonArray valueArray = HUt.toJArray(values);
            if (HUt.isNotNil(valueArray)) {
                tpl.put(field, HUt.toJArray(values));
            }
        });
        // If null of "", the AND operator will be set.
        tpl.put(VString.EMPTY, this.conditions.getBoolean(VString.EMPTY, Boolean.TRUE));
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
            .filter(HUt::isNotNil)
            // 过滤空
            .filter(field -> Objects.nonNull(condition.getValue(field)))
            // 过滤非String类型
            .filter(field -> condition.getValue(field) instanceof String);
    }

    @Override
    public String toString() {
        return "HRule{" +
            "condition=" + this.condition +
            ", conditions=" + this.conditions +
            ", required=" + this.required +
            ", unique=" + this.unique +
            ", diff=" + this.diff +
            ", type=" + this.type +
            '}';
    }
}
