package io.vertx.tp.atom.modeling.data;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.tp.atom.cv.em.AttributeType;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.config.AoSource;
import io.vertx.tp.atom.modeling.reference.RQuery;
import io.vertx.tp.atom.modeling.reference.RQuote;
import io.vertx.tp.atom.modeling.reference.RResult;
import io.vertx.tp.atom.modeling.reference.RRule;
import io.vertx.up.eon.em.DataFormat;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ## Reference Calculation
 *
 * ### 1. Intro
 *
 * Reference Calculation based on configuration in each attribute, `serviceReference` field.
 *
 * ### 2. Dao Mode
 *
 * Here are three dao mode in MetaReference
 *
 * 1. Dynamic Dao: {@link io.vertx.tp.modular.dao.AoDao}, based on `M_MODEL` definition.
 * 2. Static Dao: {@link io.vertx.up.uca.jooq.UxJooq}, directly mapped to single table.
 * 3. Static Dao with Join: {@link io.vertx.up.uca.jooq.UxJoin}, mapped to more than on table.
 *
 * ### 3. Dao Map
 *
 * Here are following hash map rules to store component references:
 *
 * 1. Each `key = value` pair refer to `source = {@link io.vertx.tp.atom.modeling.reference.RQuote}`.
 * 2. `references` stored `source = RQuote` hash map.
 * 3. For 「Static」 mode, each {@link io.vertx.tp.atom.modeling.reference.RQuote} stored `condition = RDao`.
 *
 * The example is as following:
 *
 * ```
 * // <pre><code class="shell">
 *     source1: res.employee            ( type = io.vertx.tp.atom.modeling.reference.RQuote )
 *          conditionA = RDao           |-- ( type = io.vertx.tp.atom.modeling.reference.RDao )
 *              -- supportAName
 *              -- supportGroup
 *              -- supportSection
 *          conditionB = RDao           |-- ( type = io.vertx.tp.atom.modeling.reference.RDao )
 *              -- supportBName
 *     source2: rl.device.relation      ( type = io.vertx.tp.atom.modeling.reference.RQuote )
 *          conditionC = AoDao          |-- ( type = io.vertx.tp.atom.modeling.reference.RDao )
 *              -- up
 *          conditionD = AoDao          |-- ( type = io.vertx.tp.atom.modeling.reference.RDao )
 *              -- down
 * // </code></pre>
 * ```
 *
 * > `conditionX` could be calculated by `key` ( include "" default value )
 *
 * ### 4. Result Map
 *
 * Here are following hash map rules to store component result:
 *
 * 1. Each `key = value` pair refer to `field = {@link io.vertx.tp.atom.modeling.reference.RResult}`.
 * 2. `result` stored `source = RResult` hash map and refer to `RDao`.
 *
 * The example is as following:
 *
 * ```
 * // <pre><code class="shell">
 *     ( type = io.vertx.tp.atom.modeling.reference.RResult )
 *     supportAName = RResult
 *     supportBName = RResult
 *     supportGroup = RResult
 *     supportSection = RResult
 *     up = RResult
 *     down = RResult
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class MetaReference {
    /**
     * The hash map to store `source = {@link io.vertx.tp.atom.modeling.reference.RQuote}`.
     */
    private final transient ConcurrentMap<String, RQuote> references
            = new ConcurrentHashMap<>();
    /**
     * The hash map to store `field = {@link io.vertx.tp.atom.modeling.reference.RResult}`.
     */
    private final transient ConcurrentMap<String, RResult> result
            = new ConcurrentHashMap<>();

    private final transient ConcurrentMap<String, Set<String>> diffFieldMap
            = new ConcurrentHashMap<>();

    /*
     * Query Attribute
     */
    private final transient ConcurrentMap<String, RQuery> queries
            = new ConcurrentHashMap<>();

    /**
     * 「Fluent」Build reference metadata information based on `Model`.
     *
     * @param modelRef {@link io.vertx.tp.atom.modeling.Model} Input `M_MODEL` definition.
     * @param appName  {@link java.lang.String} The application name.
     */
    public MetaReference(final Model modelRef, final String appName) {
        /* type = REFERENCE */
        final Set<MAttribute> attributes = modelRef.getAttributes();
        attributes.stream()
                // condition1, Not Null
                .filter(Objects::nonNull)
                // condition2, source is not Null
                .filter(attr -> Objects.nonNull(attr.getSource()))
                /*
                 * condition3, remove self reference to avoid memory out
                 * This condition is critical because of Memory Out Issue of deadLock in reference
                 */
                .filter(attr -> !modelRef.identifier().equals(attr.getSource()))
                // condition4, type = REFERENCE
                .filter(attr -> AttributeType.REFERENCE.name().equals(attr.getType()))
                // Processing workflow on result.
                .forEach(attribute -> {
                    /*
                     *  Hash Map `references` calculation
                     *      - source = RQuote
                     *          - condition1 = RDao
                     *          - condition2 = RDao
                     *  Based on DataAtom reference to create
                     */
                    final String source = attribute.getSource();
                    final AoSource service = new AoSource(attribute);
                    final RQuote quote = Fn.pool(this.references, source, () -> RQuote.create(appName, source)).add(attribute, service);
                    /*
                     *  Hash Map `result` calculation
                     *      - field = RResult
                     *  Based on DataAtom reference to create
                     */
                    final String field = attribute.getName();
                    final RResult result = Fn.pool(this.result, field, () -> new RResult(attribute, service));

                    /*
                     * Qr Engine, stored quote reference map.
                     *
                     * field1 = query1,
                     * field2 = query2
                     */
                    if (DataFormat.Elementary == service.format()) {
                        Fn.pool(this.queries, field,
                                () -> new RQuery(field, attribute.getSourceField())
                                        .bind(quote.dao(field))
                                        .bind(result.joined()));
                    }
                });
        /*
         * DataQuote之后处理
         */
        final ConcurrentMap<String, RRule> rules = new ConcurrentHashMap<>();
        this.references.values().forEach(source -> rules.putAll(source.rules()));

        rules.forEach((field, rule) -> {
            /* Diff fields combine here. */
            final Set<String> diffFields = Ut.toSet(rule.getDiff());
            if (!diffFields.isEmpty()) {
                this.diffFieldMap.put(field, diffFields);
            }
            /* result bind rule for response building. */
            final RResult result = this.result.getOrDefault(field, null);
            if (Objects.nonNull(result)) {
                result.bind(rule);
            }
        });
    }

    ConcurrentMap<String, Set<String>> fieldDiff() {
        return this.diffFieldMap;
    }

    ConcurrentMap<String, RQuote> refInput() {
        return this.references;
    }

    ConcurrentMap<String, RQuery> refQr() {
        return this.queries;
    }

    ConcurrentMap<String, RResult> refOutput() {
        return this.result;
    }

}
