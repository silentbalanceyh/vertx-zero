package io.vertx.tp.atom.modeling.reference;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.error._404ModelNotFoundException;
import io.vertx.tp.ke.atom.metadata.KJoin;
import io.vertx.tp.ke.atom.metadata.KPoint;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.modular.dao.AoDao;
import io.vertx.tp.optic.DS;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.up.commune.Record;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.Strings;
import io.vertx.up.uca.jooq.UxJoin;
import io.vertx.up.uca.jooq.UxJooq;
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
    private transient DataAtom atom;
    /**
     * 「Static」When static source triggered, this reference stored initialized Dao class.
     *
     * The Dao class came from `serviceReference`
     */
    private transient UxJooq jooq;
    /**
     * 「Static」When static source triggered, this reference stored initialized Join Dao class.
     *
     * The Dao class came from 'serviceReference`
     */
    private transient UxJoin join;
    /**
     * 「Static」joinDef in current RDao.
     */
    private transient KJoin kJoin;
    /**
     * Source identifier that has been mapped `source` field of `M_ATTRIBUTE`.
     */
    private final transient String source;

    public RDao(final String appName, final String source) {
        this.source = source;
        try {
            this.atom = DataAtom.get(appName, source);
        } catch (final _404ModelNotFoundException ignored) {
        }
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
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final RDao rDao = (RDao) o;
        return this.source.equals(rDao.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.source);
    }

    public JsonArray fetchBy(final JsonObject condition) {
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
            final AoDao daoD = this.daoD();
            final Record[] records = daoD.fetch(condition);
            return Ut.toJArray(records);
        }
    }

    private Function<JsonObject, JsonArray> executor() {
        return condition -> {
            final JsonObject normalized = condition.copy();
            normalized.remove(Strings.EMPTY);
            if (Ut.isNil(normalized)) {
                return new JsonArray();
            } else {
                final KPoint source = this.kJoin.getSource();
                final KPoint target = this.kJoin.procTarget(condition);
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
            }
        };
    }

    private AoDao daoD() {
        return Ke.channelSync(DS.class, () -> null, ds -> {
            /* 连接池绑定数据库 */
            final DataPool pool = ds.switchDs(this.atom.sigma());
            if (Objects.nonNull(pool)) {
                /* 返回AoDao */
                final Database database = pool.getDatabase();
                return Ao.toDao(database, this.atom);
            } else return null;
        });
    }
}
