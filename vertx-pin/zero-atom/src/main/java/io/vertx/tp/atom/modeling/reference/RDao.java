package io.vertx.tp.atom.modeling.reference;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.commune.Record;
import io.vertx.up.experiment.meld.HDao;
import io.vertx.up.experiment.specification.KJoin;
import io.vertx.up.experiment.specification.KPoint;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Function;

/**
 * ##「Pojo」DataAtom Key Replaced
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RDao {
    /**
     * 「Dynamic」When dynamic source triggered, this reference stored model definition in `M_MODEL / M_ATTRIBUTE`
     */
    private final transient DataAtom atom;
    /**
     * Source identifier that has been mapped `source` field of `M_ATTRIBUTE`.
     */
    private final transient String source;
    /**
     * 「Static」joinDef in current RDao.
     */
    private transient KJoin kJoin;

    public RDao(final String appName, final String source) {
        this.source = source;
        this.atom = DataAtom.get(appName, source);
    }

    public DataAtom atom() {
        return this.atom;
    }

    public String source() {
        return this.source;
    }

    public boolean isStatic() {
        return Objects.isNull(this.atom);
    }

    @Fluent
    public RDao bind(final KJoin kJoin) {
        if (Objects.nonNull(kJoin) && Objects.isNull(this.atom)) {
            this.kJoin = kJoin;
        }
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final RDao rDao = (RDao) o;
        return this.source.equals(rDao.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.source);
    }

    public JsonArray fetchBy(final JsonObject condition) {
        if (Ux.Jooq.isEmpty(condition)) {
            return new JsonArray();
        } else {
            if (this.isStatic()) {
                /*
                 * Static
                 */
                final Function<JsonObject, JsonArray> executor = this.executor();
                return executor.apply(condition);
            } else {
                /*
                 * Dynamic
                 */
                final HDao daoD = this.daoD();
                final Record[] records = daoD.fetch(condition);
                return Ut.toJArray(records);
            }
        }
    }

    public Future<JsonArray> fetchByAsync(final JsonObject condition) {
        if (Ux.Jooq.isEmpty(condition)) {
            return Ux.futureA();
        } else {
            if (this.isStatic()) {
                /*
                 * Static
                 */
                final Function<JsonObject, Future<JsonArray>> executor = this.executorAsync();
                return executor.apply(condition);
            } else {
                /*
                 * Dynamic
                 */
                final HDao daoD = this.daoD();
                return daoD.fetchAsync(condition)
                    .compose(Ux::futureA);
            }
        }
    }

    private Function<JsonObject, JsonArray> executor() {
        return condition -> {
            final KPoint source = this.kJoin.getSource();
            final KPoint target = this.kJoin.point(condition);
            if (Objects.isNull(target)) {
                return Ux.Jooq.on(source.getClassDao()).fetchJ(condition);
            } else {
                return Ux.Join.on()
                    /*
                     * Join source / target.
                     */
                    .add(source.getClassDao(), source.getKeyJoin())
                    .join(target.getClassDao(), target.getKeyJoin())
                    .fetch(condition);
            }
        };
    }

    private Function<JsonObject, Future<JsonArray>> executorAsync() {
        return condition -> {
            final KPoint source = this.kJoin.getSource();
            final KPoint target = this.kJoin.point(condition);
            if (Objects.isNull(target)) {
                return Ux.Jooq.on(source.getClassDao()).fetchJAsync(condition);
            } else {
                return Ux.Join.on()
                    /*
                     * Join source / target.
                     */
                    .add(source.getClassDao(), source.getKeyJoin())
                    .join(target.getClassDao(), target.getKeyJoin())
                    .fetchAsync(condition);
            }
        };
    }

    private HDao daoD() {
        return Ao.toDao(this.atom);
    }
}
