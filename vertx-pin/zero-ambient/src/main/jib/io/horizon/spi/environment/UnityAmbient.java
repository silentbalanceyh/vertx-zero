package io.horizon.spi.environment;

import cn.vertxup.ambient.domain.tables.pojos.XApp;
import cn.vertxup.ambient.domain.tables.pojos.XSource;
import io.aeon.runtime.CRunning;
import io.horizon.eon.VOption;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/*
 * Read ambient here for data initialization
 */
@Deprecated
public class UnityAmbient implements UnityApp {

    @Override
    public Future<JsonObject> synchro(final String appId) {
        //        final ConcurrentMap<String, JsonObject> stored = CRunning.CC_META_APP.store();
        //        return UnityAsker.synchro(appId).compose(nil -> {
        //            final ConcurrentMap<String, XApp> apps = UnityAsker.getApps();
        //            final ConcurrentMap<String, XSource> sources = UnityAsker.getSources();
        //            final XApp app = apps.get(appId);
        //            final XSource source = sources.get(appId);
        //            final JsonObject updated = this.connect(app, source);
        //            stored.put(appId, updated);
        //            return Future.succeededFuture(updated);
        //        });
        return Ux.futureJ();
    }

    @Override
    public Future<Boolean> initialize(final Vertx vertx) {
        /*
         * Initialize Unity Pool, Checking for Environment
         */
        final ConcurrentMap<String, JsonObject> stored = CRunning.CC_META_APP.store();
        if (!stored.isEmpty()) {
            /*
             * 截断运行，如果加载过就不再运行一次 initialize 方法，若要刷新则可调用
             * synchro接口针对单系统执行调用
             */
            return Ux.futureT();
        }
        return UnityAsker.init(vertx).compose(nil -> {
            /*
             * JsonObject initialization for configuration here
             * When the `compose` here, the app and source has been initialized.
             */
            final ConcurrentMap<String, XApp> apps = UnityAsker.getApps();
            final ConcurrentMap<String, XSource> sources = UnityAsker.getSources();
            apps.keySet().stream()
                // .filter(appId -> Objects.nonNull(apps.get(appId)))
                // .filter(appId -> Objects.nonNull(sources.get(appId)))
                /* JsonObject converted here for app & source data */
                .map(appId -> this.connect(apps.get(appId), sources.get(appId)))
                .forEach(item -> stored.put(item.getString(KName.APP_ID), item));
            return Future.succeededFuture(Boolean.TRUE);
        });
    }

    @Override
    public ConcurrentMap<String, JsonObject> connect() {
        //        final Cd<String, JsonObject> stored = CRunning.CC_META_APP.store();
        return CRunning.CC_META_APP.store(); // stored.data();
    }

    private JsonObject connect(final XApp app, final XSource source) {
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
        /* Database information */
        if (Objects.nonNull(source)) {
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
             *     "source": {
             *         "hostname": "xx",
             *         "instance": "instance",
             *         "port": "",
             *         "category": "",
             *         "jdbcUrl": "",
             *         "username": "",
             *         "password": "",
             *         "driverClassName": "",
             *         "options": {
             *         }
             *     }
             * }
             */
            normalized.put(KName.SOURCE, sourceJson);
        }
        return normalized;
    }
}
