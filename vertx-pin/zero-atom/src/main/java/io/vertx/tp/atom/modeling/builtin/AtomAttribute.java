package io.vertx.tp.atom.modeling.builtin;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MField;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.DataFormat;
import io.vertx.up.eon.em.atom.AttributeType;
import io.vertx.up.experiment.mixture.HAttribute;
import io.vertx.up.experiment.mixture.HRule;
import io.vertx.up.experiment.mixture.HTField;
import io.vertx.up.experiment.mu.KAttribute;
import io.vertx.up.experiment.mu.KTag;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ## 「Pojo」SourceConfig
 *
 * Here are one implementation of HAttribute ( New )
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AtomAttribute implements HAttribute, Serializable {
    private final KAttribute attribute;

    /**
     * Create new AoService
     *
     * @param attribute {@link cn.vertxup.atom.domain.tables.pojos.MAttribute} `M_ATTRIBUTE` referred
     */
    public AtomAttribute(final MAttribute attribute, final MField sourceField) {
        /*
         * {
         *     "name",
         *     "alias",
         *     "type",
         *     "format": "JsonArray, JsonObject, Elementary",
         *     "fields": [
         *         {
         *              "field": "",
         *              "alias": "",
         *              "type": "null -> String.class | ???"
         *         }
         *     ],
         *     "rule": {
         *     }
         * }
         */
        final JsonObject attributeJ = new JsonObject();
        attributeJ.put(KName.NAME, attribute.getName());
        attributeJ.put(KName.ALIAS, attribute.getAlias());
        /*
         * 1. type: Attribute `TYPE` database field stored
         * 2. isArray: Check whether current attribute is Array Type
         * 3. config: serviceConfig
         * 4. reference: sourceReference
         * 5. DataFormat extract and current type building
         */
        final AttributeType type = Ut.toEnum(attribute::getType, AttributeType.class, AttributeType.INTERNAL);
        final Boolean isArray = Objects.isNull(attribute.getIsArray()) ? Boolean.FALSE : attribute.getIsArray();
        final JsonObject config = Ut.toJObject(attribute.getSourceConfig());
        final JsonObject reference = Ut.toJObject(attribute.getSourceReference());
        DataFormat format = Ut.toEnum(() -> config.getString(KName.FORMAT), DataFormat.class, DataFormat.Elementary);

        /*
         * format adjusting
         * 1. Priority 1: isArray = true, The Data Type is `JsonArray`.
         * 2. Priority 2: isArray must be `false`, set the default value instead.
         */
        if (isArray) {
            format = DataFormat.JsonArray;
        }
        attributeJ.put(KName.FORMAT, format);

        /*
         * type analyzing ( Complex Workflow )
         * 1. format = JsonArray, JsonArray.class, fields = JsonArray
         * 2. format = JsonObject, JsonObject.class, fields = JsonObject
         * 3. format = Elementary, `Ut.clazz`
         */
        final Class<?> configType;
        if (DataFormat.Elementary == format) {
            configType = Ut.clazz(config.getString(KName.TYPE), String.class);
        } else {
            configType = DataFormat.JsonArray == format ? JsonArray.class : JsonObject.class;
        }

        final Class<?> attributeType;
        if (AttributeType.INTERNAL == type) {
            /*
             * type = INTERNAL ( Stored / Virtual )
             */
            if (KName.Modeling.VALUE_SET.contains(attribute.getSourceField())) {
                /*
                 * BEFORE / AFTER
                 * Default type is: String
                 */
                attributeType = configType;
            } else {
                /*
                 * Type defined in sourceField
                 * Here may be
                 *
                 * 1. ???
                 * 2. JsonObject
                 * 3. JsonArray
                 */
                attributeType = Objects.isNull(sourceField) ? null : Ut.clazz(sourceField.getType(), null);
            }
        } else {
            /*
             * Reference or External
             */
            attributeType = configType;
        }
        // Type analyzed, create current type
        attributeJ.put(KName.TYPE, attributeType.getName());

        // Expand the `fields` lookup range
        final JsonArray fields = Ut.valueJArray(config.getJsonArray(KName.FIELDS));
        if (Ut.notNil(fields)) {
            attributeJ.put(KName.FIELDS, fields);
        }

        /*
         *  Bind `rule` processing
         *  Be careful of that the `rule` should be configured in `sourceReference` field instead of
         * `sourceConfig` here
         */
        if (reference.containsKey(KName.RULE)) {
            final JsonObject ruleData = Ut.valueJObject(reference.getJsonObject(KName.RULE));
            attributeJ.put(KName.RULE, ruleData);
        }
        /*
         * KMatrix Building
         */
        final KTag matrix = this.initializeMatrix(attribute);
        this.attribute = new KAttribute(attributeJ, matrix);
    }

    private KTag initializeMatrix(final MAttribute attribute) {
        final StringBuilder literal = new StringBuilder();
        // Boolean -> 0, 1
        final List<Boolean> values = new ArrayList<>();
        values.add(attribute.getActive());
        values.add(attribute.getIsTrack());
        values.add(attribute.getIsLock());
        values.add(attribute.getIsConfirm());
        values.add(attribute.getIsArray());
        values.add(attribute.getIsSyncIn());
        values.add(attribute.getIsSyncOut());
        values.add(attribute.getIsRefer());
        for (int idx = 0; idx < values.size(); idx++) {
            final Boolean value = values.get(idx);
            if (Objects.isNull(value)) {
                literal.append("NULL");
            } else {
                literal.append(value);
            }
            if (idx < (values.size() - 1)) {
                literal.append(Strings.COMMA);
            }
        }
        return new KTag(literal.toString());
    }

    /**
     * Return to current type field
     *
     * @return {@link HTField}
     */
    @Override
    public HTField field() {
        return this.attribute.field();
    }

    /**
     * Return to `fields`
     *
     * @return {@link List}
     */
    @Override
    public List<HTField> fields() {
        return this.attribute.fields();
    }

    @Override
    public KTag tag() {
        return this.attribute.tag();
    }

    /**
     * Return to `rule`
     *
     * @return {@link HRule}
     */
    @Override
    public HRule rule() {
        return this.attribute.rule();
    }

    /**
     * @return {@link DataFormat}
     */
    @Override
    public DataFormat format() {
        return this.attribute.format();
    }

}
