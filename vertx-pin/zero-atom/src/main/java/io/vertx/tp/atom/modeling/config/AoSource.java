package io.vertx.tp.atom.modeling.config;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.cv.em.AttributeType;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.eon.em.DataFormat;
import io.vertx.up.util.Ut;

import java.io.Serializable;
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
public class AoSource implements Serializable {
    /**
     * {@link java.lang.Class} is for Elementary processing.
     */
    private final transient Class<?> type;
    /**
     * {@link DataFormat} is for fieldSource
     */
    private final transient DataFormat dataFormat;
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
        final DataFormat source = Ut.toEnum(() -> sourceConfig.getString(KeField.FORMAT), DataFormat.class, DataFormat.Elementary);

        /*
         * 1. Priority 1: isArray = true, The Data Type is `JsonArray`.
         * 2. Priority 2: isArray must be `false`, get source value.
         */
        final DataFormat fieldService;
        if (isArray) {
            // - JsonArray
            fieldService = DataFormat.JsonArray;
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
        if (DataFormat.Elementary == fieldService) {
            final String dataType = sourceConfig.getString(KeField.TYPE);
            this.type = Ut.clazz(dataType, String.class);
        } else {
            /* fields is only ok when INTERNAL because other types are defined by self */
            if (AttributeType.INTERNAL == type) {
                this.fields.clear();
                this.fields.addAll(Ut.sureJArray(sourceConfig.getJsonArray(KeField.FIELDS)));
            }
            if (DataFormat.JsonArray == fieldService) {
                this.type = JsonArray.class;
            } else {
                this.type = JsonObject.class;
            }
        }
        this.dataFormat = fieldService;
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
     * @return {@link DataFormat}
     */
    public DataFormat format() {
        return this.dataFormat;
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
