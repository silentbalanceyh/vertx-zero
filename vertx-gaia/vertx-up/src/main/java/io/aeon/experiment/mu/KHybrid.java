package io.aeon.experiment.mu;

import io.aeon.experiment.rule.RuleUnique;
import io.horizon.specification.modeler.HAttribute;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KHybrid implements Serializable {
    // ====================== Data Structure for each Model ===========================
    // The display name of current model
    private final String alias;
    /*
     * RuleUnique of current Model, this field could be serialized/deserialized directly instead of
     * other building method. the default action to build `RuleUnique` is as following:
     *
     *      final String content = this.model.getRuleUnique();
     *      if (Ut.notNil(content)) {
     *          this.unique = Ut.deserialize(content, RuleUnique.class);
     *      }
     *
     * Above code segments could help you to build RuleUnique
     */
    private final RuleUnique unique;
    // ======================= Attribute Level ========================================
    private final ConcurrentMap<String, KReference> referenceMap = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, HAttribute> attributeMap = new ConcurrentHashMap<>();
    private final KMarker marker;

    private KHybrid(final JsonObject hybridJ) {
        // alias / rule / trackable
        this.alias = hybridJ.getString(KName.ALIAS, null);
        final JsonObject ruleJ = Ut.valueJObject(hybridJ, KName.RULE_UNIQUE);
        this.unique = Ut.deserialize(ruleJ, RuleUnique.class);
        final Boolean track = hybridJ.getBoolean(KName.TRACKABLE, Boolean.FALSE);
        this.marker = new KMarker(track);

        // Matrix
        final JsonObject matrixJ = Ut.valueJObject(hybridJ, KName.MATRIX);
        Ut.<String>itJObject(matrixJ, (literal, field) -> this.marker.put(field, literal));

        // References
        final JsonArray referenceA = Ut.valueJArray(hybridJ, KName.REFERENCE);
        Ut.itJArray(referenceA).forEach(referenceJ -> {
            final String name = referenceJ.getString(KName.NAME);
            final String source = referenceJ.getString(KName.SOURCE);
            final String sourceField = referenceJ.getString(KName.SOURCE_FIELD);
            final JsonObject config = Ut.valueJObject(referenceJ, KName.CONFIG);
            final KReference reference = new KReference();
            reference.name(name).source(source).sourceField(sourceField).sourceReference(config);
            if (reference.isReference()) {
                this.referenceMap.put(name, reference);
            }
        });

        // attributes
        final JsonObject attributeJ = Ut.valueJObject(hybridJ, KName.ATTRIBUTE);
        attributeJ.fieldNames().stream().filter(field -> {
            final Object value = attributeJ.getValue(field);
            // PIck Up two Only
            return value instanceof String || value instanceof JsonObject;
        }).forEach(field -> {
            final JsonObject config = new JsonObject();
            // Matrix
            final KTag matrix = this.marker.tag(field);
            // Reference
            final KReference reference = this.referenceMap.get(field);

            // KAttribute
            final Object value = attributeJ.getValue(field);
            if (value instanceof String) {
                config.put(KName.NAME, field);
                config.put(KName.ALIAS, value);
            } else {
                config.put(KName.NAME, field);
                final JsonObject fieldConfig = Ut.valueJObject((JsonObject) value);
                config.mergeIn(fieldConfig, true);
            }
            if (Objects.nonNull(reference)) {
                final JsonObject referenceJ = reference.sourceReference();
                final JsonObject rule = Ut.valueJObject(referenceJ, KName.RULE);
                if (Ut.isNotNil(rule)) {
                    // reference `rule`
                    config.put(KName.RULE, rule);
                }
            }
            final KAttribute attribute = new KAttribute(config, matrix);
            this.attributeMap.put(field, attribute);
        });
    }

    public static KHybrid create(final JsonObject hybridJ) {
        final JsonObject input = Ut.valueJObject(hybridJ);
        return new KHybrid(input);
    }

    public RuleUnique rule() {
        return this.unique;
    }

    public String alias() {
        return this.alias;
    }

    public KMarker marker() {
        return this.marker;
    }

    public ConcurrentMap<String, KReference> reference() {
        return this.referenceMap;
    }

    public ConcurrentMap<String, HAttribute> attribute() {
        return this.attributeMap;
    }
}
