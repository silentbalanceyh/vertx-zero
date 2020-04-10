package io.vertx.tp.optic.environment;

import cn.vertxup.ambient.domain.tables.daos.XAppDao;
import cn.vertxup.ambient.domain.tables.daos.XSourceDao;
import cn.vertxup.ambient.domain.tables.pojos.XApp;
import cn.vertxup.ambient.domain.tables.pojos.XSource;
import io.vertx.tp.ambient.cv.AtMsg;
import io.vertx.tp.ambient.refine.At;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.up.commune.config.Database;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.jooq.Configuration;

import java.util.List;
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
    static void init() {
        /* All app here */
        final XAppDao appDao = Ut.singleton(XAppDao.class, getConfiguration());
        final List<XApp> applications = appDao.findAll();
        At.infoApp(LOGGER, AtMsg.UNITY_APP, applications.size());
        /* All data source here */
        final XSourceDao sourceDao = Ut.singleton(XSourceDao.class, getConfiguration());
        final List<XSource> sources = sourceDao.findAll();
        At.infoApp(LOGGER, AtMsg.UNITY_SOURCE, sources.size());

        /* Data, use application key as key here. */
        APP_POOL.putAll(Ut.elementZip(applications, XApp::getKey, app -> app));
        SOURCE_POOL.putAll(Ut.elementZip(sources, XSource::getAppId, source -> source));
    }

    private static Configuration getConfiguration() {
        final Database database = Database.getCurrent();
        final DataPool pool = DataPool.create(database);
        return pool.getExecutor().configuration();
    }

    static ConcurrentMap<String, XApp> getApps() {
        return APP_POOL;
    }

    static ConcurrentMap<String, XSource> getSources() {
        return SOURCE_POOL;
    }
}
