package io.vertx.mod.atom.modeling.builtin;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MField;
import io.horizon.eon.VString;
import io.modello.atom.normalize.KAttribute;
import io.modello.atom.normalize.KMarkAttribute;
import io.modello.atom.normalize.RRule;
import io.modello.eon.em.EmAttribute;
import io.modello.eon.em.EmValue;
import io.modello.specification.atom.HAttribute;
import io.modello.specification.meta.HMetaField;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
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
        final EmAttribute.Type type = Ut.toEnum(attribute::getType, EmAttribute.Type.class, EmAttribute.Type.INTERNAL);
        final Boolean isArray = Objects.isNull(attribute.getIsArray()) ? Boolean.FALSE : attribute.getIsArray();
        final JsonObject config = Ut.toJObject(attribute.getSourceConfig());
        final JsonObject reference = Ut.toJObject(attribute.getSourceReference());
        EmValue.Format format = Ut.toEnum(() -> config.getString(KName.FORMAT), EmValue.Format.class, EmValue.Format.Elementary);

        /*
         * format adjusting
         * 1. Priority 1: isArray = true, The Data Type is `JsonArray`.
         * 2. Priority 2: isArray must be `false`, set the default value instead.
         */
        if (isArray) {
            format = EmValue.Format.JsonArray;
        }
        attributeJ.put(KName.FORMAT, format);

        /*
         * type analyzing ( Complex Workflow )
         * 1. format = JsonArray, JsonArray.class, fields = JsonArray
         * 2. format = JsonObject, JsonObject.class, fields = JsonObject
         * 3. format = Elementary, `Ut.clazz`
         */
        final Class<?> configType;
        if (EmValue.Format.Elementary == format) {
            configType = Ut.clazz(config.getString(KName.TYPE), String.class);
        } else {
            configType = EmValue.Format.JsonArray == format ? JsonArray.class : JsonObject.class;
        }

        final Class<?> attributeType;
        if (EmAttribute.Type.INTERNAL == type) {
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
        /*
         * java.lang.NullPointerException
             at io.vertx.mod.builtin.modeling.atom.AtomAttribute.<init>(AtomAttribute.java:124)
         */
        if (Objects.nonNull(attributeType)) {
            attributeJ.put(KName.TYPE, attributeType.getName());
        }

        // Expand the `fields` lookup range
        final JsonArray fields = Ut.valueJArray(config.getJsonArray(KName.FIELDS));
        if (Ut.isNotNil(fields)) {
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
        final KMarkAttribute matrix = this.initializeMatrix(attribute);
        this.attribute = new KAttribute(attributeJ, matrix);
    }

    private KMarkAttribute initializeMatrix(final MAttribute attribute) {
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
                literal.append(VString.COMMA);
            }
        }
        return KMarkAttribute.of(literal.toString());
    }

    @Override
    public HMetaField field() {
        return this.attribute.field();
    }

    @Override
    public List<HMetaField> fieldCompiled() {
        return this.attribute.fieldCompiled();
    }

    @Override
    public KMarkAttribute marker() {
        return this.attribute.marker();
    }

    /**
     * Return to `rule`
     *
     * @return {@link RRule}
     */
    @Override
    public RRule referenceRule() {
        return this.attribute.referenceRule();
    }

    /**
     * @return {@link EmValue.Format}
     */
    @Override
    public EmValue.Format format() {
        return this.attribute.format();
    }

}
