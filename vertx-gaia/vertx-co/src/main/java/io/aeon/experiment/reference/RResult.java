package io.aeon.experiment.reference;

import io.horizon.atom.Kv;
import io.horizon.eon.em.typed.DataFormat;
import io.horizon.specification.modeler.HAttribute;
import io.horizon.specification.modeler.HRule;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
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
 * #### 2.1. Single Pair
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
 * #### 2.2. Multi Join
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

    private final HRule rule;

    private final DataFormat format;

    private final Class<?> type;

    private final List<Kv<String, String>> joined = new ArrayList<>();

    private final String sourceField;

    public RResult(final String referenceField, final JsonObject referenceConfig, final HAttribute config) {
        this.type = config.field().type();
        this.format = config.format();
        this.rule = config.refRule();
        this.sourceField = referenceField;
        /* Joined calculation */
        final JsonObject sourceReference = Ut.valueJObject(referenceConfig);
        final Object connect = sourceReference.getValue(KName.CONNECT);
        if (Objects.nonNull(connect)) {
            if (connect instanceof String) {
                /*
                 * Single mode.
                 * Example:
                 *
                 * name, sourceField, connect
                 *
                 * up, targetGlobalId = globalId
                 *
                 * supportAName, realname = workNumber
                 *
                 * 1. field = JsonObject/JsonArray, ( Mode 1 )
                 * 2. field = Elementary, ( Mode 2 )
                 *
                 * reference key: targetGlobalId
                 * record key: globalId
                 */
                final String currentField = (String) connect;
                this.joined.add(Kv.create(referenceField, currentField));
            } else if (connect instanceof JsonObject) {
                /*
                 * Multi mode
                 *
                 * reference key: workNumber
                 * record key: supportANo
                 */
                final JsonObject mapping = (JsonObject) connect;
                Ut.<String>itJObject(mapping, (currentField, field) -> this.joined.add(Kv.create(field, currentField)));
            }
        }
    }

    public DataFormat format() {
        return this.format;
    }

    public Class<?> typeData() {
        return this.type;
    }

    public List<Kv<String, String>> joined() {
        return this.joined;
    }

    public String sourceField() {
        return this.sourceField;
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

    public static JsonArray required(final JsonArray source, final HRule rule) {
        /* required fields */
        return rulerAnd(source, rule.getRequired(), value -> Ut.isNotNil(value.toString()));
    }

    public static JsonArray duplicated(final JsonArray source, final HRule rule) {
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
            } else {
                return false;
            }
        }));
    }

    private static JsonArray rulerOr(final JsonArray source, final Set<String> fieldSet, final Predicate<Object> fnFilter) {
        return ruler(source, fieldSet, json -> fieldSet.stream().anyMatch(field -> {
            final Object value = json.getValue(field);
            if (Objects.nonNull(value)) {
                return fnFilter.test(value);
            } else {
                return false;
            }
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
