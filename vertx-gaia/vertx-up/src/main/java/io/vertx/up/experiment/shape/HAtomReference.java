package io.vertx.up.experiment.shape;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.DataFormat;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.experiment.mixture.HAttribute;
import io.vertx.up.experiment.mixture.HDao;
import io.vertx.up.experiment.mixture.HReference;
import io.vertx.up.experiment.mu.KReference;
import io.vertx.up.experiment.reference.RDao;
import io.vertx.up.experiment.reference.RQuery;
import io.vertx.up.experiment.reference.RQuote;
import io.vertx.up.experiment.reference.RResult;
import io.vertx.up.experiment.specification.KJoin;
import io.vertx.up.experiment.specification.KPoint;
import io.vertx.up.experiment.specification.power.KApp;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.uca.cache.Cd;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
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
 * 2. Static Dao: directly mapped to single table.
 * 3. Static Dao with Join: mapped to more than on table.
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
public class HAtomReference implements HReference {
    protected final transient Cc<String, RDao> ccDao = Cc.open();
    /**
     * The hash map to store `source = {@link RQuote}`.
     */
    protected final transient Cc<String, RQuote> ccReference = Cc.open();
    /**
     * The hash map to store `field = {@link RResult}`.
     */
    protected final transient Cc<String, RResult> ccResult = Cc.open();
    /*
     * Query Attribute
     */
    protected final transient Cc<String, RQuery> ccQuery = Cc.open();

    protected final transient KApp app;

    private final transient Cc<String, KReference> ccData = Cc.open();

    public HAtomReference(final KApp app) {
        this.app = app;
    }

    // ======================= Overwrite Api ==========================
    @Override
    public ConcurrentMap<String, RQuote> refInput() {
        final Cd<String, RQuote> store = this.ccReference.store();
        return store.data();
    }

    @Override
    public ConcurrentMap<String, RQuery> refQr() {
        final Cd<String, RQuery> store = this.ccQuery.store();
        return store.data();
    }

    @Override
    public ConcurrentMap<String, RResult> refOutput() {
        final Cd<String, RResult> store = this.ccResult.store();
        return store.data();
    }

    @Override
    public KReference refData(final String name) {
        return this.ccData.store(name);
    }

    // ======================= Overwrite Api ==========================
    protected void initializeReference(final KReference input, final HAttribute hAttribute) {
        if (input.isReference()) {
            final KReference reference = this.ccData.pick(() -> input, input.name());
            final String source = reference.source();

            final RDao dao = this.initializeDao(reference);
            /*
             * RDao initialize and unlink
             */
            final RQuote quote = this.ccReference.pick(() -> RQuote.create(this.app.name()), source);
            // Fn.po?l(this.references, source, () -> RQuote.create(appName));
            final JsonObject referenceConfig = reference.sourceReference();
            quote.add(hAttribute, referenceConfig, dao);
            /*
             *  Hash Map `result` calculation
             *      - field = RResult
             *  Based on DataAtom reference to create
             */
            final String field = reference.name();
            final String referenceField = reference.sourceField();
            final RResult result = this.ccResult.pick(() -> new RResult(referenceField, referenceConfig, hAttribute), field);
            // Fn.po?l(this.result, field, () -> new RResult(referenceField, referenceConfig, hAttribute));
            /*
             * Qr Engine, stored quote reference map.
             *
             * field1 = query1,
             * field2 = query2
             */
            if (DataFormat.Elementary == hAttribute.format()) {
                this.ccQuery.pick(() -> new RQuery(field, reference.sourceField())
                    .bind(quote.dao(field))
                    .bind(result.joined()), field);
                // Fn.po?l(this.queries, field, () -> new RQuery(field, attribute.getSourceField()).bind(quote.dao(field)).bind(result.joined()));
            }
        }
    }

    protected HDao toDao(final HAtom atom) {
        // Could not call this api when it's not overwrite ( Dynamic Only )
        throw new _501NotSupportException(this.getClass());
    }

    protected HAtom toAtom(final String identifier) {
        /*
         * The default operation return to null, it means that it's not
         * dynamic Dao here, in this kind of situation, the following api will be ignored:
         *
         * HDao toDao(HAtom);
         *
         * The code logical will skip `HDao` building
         */
        return null;
    }

    // ======================= Private Api ==========================
    private RDao initializeDao(final KReference reference) {
        final String source = reference.source();
        final HAtom atom = this.toAtom(source);
        final JsonObject sourceReference = reference.sourceReference();
        // KJoin Building
        final KJoin join;
        if (Objects.isNull(atom) && sourceReference.containsKey(KName.DAO)) {
            join = Ut.deserialize(sourceReference.getJsonObject(KName.DAO), KJoin.class);
        } else {
            join = null;
        }
        final Function<JsonObject, Future<JsonArray>> actionA = this.actionA(atom, join);
        final Function<JsonObject, JsonArray> actionS = this.actionS(atom, join);
        // RDao Creation
        final RDao dao = this.ccDao.pick(() -> new RDao(source), this.app.name() + "/" + source);
        // Fn.po?l(this.sourceDao, appName + "/" + source, () -> new RDao(source));
        return dao.actionA(actionA).actionS(actionS);
    }

    private Function<JsonObject, Future<JsonArray>> actionA(final HAtom atom, final KJoin join) {
        return condition -> {
            if (Ux.irNil(condition)) {
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
                final HDao dao = this.toDao(atom);
                return dao.fetchAsync(condition).compose(Ux::futureA);
            }
        };
    }

    private Function<JsonObject, JsonArray> actionS(final HAtom atom, final KJoin join) {
        return condition -> {
            if (Ux.irNil(condition)) {
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
                final HDao dao = this.toDao(atom);
                return Ut.toJArray(dao.fetch(condition));
            }
        };
    }
}
