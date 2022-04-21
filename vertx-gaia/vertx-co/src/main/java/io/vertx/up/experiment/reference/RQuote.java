package io.vertx.up.experiment.reference;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;
import io.vertx.up.experiment.mixture.HAttribute;
import io.vertx.up.experiment.mixture.HRule;
import io.vertx.up.util.Ut;

import java.io.Serializable;
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
@SuppressWarnings("all")
public class RQuote implements Serializable {
    /**
     * The application name for DataAtom create.
     */
    private final String appName;
    /**
     * Stored `attr -> type`
     *
     * In current version, the type support two only:
     * - {@link io.vertx.core.json.JsonObject} for json object.
     * - {@link io.vertx.core.json.JsonArray} for json array.
     */
    private final ConcurrentMap<String, Class<?>> typeMap = new ConcurrentHashMap<>();

    /**
     * Stored `attr -> sourceConfig`.
     * This hash map has been reserved, it's for EXTERNAL/INTERNAL
     */
    private final ConcurrentMap<String, HAttribute> sourceConfig = new ConcurrentHashMap<>();
    /**
     * Stored `attr -> sourceReference`.
     * This hash map is for REFERENCE only in current version.
     */
    private final ConcurrentMap<String, JsonObject> sourceReference = new ConcurrentHashMap<>();

    /**
     * Extract `rule` from input json config and build `attr -> DataQRule` of current model.
     */
    private final ConcurrentMap<String, HRule> sourceRule = new ConcurrentHashMap<>();
    /**
     * Extract `dao` by condition `hashCode()`.
     */
    private final ConcurrentMap<String, RDao> sourceDao = new ConcurrentHashMap<>();


    // ----------------------- Factory Method Start ----------------------

    /**
     * Private constructor to prevent `new ...` creation.
     *
     * @param appName {@link java.lang.String} application name.
     */
    private RQuote(final String appName) {
        this.appName = appName;
    }

    /**
     * The factory method to create new instance.
     *
     * @param appName {@link java.lang.String} application name.
     *
     * @return {@link RQuote}
     */
    public static RQuote create(final String appName) {
        return new RQuote(appName);
    }
    // ----------------------- Factory Method End ----------------------

    public String app() {
        return this.appName;
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
     * @param referenceConfig {@link JsonObject} Input attribute object
     * @param service         {@link HAttribute}
     * @param dao             {@link RDao}
     *
     * @return {@link RQuote}
     */
    @Fluent
    public RQuote add(final HAttribute service, final JsonObject referenceConfig,
                      final RDao dao) {
        /*
         * JsonArray / JsonObject
         */
        final String name = service.field().name();
        this.sourceConfig.put(name, service);
        this.typeMap.put(name, service.field().type());
        /*
         * sourceReference
         */
        if (Ut.notNil(referenceConfig)) {
            final JsonObject sourceReference = referenceConfig.copy();
            this.sourceReference.put(name, sourceReference);
            /*
             * Json data format, here defined `rule` field and convert to `DataQRule`
             */
            final HRule rule = service.rule();
            this.sourceRule.put(name, rule);
            /*
             * RDao put
             */
            this.sourceDao.put(rule.keyDao(), dao);
        }
        return this;
    }

    /**
     * @return Hash map of rules
     */
    public ConcurrentMap<String, HRule> rules() {
        return this.sourceRule;
    }

    public HRule rule(final String field) {
        return this.sourceRule.getOrDefault(field, null);
    }

    /**
     * @param field {@link java.lang.String} Input attribute name.
     *
     * @return {@link RDao}
     */
    public RDao dao(final String field) {
        final HRule rule = this.sourceRule.getOrDefault(field, null);
        return this.sourceDao.getOrDefault(rule.keyDao(), null);
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
