package io.modello.atom.normalize;

import io.horizon.atom.common.Kv;
import io.horizon.eon.VName;
import io.horizon.util.HUt;
import io.modello.eon.em.EmValue;
import io.modello.specification.atom.HAttribute;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private final RRule rule;

    private final EmValue.Format format;

    private final Class<?> type;

    private final List<Kv<String, String>> joined = new ArrayList<>();

    private final String sourceField;

    public RResult(final String referenceField, final JsonObject referenceConfig, final HAttribute config) {
        this.type = config.field().type();
        this.format = config.format();
        this.rule = config.referenceRule();
        this.sourceField = referenceField;
        /* Joined calculation */
        final JsonObject sourceReference = HUt.valueJObject(referenceConfig);
        final Object connect = sourceReference.getValue(VName.CONNECT);
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
                final String currentField = connect.toString();
                this.joined.add(Kv.create(referenceField, currentField));
            } else if (connect instanceof JsonObject) {
                /*
                 * Multi mode
                 *
                 * reference key: workNumber
                 * record key: supportANo
                 */
                final JsonObject mapping = (JsonObject) connect;
                mapping.fieldNames().stream()
                    .filter(item -> mapping.getValue(item) instanceof String)
                    .forEach(field -> this.joined.add(Kv.create(field, mapping.getString(field))));
                //                HUt.<String>itJObject(mapping, (currentField, field) -> );
            }
        }
    }

    public EmValue.Format format() {
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
