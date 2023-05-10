package io.modello.atom.normalize;

import io.horizon.eon.VName;
import io.horizon.util.HUt;
import io.modello.eon.em.AttributeMarker;
import io.modello.eon.em.ValueFormat;
import io.modello.specification.atom.HAttribute;
import io.modello.specification.meta.HMetaField;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KAttribute implements HAttribute, Serializable {
    private final ValueFormat format;

    private final List<HMetaField> shapes = new ArrayList<>();

    private final HMetaField type;
    private final KMarkAttribute tag;
    private RRule rule;

    /*
     * Data Structure of Matrix
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
    public KAttribute(final JsonObject config, final KMarkAttribute tag) {
        this.tag = tag;
        /*
         * Extract DataFormat from `format` field in config，
         * Here are format adjustment:
         * 1. Priority 1: isArray = true, the format is `JsonArray`.
         * 2. Priority 2: isArray = false, set the default value instead ( Elementary )
         */
        ValueFormat format = HUt.toEnum(() -> config.getString(VName.FORMAT), ValueFormat.class, ValueFormat.Elementary);
        if (tag.value(AttributeMarker.array)) {
            format = ValueFormat.JsonArray;
        }
        this.format = format;

        /*
         * Here the type must be fixed or null
         */
        final Class<?> type = HUt.clazz(config.getString(VName.TYPE), String.class);
        final String name = config.getString(VName.NAME);
        final String alias = config.getString(VName.ALIAS);
        this.type = HMetaField.of(name, alias, type);

        /*
         * Format is not elementary, expand the `fields` lookup range
         * instead of simple, then add children into HTField for complex
         */
        if (ValueFormat.Elementary != format) {
            final JsonArray fields = HUt.valueJArray(config.getJsonArray(VName.FIELDS));
            HUt.itJArray(fields).forEach(item -> {
                final String field = item.getString(VName.FIELD);
                if (HUt.isNotNil(field)) {
                    final String fieldAlias = item.getString(VName.ALIAS, null);
                    final Class<?> subType = HUt.clazz(item.getString(VName.TYPE), String.class);
                    this.shapes.add(HMetaField.of(field, fieldAlias, subType));
                }
            });
            this.type.add(this.shapes);
        }

        /*
         * Bind `rule` processing, the `rule` should be configured in config instead of
         */
        if (config.containsKey(VName.RULE)) {
            final JsonObject ruleJ = HUt.valueJObject(config, VName.RULE);
            this.rule = HUt.deserialize(ruleJ, RRule.class);
            /* Bind type into rule */
            this.rule.type(this.type.type());
            /* Unique rule for diffSet */
            this.type.key(this.rule.getUnique());
            // this.type.?uleUnique(this.rule.getUnique());
        }
    }

    @Override
    public RRule referenceRule() {
        return this.rule;
    }

    @Override
    public KMarkAttribute marker() {
        return this.tag;
    }

    @Override
    public ValueFormat format() {
        return this.format;
    }

    @Override
    public HMetaField field() {
        return this.type;
    }

    @Override
    public List<HMetaField> fieldCompiled() {
        return this.shapes;
    }
}
