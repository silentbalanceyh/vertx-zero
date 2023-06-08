package io.vertx.up.plugin.jooq;

import io.github.jklingsporn.vertx.jooq.classic.VertxDAO;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.future.PromiseImpl;
import io.vertx.up.exception.booting.JooqClassInvalidException;
import io.vertx.up.exception.booting.JooqVertxNullException;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import org.jooq.Configuration;
import org.jooq.DSLContext;

/**
 * Container to wrap Jooq / VertxDAO
 * 1. Jooq DSL will support sync operation
 * 2. VertxDAO support the async operation
 *
 * The instance counter is as following:
 *
 * 1. 1 vertx instance
 * 2. 1 configuration instance ( 1 context instance )
 * 3. n clazz = Dao instances
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class JooqDsl {
    private static final Annal LOGGER = Annal.get(JooqDsl.class);

    private static final Cc<String, JooqDsl> CC_DSL = Cc.open();
    private transient Vertx vertxRef;
    private transient Configuration configurationRef;
    private transient String poolKey;

    private transient VertxDAO dao;

    private JooqDsl(final String poolKey) {
        this.poolKey = poolKey;
    }

    static JooqDsl init(final Vertx vertxRef, final Configuration configurationRef, final Class<?> daoCls) {
        // Checking when initializing
        Fn.out(!Ut.isImplement(daoCls, VertxDAO.class), JooqClassInvalidException.class, JooqDsl.class, daoCls.getName());
        Fn.outBoot(null == vertxRef, LOGGER, JooqVertxNullException.class, daoCls);

        // Calculate the key of current pool
        final String poolKey = String.valueOf(vertxRef.hashCode()) + ":" +
            String.valueOf(configurationRef.hashCode()) + ":" + daoCls.getName();
        return CC_DSL.pick(() -> new JooqDsl(poolKey).bind(vertxRef, configurationRef).store(daoCls), poolKey);
        // return Fn.po?l(DSL_MAP, poolKey, () -> new JooqDsl(poolKey).bind(vertxRef, configurationRef).store(daoCls));
    }

    private JooqDsl bind(final Vertx vertxRef, final Configuration configurationRef) {
        this.vertxRef = vertxRef;
        this.configurationRef = configurationRef;
        return this;
    }

    private <T> JooqDsl store(final Class<?> daoCls) {
        /*
         * VertxDao initializing
         * Old:
         * final T dao = Ut.instance(clazz, configuration);
         * Ut.invoke(dao, "setVertx", vertxRef);
         */
        final VertxDAO vertxDAO = Ut.instance(daoCls, configurationRef, vertxRef);
        this.dao = vertxDAO;
        return this;
    }

    public String poolKey() {
        return this.poolKey;
    }

    // ----------------------- Metadata

    public DSLContext context() {
        return this.configurationRef.dsl();
    }

    public VertxDAO dao() {
        return this.dao;
    }

    public <T> Future<T> executeBlocking(Handler<Promise<T>> blockingCodeHandler) {
        Promise<T> promise = new PromiseImpl((ContextInternal) this.vertxRef.getOrCreateContext());
        this.vertxRef.executeBlocking(blockingCodeHandler, false, promise);
        return promise.future();
    }
}
