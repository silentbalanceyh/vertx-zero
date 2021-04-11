package io.vertx.tp.atom.modeling.data;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.tp.atom.cv.em.AttributeType;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.reference.RDao;
import io.vertx.tp.atom.modeling.reference.RQuote;
import io.vertx.tp.atom.modeling.reference.RResult;
import io.vertx.tp.atom.modeling.reference.RRule;
import io.vertx.tp.error._500AtomFirstException;
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

    private transient DataAtom atomRef;

    MetaReference opps(final DataAtom atomRef) {
        this.atomRef = atomRef;
        return this;
    }

    @Deprecated
    private final transient ConcurrentMap<String, RRule> rules
            = new ConcurrentHashMap<>();
    @Deprecated
    private final transient ConcurrentMap<String, Set<String>> ruleDiff
            = new ConcurrentHashMap<>();

    /**
     * 「Fluent」Build reference metadata information based on `Model`.
     *
     * @param modelRef {@link io.vertx.tp.atom.modeling.Model} Input `M_MODEL` definition.
     */
    @Fluent
    public MetaReference bind(final Model modelRef) {
        Fn.out(Objects.isNull(this.atomRef), _500AtomFirstException.class, this.getClass(), modelRef.identifier());
        /* type = REFERENCE */
        final Set<MAttribute> attributes = modelRef.getAttributes();
        attributes.stream()
                // condition1, Not Null
                .filter(Objects::nonNull)
                // condition2, source is not Null
                .filter(attr -> Objects.nonNull(attr.getSource()))
                // condition3, type = REFERENCE
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
                    final DataAtom another = this.atomRef.getAnother(source);
                    Fn.pool(this.references, source, () -> RQuote.create(another)).add(attribute);


                    /*
                     *  Hash Map `result` calculation
                     *      - field = RResult
                     *  Based on DataAtom reference to create
                     */
                    final String field = attribute.getName();
                    Fn.pool(this.result, field, RResult::new).add(attribute);
                });
        /*
         * DataQuote之后处理
         */
        this.references.values().forEach(source -> {
            /* Rules added */
            this.rules.putAll(source.rules());
            /* Source mount */
        });

        this.rules.forEach((field, rule) -> {
            final Set<String> diffFields = Ut.toSet(rule.getDiff());
            if (!diffFields.isEmpty()) {
                this.ruleDiff.put(field, diffFields);
            }
        });
        return this;
    }


    /*
     * 模板中增加引用
     * source -> DataQuote
     */
    ConcurrentMap<String, RRule> rules() {
        return this.rules;
    }

    ConcurrentMap<String, Set<String>> ruleDiff() {
        return this.ruleDiff;
    }

    ConcurrentMap<RDao, RQuote> references(final String appName) {
        final ConcurrentMap<RDao, RQuote> switched = new ConcurrentHashMap<>();
        this.references.forEach((source, quote) -> {
            /* DataAtom 交换 */
            final RDao qKey = new RDao(appName, source);
            qKey.connect(quote);
            switched.put(qKey, quote);
        });
        return switched;
    }
}
