package io.vertx.tp.plugin.jooq;

import io.github.jklingsporn.vertx.jooq.classic.VertxDAO;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import org.jooq.Table;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class JooqMeta {
    // Unique class name in whole environment
    private static final ConcurrentMap<Class<?>, JooqMeta> META_MAP = new ConcurrentHashMap<>();
    // Dao Class
    private transient Class<?> daoCls;
    private transient VertxDAO vertxDAO;
    private transient Table<?> table;
    private transient Class<?> type;

    private JooqMeta(final VertxDAO vertxDAO, final Class<?> daoCls) {
        Objects.requireNonNull(vertxDAO);
        this.vertxDAO = vertxDAO;
        this.daoCls = daoCls;

        this.table = Ut.field(vertxDAO, "table");
        this.type = Ut.field(vertxDAO, "type");
    }

    static JooqMeta create(final VertxDAO vertxDAO, final Class<?> daoCls) {
        Objects.requireNonNull(daoCls);
        return Fn.pool(META_MAP, daoCls, () -> new JooqMeta(vertxDAO, daoCls));
    }

    Table<?> table() {
        return this.table;
    }

    Class<?> type() {
        return this.type;
    }

    VertxDAO dao() {
        return this.vertxDAO;
    }
}