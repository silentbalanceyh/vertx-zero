package io.vertx.tp.atom.modeling.reference;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.config.AoSource;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.DataFormat;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

/**
 * ## Result
 *
 * ### 1. Intro
 *
 * Processing result based on configuration `connect` defined, different situation could trigger different joining
 *
 * Here are three critical concept:
 *
 * 1. Name: Mapped to `X_ATTRIBUTE` field `NAME`, it's the model attribute name.
 * 2. SourceField: Mapped to reference field name.
 * 3. JoinField: it's defined by `connect` configuration.
 *
 * ### 2. connect
 *
 * #### 2.1. No definition
 *
 * There is no `connect` field defined in current json configuration.
 *
 * ```json
 * // <pre><code class="json">
 *     {
 *         "sourceField": "name"
 *     }
 * // </code></pre>
 * ```
 *
 * > One to One, single field value calculation
 *
 * 1. Pick data from reference data by `sourceField` name.
 * 2. Convert `sourceField` data to another data value based on `name` and `type` came from {@link RRule}.
 * 3. Build new attribute key-pair `name = value`.
 *
 * > JoinField = SourceField, Name. Result: `name = literal value`.
 *
 * #### 2.2. Single Pair
 *
 * There is `connect` of {@link java.lang.String} defined in json configuration such as following segment.
 *
 * ```json
 * // <pre><code class="json">
 *     {
 *         "connect": "joinField"
 *     }
 * // </code></pre>
 * ```
 *
 * > One to One/Many, join json object with JoinField ( Batch Mode )
 *
 * 1. Pick data from reference data by `sourceField` name.
 * 2. Join data based on `joinField` in Batch mode, connect json object/array.
 * 3. Build new attribute key-pair `name = json object/array`.
 *
 * > JoinField, SourceField, Name. Result: `name = JsonObject / JsonArray`.
 *
 * #### 2.3. Multi Join
 *
 * There is `connect` of {@link io.vertx.core.json.JsonObject} defined in json configuration such as following segment.
 *
 *
 * ```json
 * // <pre><code class="json">
 *     {
 *         "connect": {
 *             "sourceField1": "joinField1",
 *             "sourceField2": "joinField2"
 *         }
 *     }
 * // </code></pre>
 * ```
 *
 * > One to One/Many, join json object with JoinFields ( Batch Mode )
 *
 * 1. Pick data from reference data by `sourceField1` and `sourceField2` name.
 * 2. Join data based on `joinField1` and `joinField2` in Batch mode, connect json object/array.
 * 3. Build new attribute key-pair `name = json object/array`.
 *
 * > JoinFields, SourceFields, Name. Result: `name = JsonObject / JsonArray`.
 *
 *
 * ### 3. K-V
 *
 * 1. Left field is `sourceField` of reference record - `referenceField`.
 * 2. Right field is `joinField` of current record - `currentField`.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RResult implements Serializable {

    private transient RRule rule;

    private final transient DataFormat format;

    private final transient Class<?> type;

    private final transient List<Kv<String, String>> joined = new ArrayList<>();

    public RResult(final MAttribute attribute) {
        final AoSource source = new AoSource(attribute);
        this.type = source.type();
        this.format = source.format();
        /* Joined calculation */
        final JsonObject sourceReference = Ut.toJObject(attribute.getSourceReference());
        final Object connect = sourceReference.getValue(KeField.CONNECT);
        if (Objects.isNull(connect)) {
            /*
             * No connect defined.
             *
             * sourceField = name
             */
            final String currentField = attribute.getName();
            final String referenceField = attribute.getSourceField();
            this.joined.add(Kv.create(referenceField, currentField));
        } else {
            if (connect instanceof String) {
                /*
                 * Single mode.
                 */
                final String currentField = (String) connect;
                final String referenceField = attribute.getSourceField();
                this.joined.add(Kv.create(referenceField, currentField));
            } else if (connect instanceof JsonObject) {
                /*
                 * Multi mode
                 */
                final JsonObject mapping = (JsonObject) connect;
                Ut.<String>itJObject(mapping,
                        (currentField, referenceField) -> this.joined.add(Kv.create(referenceField, currentField)));
            }
        }
    }

    public DataFormat format() {
        return this.format;
    }

    public Class<?> typeData() {
        return this.type;
    }

    public List<Kv<String, String>> joinedList() {
        return this.joined;
    }

    public Kv<String, String> joinedKv() {
        if (Values.ONE == this.joined.size()) {
            return this.joined.get(Values.IDX);
        } else return null;
    }

    @Fluent
    public RResult bind(final RRule rule) {
        this.rule = rule;
        return this;
    }

    public JsonArray runRuler(final JsonArray source) {
        /* 1. `required` rule */
        JsonArray processed = RRuler.required(source, this.rule);
        /* 2. `compress` rule for duplicated */
        processed = RRuler.duplicated(processed, this.rule);

        return processed;
    }
}

final class RRuler {
    private RRuler() {
    }

    public static JsonArray required(final JsonArray source, final RRule rule) {
        /* required fields */
        return rulerAnd(source, rule.getRequired(), value -> Ut.notNil(value.toString()));
    }

    public static JsonArray duplicated(final JsonArray source, final RRule rule) {
        /* unique field */
        final Set<JsonObject> added = new HashSet<>();
        return ruler(source, rule.getUnique(), json -> {
            final JsonObject uniqueJson = Ut.elementSubset(json, rule.getUnique());
            if (added.contains(uniqueJson)) {
                return false;
            } else {
                added.add(uniqueJson);
                return true;
            }
        });
    }

    private static JsonArray rulerAnd(final JsonArray source, final Set<String> fieldSet, final Predicate<Object> fnFilter) {
        return ruler(source, fieldSet, json -> fieldSet.stream().allMatch(field -> {
            final Object value = json.getValue(field);
            if (Objects.nonNull(value)) {
                return fnFilter.test(value);
            } else return false;
        }));
    }

    private static JsonArray rulerOr(final JsonArray source, final Set<String> fieldSet, final Predicate<Object> fnFilter) {
        return ruler(source, fieldSet, json -> fieldSet.stream().anyMatch(field -> {
            final Object value = json.getValue(field);
            if (Objects.nonNull(value)) {
                return fnFilter.test(value);
            } else return false;
        }));
    }

    private static JsonArray ruler(final JsonArray source, final Set<String> fieldSet, final Predicate<JsonObject> fnFilter) {
        if (fieldSet.isEmpty()) {
            return source;
        } else {
            /* Code Logical */
            final JsonArray processed = new JsonArray();
            Ut.itJArray(source).filter(fnFilter).forEach(processed::add);
            return processed;
        }
    }
}
