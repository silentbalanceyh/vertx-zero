package io.vertx.tp.atom.modeling.config;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MField;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.cv.em.AttributeType;
import io.vertx.up.commune.element.TypeField;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.DataFormat;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ## 「Pojo」SourceConfig
 *
 * ### 1. Intro
 *
 * Here are the parsing workflow:
 *
 * 1. When <strong>isArray</strong> = true, the dataFormat is `JsonArray`.
 * 2. Otherwise, extract `type` field of `sourceConfig` to get actual dataFormat.
 *
 * ### 2. Limitation
 *
 * #### 2.1. JsonArray
 *
 * 1. INTERNAL: Must contains `fields` configuration for sub-elements.
 * 2. REFERENCE/EXTERNAL: No.
 * 3. The `type` is {@link io.vertx.core.json.JsonArray};
 *
 * #### 2.2. JsonObject
 *
 * 1. INTERNAL: Must contains `fields` configuration for sub-elements.
 * 2. REFERENCE/EXTERNAL: No.
 * 3. The `type` is {@link io.vertx.core.json.JsonObject};
 *
 * #### 2.3. Elementary
 *
 * 1. The config must contains `type` field to define java class type name.
 * 2. It's default value here. `type = {@link java.lang.String}`, `source = Elementary`.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AoAttribute implements Serializable {
    /**
     * {@link DataFormat} is for fieldSource
     */
    private final transient DataFormat dataFormat;
    /**
     * The `shapes` that mapped to `fields` attribute
     */
    private final transient List<TypeField> shapes = new ArrayList<>();

    private final transient TypeField type;
    /**
     * The `rule` for serviceConfig here.
     */
    private transient AoRule rule;

    /**
     * Create new AoService
     *
     * @param attribute {@link cn.vertxup.atom.domain.tables.pojos.MAttribute} `M_ATTRIBUTE` referred
     */
    public AoAttribute(final MAttribute attribute, final MField sourceField) {
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
        if (isArray) format = DataFormat.JsonArray;
        this.dataFormat = format;

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
        this.type = TypeField.create(attribute.getName(), attribute.getAlias(), attributeType);

        // Expand the `fields` lookup range
        final JsonArray fields = Ut.sureJArray(config.getJsonArray(KName.FIELDS));
        Ut.itJArray(fields).forEach(item -> {
            final String field = item.getString(KName.FIELD);
            if (Ut.notNil(field)) {
                final String alias = item.getString(KName.ALIAS, null);
                final Class<?> subType = Ut.clazz(item.getString(KName.TYPE), String.class);
                this.shapes.add(TypeField.create(field, alias, subType));
            }
        });
        // Add children into TypeField for complex
        if (DataFormat.Elementary != format) {
            this.type.add(this.shapes);
        }

        /*
         *  Bind `rule` processing
         *  Be careful of that the `rule` should be configured in `sourceReference` field instead of
         * `sourceConfig` here
         */
        if (reference.containsKey(KName.RULE)) {
            final JsonObject ruleData = Ut.sureJObject(reference.getJsonObject(KName.RULE));
            this.rule = Ut.deserialize(ruleData, AoRule.class);
            /* Bind type into rule */
            this.rule.type(attributeType);
            /* Unique rule for diffSet */
            this.type.ruleUnique(this.rule.getUnique());
        }
    }

    /**
     * Return to current data type
     *
     * @return {@link java.lang.Class}
     */
    public Class<?> typeCls() {
        return this.type.type();
    }

    /**
     * Return to current type field
     *
     * @return {@link TypeField}
     */
    public TypeField type() {
        return this.type;
    }

    /**
     * Return to `fields`
     *
     * @return {@link List}
     */
    public List<TypeField> types() {
        return this.shapes;
    }

    /**
     * Return to `rule`
     *
     * @return {@link AoRule}
     */
    public AoRule rule() {
        return this.rule;
    }

    /**
     * @return {@link DataFormat}
     */
    public DataFormat format() {
        return this.dataFormat;
    }

}
