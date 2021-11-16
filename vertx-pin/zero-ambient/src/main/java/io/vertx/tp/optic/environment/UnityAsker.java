package io.vertx.tp.optic.environment;

import cn.vertxup.ambient.domain.tables.daos.XAppDao;
import cn.vertxup.ambient.domain.tables.daos.XSourceDao;
import cn.vertxup.ambient.domain.tables.pojos.XApp;
import cn.vertxup.ambient.domain.tables.pojos.XSource;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.tp.ambient.cv.AtMsg;
import io.vertx.tp.ambient.refine.At;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.jooq.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class UnityAsker {

    private static final Annal LOGGER = Annal.get(UnityAsker.class);

    private static final ConcurrentMap<String, XApp> APP_POOL =
        new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, XSource> SOURCE_POOL =
        new ConcurrentHashMap<>();

    /*
     * Be careful, in this step there is no Vert.x instance available, it means that you must
     * set Jooq environment before vert.x begin up, in this situation, here you couldn't
     * use `Ux.Jooq.on(Dao.class)` mode to get Dao reference.
     */
    static Future<Boolean> init(final Vertx vertx) {
        /* All app here */
        final Configuration configuration = Ke.getConfiguration();
        final XAppDao appDao = new XAppDao(configuration, vertx);
        return appDao.findAll().compose(applications -> {
            At.infoApp(LOGGER, AtMsg.UNITY_APP, applications.size());
            /* Data, use application key as key here. */
            APP_POOL.putAll(Ut.elementZip(applications, XApp::getKey, app -> app));
            return Future.succeededFuture(Boolean.TRUE);
        }).compose(nil -> {
            final XSourceDao sourceDao = new XSourceDao(configuration, vertx);
            return sourceDao.findAll();
        }).compose(sources -> {
            /* All data source here */
            At.infoApp(LOGGER, AtMsg.UNITY_SOURCE, sources.size());
            /* Data, use application key as key here. */
            SOURCE_POOL.putAll(Ut.elementZip(sources, XSource::getAppId, source -> source));
            return Future.succeededFuture(Boolean.TRUE);
        });
    }

    static ConcurrentMap<String, XApp> getApps() {
        return APP_POOL;
    }

    static ConcurrentMap<String, XSource> getSources() {
        return SOURCE_POOL;
    }
}
