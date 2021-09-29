package io.vertx.tp.ambient.atom;

import cn.vertxup.ambient.domain.tables.daos.XAppDao;
import cn.vertxup.ambient.domain.tables.pojos.XApp;
import io.vertx.tp.error._500AmbientErrorException;
import io.vertx.tp.error._500ApplicationInitException;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import org.jooq.DSLContext;

@SuppressWarnings("all")
public class AtApp {

    private transient final XApp app;
    private transient DSLContext context;
    private transient XAppDao dao;

    // TODO: NEW VERSION
    private AtApp(final DSLContext context,
                  final String name) {
        Fn.outWeb(null == context, _500AmbientErrorException.class, this.getClass());
        /* Dao initialized */
        this.context = context;

        this.dao = null;// new XAppDao(context.configuration());
        /* Context */
        this.app = null; // this.dao.fetchOneByName(name);
        Fn.outWeb(null == this.app, _500ApplicationInitException.class,
            this.getClass(), name);
    }

    private AtApp(final String name) {
        final UxJooq jooq = Ux.Jooq.on(XAppDao.class);
        Fn.outWeb(null == jooq, _500AmbientErrorException.class, this.getClass());
        /* Current */
        this.app = jooq.fetchOne(KName.NAME, name);
        Fn.outWeb(null == this.app, _500ApplicationInitException.class,
            this.getClass(), name);
    }


    public static AtApp create(final DSLContext context,
                               final String name) {
        final String key = name + context.hashCode();
        return Fn.pool(Pool.APP_POOL, key, () -> new AtApp(context, name));
    }

    public static AtApp create(final String name) {
        return Fn.pool(Pool.APP_POOL, name, () -> new AtApp(name));
    }

    public XApp getApp() {
        return this.app;
    }
}
