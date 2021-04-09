package io.vertx.tp.atom.modeling.reference;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._409ReferenceDaoException;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.fn.Fn;
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
public class DataQuote implements Serializable {
    /**
     * Bind the identifier of defined Model
     *
     * - Static Model
     * - Dynamic Model
     */
    private transient final String source;
    /**
     * Bind the daoCls of defined Model of static.
     *
     * - Static Model
     * - Dynamic Model
     */
    private transient Class<?> daoCls;
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
    private transient final ConcurrentMap<String, JsonObject> sourceConfig = new ConcurrentHashMap<>();
    /**
     * Stored `attr -> sourceReference`.
     * This hash map is for REFERENCE only in current version.
     */
    private transient final ConcurrentMap<String, JsonObject> sourceReference = new ConcurrentHashMap<>();

    /**
     * Extract `rule` from input json config and build `attr -> DataQRule` of current model.
     */
    private transient final ConcurrentMap<String, DataQRule> sourceRule = new ConcurrentHashMap<>();

    /**
     * Private constructor to prevent `new ...` creation.
     *
     * @param source The table name or identifier of entity.
     */
    private DataQuote(final String source) {
        this.source = source;
    }

    /**
     * The factory method to create new instance.
     *
     * @param source The table name or identifier of entity.
     *
     * @return {@link io.vertx.tp.atom.modeling.reference.DataQuote}
     */
    public static DataQuote create(final String source) {
        return new DataQuote(source);
    }

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
     * @return {@link io.vertx.tp.atom.modeling.reference.DataQuote}
     */
    @Fluent
    public DataQuote add(final MAttribute attribute) {
        /*
         * JsonArray / JsonObject
         */
        final Boolean isArray = attribute.getIsArray();
        final String name = attribute.getName();
        if (Objects.nonNull(isArray) && isArray) {
            this.typeMap.put(name, JsonArray.class);
        } else {
            this.typeMap.put(name, JsonObject.class);
        }
        /*
         * sourceConfig
         */
        if (Objects.nonNull(attribute.getSourceConfig())) {
            final JsonObject sourceConfig = Ut.toJObject(attribute.getSourceConfig());
            this.sourceConfig.put(name, sourceConfig);
        }
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
                final DataQRule rule = Ut.deserialize(ruleData, DataQRule.class);
                this.sourceRule.put(name, rule.type(this.typeMap.get(name)));
            }
            /*
             * daoCls null
             */
            if (sourceReference.containsKey("dao")) {
                final String daoCls = sourceReference.getString("dao");
                if (Objects.isNull(this.daoCls)) {
                    this.daoCls = Ut.clazz(daoCls);
                } else {
                    /*
                     * Ensure this.daoCls and sourceReference.dao
                     * When duplicated, throw out _409ReferenceDaoException class
                     */
                    Fn.out(!daoCls.equals(this.daoCls.getName()), _409ReferenceDaoException.class, this.getClass(), this.source, attribute.getName());
                }
            }
        }
        return this;
    }

    /**
     * @return Hash map of rules
     */
    public ConcurrentMap<String, DataQRule> rules() {
        return this.sourceRule;
    }

    /**
     * @param field Input attribute name
     *
     * @return Data type of attribute
     */
    public Class<?> type(final String field) {
        return this.typeMap.getOrDefault(field, null);
    }

    /**
     * @return {@link java.lang.Class}
     */
    public Class<?> typeDao() {
        return this.daoCls;
    }
}
