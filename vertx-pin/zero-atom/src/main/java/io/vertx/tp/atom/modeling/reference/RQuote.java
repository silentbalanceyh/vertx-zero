package io.vertx.tp.atom.modeling.reference;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.config.AoSource;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ##「Pojo」Quote Definition Object
 *
 * ### 1. Intro
 *
 * Here are basic definition in `X_ATTRIBUTE` for calculation.
 *
 * - source: Defined `identifier` in our environment ( Static Model & Dynamic Model )
 * - sourceField: Defined `field` of critical reference or it will not be used.
 *
 * ### 2. Level
 *
 * Here are mapping relations
 *
 * 1. Quote: Model/Entity level, it provides features to process the whole model.
 * 2. Rule: Attribute/Field level, it provides rules to process attributes/fields only.
 *
 * ### 3. Category
 *
 * When create DataQuote, it's distinguish by `source` field, it means that the `source` could not produce duplicated
 * `sourceField` here.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RQuote implements Serializable {
    /**
     * Bind the identifier of defined Model
     *
     * - Static Model
     * - Dynamic Model
     */
    private transient final DataAtom atomRef;
    /**
     * Stored `attr -> type`
     *
     * In current version, the type support two only:
     * - {@link io.vertx.core.json.JsonObject} for json object.
     * - {@link io.vertx.core.json.JsonArray} for json array.
     */
    private transient final ConcurrentMap<String, Class<?>> typeMap = new ConcurrentHashMap<>();

    /**
     * Stored `attr -> sourceConfig`.
     * This hash map has been reserved, it's for EXTERNAL/INTERNAL
     */
    private transient final ConcurrentMap<String, AoSource> sourceConfig = new ConcurrentHashMap<>();
    /**
     * Stored `attr -> sourceReference`.
     * This hash map is for REFERENCE only in current version.
     */
    private transient final ConcurrentMap<String, JsonObject> sourceReference = new ConcurrentHashMap<>();

    /**
     * Extract `rule` from input json config and build `attr -> DataQRule` of current model.
     */
    private transient final ConcurrentMap<String, RRule> sourceRule = new ConcurrentHashMap<>();


    // ----------------------- Factory Method Start ----------------------

    /**
     * Private constructor to prevent `new ...` creation.
     *
     * @param atomRef The table name or identifier of entity.
     */
    private RQuote(final DataAtom atomRef) {
        this.atomRef = atomRef;
    }

    /**
     * The factory method to create new instance.
     *
     * @param atomRef The table name or identifier of entity.
     *
     * @return {@link RQuote}
     */
    public static RQuote create(final DataAtom atomRef) {
        return new RQuote(atomRef);
    }
    // ----------------------- Factory Method End ----------------------


    /**
     * 「Fluent」Add new attribute in current Quote instance.
     *
     * Here the dao class could be unique, it means that when `source` is fixed, here are only
     * one dao class that has been mapped to current DataQuote
     *
     * DataQuote created by pooled with
     *
     * ```java
     * // <pre><code class="java">
     *     final DataQuote reference = Fn.pool(this.references, source, () -> DataQuote.create(source));
     * // </code></pre>
     * ```
     *
     * > Here are specification that only one source must be unique dao class instead.
     *
     * @param attribute {@link cn.vertxup.atom.domain.tables.pojos.MAttribute} Input attribute object
     *
     * @return {@link RQuote}
     */
    @Fluent
    public RQuote add(final MAttribute attribute) {
        /*
         * JsonArray / JsonObject
         */
        final String name = attribute.getName();
        final AoSource service = new AoSource(attribute);
        this.sourceConfig.put(name, service);
        this.typeMap.put(name, service.type());
        /*
         * sourceReference
         */
        if (Objects.nonNull(attribute.getSourceReference())) {
            final JsonObject sourceReference = Ut.toJObject(attribute.getSourceReference());
            this.sourceReference.put(name, sourceReference);
            /*
             * Json data format, here defined `rule` field and convert to `DataQRule`
             */
            final JsonObject ruleData = Ut.sureJObject(sourceReference.getJsonObject(KeField.RULE));
            if (Ut.notNil(ruleData)) {
                final RRule rule = Ut.deserialize(ruleData, RRule.class);
                this.sourceRule.put(name, rule.type(this.typeMap.get(name)));
            }
            /*
             *
             */
        }
        return this;
    }

    /**
     * @return Hash map of rules
     */
    public ConcurrentMap<String, RRule> rules() {
        return this.sourceRule;
    }

    /**
     * @param field Input attribute name
     *
     * @return Data type of attribute
     */
    public Class<?> type(final String field) {
        return this.typeMap.getOrDefault(field, java.lang.String.class);
    }
}
