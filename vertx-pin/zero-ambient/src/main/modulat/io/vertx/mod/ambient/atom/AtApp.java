package io.vertx.mod.ambient.atom;

import cn.vertxup.ambient.domain.tables.daos.XAppDao;
import cn.vertxup.ambient.domain.tables.pojos.XApp;
import io.horizon.uca.cache.Cc;
import io.vertx.mod.ambient.error._500AmbientErrorException;
import io.vertx.mod.ambient.error._500ApplicationInitException;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import org.jooq.DSLContext;

@SuppressWarnings("all")
public class AtApp {

    /*
     * DSLContext = AppDao in
     * For multi application usage, each application should has
     * only one AppDao that in to DSLContext.
     */
    private static final Cc<String, AtApp> CC_APP = Cc.open();
    private transient final XApp app;
    private transient DSLContext context;
    private transient XAppDao dao;

    private AtApp(final String name) {
        final UxJooq jooq = Ux.Jooq.on(XAppDao.class);
        Fn.outWeb(null == jooq, _500AmbientErrorException.class, this.getClass());
        /* Current */
        this.app = jooq.fetchOne(KName.NAME, name);
        Fn.outWeb(null == this.app, _500ApplicationInitException.class,
            this.getClass(), name);
    }

    public static AtApp create(final String name) {
        return CC_APP.pick(() -> new AtApp(name), name); // Fn.po?l(Pool.APP_POOL, name, () -> new AtApp(name));
    }

    public XApp getApp() {
        return this.app;
    }
}
