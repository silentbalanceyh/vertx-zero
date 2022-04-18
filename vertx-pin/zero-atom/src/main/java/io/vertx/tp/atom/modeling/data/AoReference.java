package io.vertx.tp.atom.modeling.data;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.DataFormat;
import io.vertx.up.eon.em.atom.AttributeType;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.experiment.mixture.HAttribute;
import io.vertx.up.experiment.mixture.HDao;
import io.vertx.up.experiment.mixture.HReference;
import io.vertx.up.experiment.reference.RDao;
import io.vertx.up.experiment.reference.RQuery;
import io.vertx.up.experiment.reference.RQuote;
import io.vertx.up.experiment.reference.RResult;
import io.vertx.up.experiment.specification.KJoin;
import io.vertx.up.experiment.specification.KPoint;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

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
 * 1. Dynamic Dao: {@link HDao}, based on `M_MODEL` definition.
 * 2. Static Dao: {@link io.vertx.up.uca.jooq.UxJooq}, directly mapped to single table.
 * 3. Static Dao with Join: {@link io.vertx.up.uca.jooq.UxJoin}, mapped to more than on table.
 *
 * ### 3. Dao Map
 *
 * Here are following hash map rules to store component references:
 *
 * 1. Each `key = value` pair refer to `source = {@link RQuote}`.
 * 2. `references` stored `source = RQuote` hash map.
 * 3. For 「Static」 mode, each {@link RQuote} stored `condition = RDao`.
 *
 * The example is as following:
 *
 * ```
 * // <pre><code class="shell">
 *     source1: res.employee            ( type = io.vertx.up.experiment.reference.RQuote )
 *          conditionA = RDao           |-- ( type = io.vertx.up.experiment.reference.RDao )
 *              -- supportAName
 *              -- supportGroup
 *              -- supportSection
 *          conditionB = RDao           |-- ( type = io.vertx.up.experiment.reference.RDao )
 *              -- supportBName
 *     source2: rl.device.relation      ( type = io.vertx.up.experiment.reference.RQuote )
 *          conditionC = AoDao          |-- ( type = io.vertx.up.experiment.reference.RDao )
 *              -- up
 *          conditionD = AoDao          |-- ( type = io.vertx.up.experiment.reference.RDao )
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
 * 1. Each `key = value` pair refer to `field = {@link RResult}`.
 * 2. `result` stored `source = RResult` hash map and refer to `RDao`.
 *
 * The example is as following:
 *
 * ```
 * // <pre><code class="shell">
 *     ( type = io.vertx.up.experiment.reference.RResult )
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
class AoReference implements HReference {
    private final transient ConcurrentMap<String, RDao> sourceDao = new ConcurrentHashMap<>();
    /**
     * The hash map to store `source = {@link RQuote}`.
     */
    private final transient ConcurrentMap<String, RQuote> references
        = new ConcurrentHashMap<>();
    /**
     * The hash map to store `field = {@link RResult}`.
     */
    private final transient ConcurrentMap<String, RResult> result
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
    public AoReference(final Model modelRef, final String appName) {
        /* type = REFERENCE */
        final Set<MAttribute> attributes = modelRef.dbAttributes();
        attributes.stream()
            // condition1, Not Null
            .filter(Objects::nonNull)
            // condition2, source is not Null
            .filter(attr -> Objects.nonNull(attr.getSource()))
            /*
             * condition3, remove self reference to avoid memory out
             * This condition is critical because of Memory Out Issue of deadLock in reference
             * Current model identifier must not be `source` because it will trigger
             * Self deal lock here. To avoid this kind of situation, filtered this item.
             */
            .filter(attr -> !modelRef.identifier().equals(attr.getSource()))
            // condition4, type = REFERENCE
            // .filter(attr -> AttributeType.REFERENCE.name().equals(attr.getType()))
            // Processing workflow on result.
            .forEach(attribute -> {
                /*
                 *  Hash Map `references` calculation
                 *      - source = RQuote
                 *          - condition1 = RDao
                 *          - condition2 = RDao
                 *  Based on DataAtom reference to create
                 */
                final AttributeType type = Ut.toEnum(attribute::getType, AttributeType.class, AttributeType.INTERNAL);

                if (AttributeType.REFERENCE == type) {
                    /*
                     * Reference Initializing
                     */
                    final HAttribute aoAttr = modelRef.attribute(attribute.getName());
                    this.initializeReference(attribute, aoAttr, appName);
                }
            });
    }

    private void initializeReference(final MAttribute attribute, final HAttribute hAttribute, final String appName) {
        final String source = attribute.getSource();

        final JsonObject referenceConfig = Ut.toJObject(attribute.getSourceReference());

        if (Ut.notNil(referenceConfig)) {

            final RDao dao = this.initializeDao(attribute, appName, referenceConfig);
            /*
             * RDao initialize and unlink
             */
            final RQuote quote = Fn.pool(this.references, source, () -> RQuote.create(appName));

            quote.add(hAttribute, referenceConfig, dao);
            /*
             *  Hash Map `result` calculation
             *      - field = RResult
             *  Based on DataAtom reference to create
             */
            final String field = attribute.getName();
            final String referenceField = attribute.getSourceField();
            final RResult result = Fn.pool(this.result, field, () -> new RResult(referenceField, referenceConfig, hAttribute));
            /*
             * Qr Engine, stored quote reference map.
             *
             * field1 = query1,
             * field2 = query2
             */
            if (DataFormat.Elementary == hAttribute.format()) {
                Fn.pool(this.queries, field, () -> new RQuery(field, attribute.getSourceField()).bind(quote.dao(field)).bind(result.joined()));
            }
        }
    }

    private RDao initializeDao(final MAttribute attribute, final String appName, final JsonObject referenceConfig) {
        final String source = attribute.getSource();
        final HAtom atom = DataAtom.get(appName, source);
        final KJoin join;
        if (Objects.isNull(atom) && referenceConfig.containsKey(KName.DAO)) {
            join = Ut.deserialize(referenceConfig.getJsonObject(KName.DAO), KJoin.class);
        } else {
            join = null;
        }
        final Function<JsonObject, Future<JsonArray>> actionA = this.actionA(atom, join);
        final Function<JsonObject, JsonArray> actionS = this.actionS(atom, join);
        // RDao Creation
        final RDao dao = Fn.pool(this.sourceDao, appName + "/" + source, () -> new RDao(source));
        return dao.actionA(actionA).actionS(actionS);
    }

    private Function<JsonObject, Future<JsonArray>> actionA(final HAtom atom, final KJoin join) {
        return condition -> {
            if (Ux.Jooq.isEmpty(condition)) {
                return Ux.futureA();
            }
            if (Objects.isNull(atom)) {
                Objects.requireNonNull(join);
                final KPoint source = join.getSource();
                final KPoint target = join.point(condition);
                // Static
                if (Objects.isNull(target)) {
                    // Jooq
                    return Ux.Jooq.on(source.getClassDao()).fetchJAsync(condition);
                } else {
                    // Join
                    return Ux.Join.on()
                        .add(source.getClassDao(), source.getKeyJoin())
                        .join(target.getClassDao(), target.getKeyJoin())
                        .fetchAsync(condition);
                }
            } else {
                // Dynamic
                final HDao dao = Ao.toDao(atom);
                return dao.fetchAsync(condition).compose(Ux::futureA);
            }
        };
    }

    private Function<JsonObject, JsonArray> actionS(final HAtom atom, final KJoin join) {
        return condition -> {
            if (Ux.Jooq.isEmpty(condition)) {
                return new JsonArray();
            }
            if (Objects.isNull(atom)) {
                Objects.requireNonNull(join);
                final KPoint source = join.getSource();
                final KPoint target = join.point(condition);
                // Static
                if (Objects.isNull(target)) {
                    // Jooq
                    return Ux.Jooq.on(source.getClassDao()).fetchJ(condition);
                } else {
                    // Join
                    return Ux.Join.on()
                        .add(source.getClassDao(), source.getKeyJoin())
                        .join(target.getClassDao(), target.getKeyJoin())
                        .fetch(condition);
                }
            } else {
                // Dynamic
                final HDao dao = Ao.toDao(atom);
                return Ut.toJArray(dao.fetch(condition));
            }
        };
    }


    @Override
    public ConcurrentMap<String, RQuote> refInput() {
        return this.references;
    }

    @Override
    public ConcurrentMap<String, RQuery> refQr() {
        return this.queries;
    }

    @Override
    public ConcurrentMap<String, RResult> refOutput() {
        return this.result;
    }

}
