package io.vertx.tp.atom.modeling.config;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.cv.em.AttributeType;
import io.vertx.tp.atom.cv.em.FieldSource;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

/**
 * ## 「Pojo」ServiceConfig
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AoSource implements Serializable {
    /**
     * {@link java.lang.Class} is for Elementary processing.
     */
    private final transient Class<?> type;
    /**
     * {@link io.vertx.tp.atom.cv.em.FieldSource} is for fieldSource
     */
    private final transient FieldSource fieldSource;
    /**
     * The `fields` attribute is for {@link io.vertx.core.json.JsonArray} definition.
     */
    private final transient JsonArray fields = new JsonArray();

    /**
     * Create new AoService
     *
     * @param attribute {@link cn.vertxup.atom.domain.tables.pojos.MAttribute} `M_ATTRIBUTE` referred
     */
    public AoSource(final MAttribute attribute) {
        /* type */
        final AttributeType type = Ut.toEnum(attribute::getType, AttributeType.class, AttributeType.INTERNAL);
        final JsonObject sourceConfig = Ut.toJObject(attribute.getSourceConfig());

        /* Three field to calculation */
        final Boolean isArray = Objects.isNull(attribute.getIsArray()) ? Boolean.FALSE : attribute.getIsArray();

        /* sourceConfig must contains configuration. */
        final FieldSource source = Ut.toEnum(() -> sourceConfig.getString(KeField.SOURCE), FieldSource.class, FieldSource.Elementary);

        /*
         * 1. Priority 1: isArray = true, The Data Type is `JsonArray`.
         * 2. Priority 2: isArray must be `false`, get source value.
         */
        final FieldSource fieldService;
        if (isArray) {
            // - JsonArray
            fieldService = FieldSource.JsonArray;
        } else {
            // - JsonObject
            // - Elementary
            fieldService = source;
        }
        /*
         * source value processing.
         *
         * 1. source = JsonArray, JsonArray.class, fields = JsonArray
         * 2. source = JsonObject, JsonObject.class, fields = JsonObject
         * 3. source = Elementary, `Ut.clazz`
         */
        if (FieldSource.Elementary == fieldService) {
            final String dataType = sourceConfig.getString(KeField.TYPE);
            this.type = Ut.clazz(dataType, String.class);
        } else {
            /* fields is only ok when INTERNAL because other types are defined by self */
            if (AttributeType.INTERNAL == type) {
                this.fields.clear();
                this.fields.addAll(sourceConfig.getJsonArray(KeField.FIELDS));
            }
            if (FieldSource.JsonArray == fieldService) {
                this.type = JsonArray.class;
            } else {
                this.type = JsonObject.class;
            }
        }
        this.fieldSource = fieldService;
    }

    /**
     * Return to current data type
     *
     * @return {@link java.lang.Class}
     */
    public Class<?> type() {
        return this.type;
    }

    /**
     * Return to current data type name
     *
     * @return {@link java.lang.String}
     */
    public String typeName() {
        return this.type.getName();
    }

    /**
     * @return {@link io.vertx.tp.atom.cv.em.FieldSource}
     */
    public FieldSource fieldSource() {
        return this.fieldSource;
    }

    /**
     * Return to `fields` data configuration.
     *
     * @return {@link io.vertx.core.json.JsonArray}
     */
    public JsonArray fields() {
        return this.fields;
    }
}
