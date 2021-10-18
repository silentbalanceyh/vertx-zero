package io.vertx.tp.atom.modeling.reference;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.config.AoAttribute;
import io.vertx.tp.atom.modeling.config.AoRule;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.KName;
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

    private final transient AoRule rule;

    private final transient DataFormat format;

    private final transient Class<?> type;

    private final transient List<Kv<String, String>> joined = new ArrayList<>();

    private final transient String sourceField;

    public RResult(final MAttribute attribute, final AoAttribute aoAttr) {
        this.type = aoAttr.typeCls();
        this.format = aoAttr.format();
        this.rule = aoAttr.rule();
        this.sourceField = attribute.getSourceField();
        /* Joined calculation */
        final JsonObject sourceReference = Ut.toJObject(attribute.getSourceReference());
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
                final String referenceField = attribute.getSourceField();
                this.joined.add(Kv.create(referenceField, currentField));
            } else if (connect instanceof JsonObject) {
                /*
                 * Multi mode
                 *
                 * reference key: workNumber
                 * record key: supportANo
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

    public static JsonArray required(final JsonArray source, final AoRule rule) {
        /* required fields */
        return rulerAnd(source, rule.getRequired(), value -> Ut.notNil(value.toString()));
    }

    public static JsonArray duplicated(final JsonArray source, final AoRule rule) {
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
