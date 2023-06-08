package io.horizon.spi.environment;

import cn.vertxup.ambient.domain.tables.daos.XAppDao;
import cn.vertxup.ambient.domain.tables.daos.XSourceDao;
import cn.vertxup.ambient.domain.tables.pojos.XApp;
import cn.vertxup.ambient.domain.tables.pojos.XSource;
import io.horizon.eon.VOption;
import io.horizon.eon.em.EmDS;
import io.macrocosm.atom.context.KArk;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.modello.atom.app.KDS;
import io.modello.atom.app.KDatabase;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.cv.AtMsg;
import io.vertx.mod.ambient.refine.At;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.jooq.Configuration;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 「应用/数据源」
 * <pre><code>
 *     1. 应用初始化
 *     2. 数据源初始化
 * </code></pre>
 *
 * @author lang : 2023-06-07
 */
class RegistryKit {
    static Future<ConcurrentMap<String, XApp>> initApp(final Vertx vertx) {
        final Configuration configuration = Ke.getConfiguration();
        final XAppDao appDao = new XAppDao(configuration, vertx);
        return appDao.findAll().compose(applications -> {
            At.LOG.App.info(RegistryKit.class, AtMsg.UNITY_APP, applications.size());
            return Ux.future(Ut.elementMap(applications, XApp::getKey));
        });
    }

    static Future<ConcurrentMap<String, List<XSource>>> initSource(final Vertx vertx) {
        final Configuration configuration = Ke.getConfiguration();
        final XSourceDao sourceDao = new XSourceDao(configuration, vertx);
        return sourceDao.findAll().compose(sources -> {
            At.LOG.App.info(RegistryKit.class, AtMsg.UNITY_SOURCE, sources.size());
            final ConcurrentMap<String, List<XSource>> grouped = Ut.elementGroup(sources, XSource::getAppId);
            return Ux.future(grouped);
        });
    }

    static HArk combine(final XApp app, final List<XSource> sources) {
        // 1. HArk 创建
        final HArk ark = KArk.of(app.getName());
        final JsonObject normalized = new JsonObject();
        /* ID */
        {
            /*
             * 3 Fields of identifiers
             * sigma - Cross module to identify application / container here.
             * appId - Inner ambient environment to identify application.
             * appKey - Ox engine used as dynamic identifier here.
             */
            normalized.put(KName.KEY, app.getKey());        // `key` fixed when `api` and `non-api` configuration
            normalized.put(KName.APP_ID, app.getKey());
            normalized.put(KName.APP_KEY, app.getAppKey());
            normalized.put(KName.SIGMA, app.getSigma());
        }
        /* Unique */
        {
            /*
             * System information of application
             * 「Front App」
             * name - the unique name that will be used in front environment variable.  Z_APP
             * code - the application system code here that could be parsed by system.
             * language - language of this application
             * active - Whether enabled ( it's for future )
             */
            normalized.put(KName.NAME, app.getName());
            normalized.put(KName.CODE, app.getCode());
            normalized.put(KName.LANGUAGE, app.getLanguage());
            normalized.put(KName.ACTIVE, app.getActive());
        }
        /* Business information */
        {
            /* Major: Logo and Title */
            normalized.put(KName.App.LOGO, app.getLogo());
            normalized.put(KName.App.TITLE, app.getTitle());
            /*
             * Business information
             * title - display the information on front app
             * logo - display the logo on front app here.
             * icp - icp number of this application.
             * copyRight - copy right of this application.
             * email - administrator email that could be contacted
             */
            final JsonObject business = new JsonObject();
            business.put(KName.App.ICP, app.getIcp());
            business.put(KName.App.EMAIL, app.getEmail());
            business.put(KName.App.COPY_RIGHT, app.getCopyRight());
            normalized.put("business", business);
        }
        /* Deployment information */
        {
            /*
             * Deployment information
             * Back-End
             * domain - application domain information that will be deployed
             * port - application port information that will be exposed.
             * route - application sub routing information
             */
            final JsonObject backend = new JsonObject();
            backend.put(KName.App.DOMAIN, app.getDomain());
            backend.put(KName.App.APP_PORT, app.getAppPort());
            backend.put(KName.App.ROUTE, app.getRoute());
            normalized.put("backend", backend);
            /*
             * Front-End
             * path - front end application information
             * urlEntry - Url Entry of Login Home
             * urlMain - Url Entry of Admin Home
             *
             */
            final JsonObject frontend = new JsonObject();
            frontend.put(KName.App.PATH, app.getPath());
            frontend.put(KName.App.URL_ENTRY, app.getUrlEntry());
            frontend.put(KName.App.URL_MAIN, app.getUrlMain());
            normalized.put("frontend", frontend);
        }
        /* Auditor information */
        {
            /*
             * Auditor information of current application.
             * createdAt, createdBy
             * updatedAt, updatedBy
             */
            final JsonObject auditor = new JsonObject();
            auditor.put(KName.CREATED_BY, app.getCreatedBy());
            Fn.runAt(() -> auditor.put(KName.CREATED_AT, Ut.parse(app.getCreatedAt()).toInstant()), app.getCreatedAt());
            auditor.put(KName.UPDATED_BY, app.getUpdatedBy());
            Fn.runAt(() -> auditor.put(KName.UPDATED_AT, Ut.parse(app.getUpdatedAt()).toInstant()), app.getUpdatedAt());
            normalized.put("auditor", auditor);
        }
        // Database
        final KDS<KDatabase> kds = ark.database();
        {
            kds.registry(EmDS.Stored.PRIMARY, Database.getCurrent());
            kds.registry(EmDS.Stored.WORKFLOW, Database.getCamunda());
            kds.registry(EmDS.Stored.HISTORY, Database.getHistory());
        }
        if (!sources.isEmpty()) {
            final JsonArray sourceArray = new JsonArray();
            final Set<KDatabase> databaseSet = new LinkedHashSet<>();
            sources.forEach(source -> {
                final JsonObject sourceJson = new JsonObject();
                sourceJson.put(VOption.database.HOSTNAME, source.getHostname());
                sourceJson.put(VOption.database.INSTANCE, source.getInstance());
                sourceJson.put(VOption.database.PORT, source.getPort());
                sourceJson.put(VOption.database.CATEGORY, source.getCategory());
                sourceJson.put(VOption.database.JDBC_URL, source.getJdbcUrl());
                sourceJson.put(VOption.database.USERNAME, source.getUsername());
                sourceJson.put(VOption.database.PASSWORD, source.getPassword());
                sourceJson.put(VOption.database.DRIVER_CLASS_NAME, source.getDriverClassName());
                final String jdbcConfig = source.getJdbcConfig();
                if (Ut.isNotNil(jdbcConfig)) {
                    sourceJson.put(VOption.database.OPTIONS, Ut.toJObject(jdbcConfig));
                }
                /*
                 * {
                 *     "source": [
                 *         {
                 *             "hostname": "xx",
                 *             "instance": "instance",
                 *             "port": "",
                 *             "category": "",
                 *             "jdbcUrl": "",
                 *             "username": "",
                 *             "password": "",
                 *             "driverClassName": "",
                 *             "options": {
                 *             }
                 *         }
                 *     ]
                 * }
                 */
                sourceArray.add(sourceJson);
                final Database database = new Database();
                database.fromJson(sourceJson);
                databaseSet.add(database);
            });
            kds.registry(databaseSet);
        }
        // App Json
        final HApp appRef = ark.app();
        appRef.option(normalized, true);
        return ark;
    }
}
