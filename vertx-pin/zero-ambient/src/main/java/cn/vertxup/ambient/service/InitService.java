package cn.vertxup.ambient.service;

import cn.vertxup.ambient.domain.tables.daos.XAppDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.init.AtPin;
import io.vertx.tp.ambient.refine.At;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.extension.Init;
import io.vertx.tp.optic.extension.Prerequisite;
import io.vertx.up.atom.unity.Uson;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;
import java.util.Objects;

/**
 * ## Application initializer Implementation
 *
 * Please refer {@link cn.vertxup.ambient.service.InitStub}
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class InitService implements InitStub {
    /**
     * Zero standard logger of {@link io.vertx.up.log.Annal} instance.
     */
    private static final Annal LOGGER = Annal.get(InitService.class);
    /**
     * Injection for {@link cn.vertxup.ambient.service.AppStub}
     */
    @Inject
    private transient AppStub stub;

    /**
     * 「Async」( Creation ) This api is for application initialization at first time.
     *
     * Related Interface: {@link io.vertx.tp.optic.extension.Init}
     *
     * @param appId {@link java.lang.String} The application primary key that stored in `KEY` field of `X_APP`.
     * @param data  {@link io.vertx.core.json.JsonObject} The data that will create application instance.
     *
     * @return {@link io.vertx.core.Future}<{@link io.vertx.core.json.JsonObject}>
     */
    @Override
    public Future<JsonObject> initCreation(final String appId,
                                           final JsonObject data) {
        /* Default Future */
        return Ux.future(data.put(KeField.KEY, appId))
                /* X_APP initialization */
                .compose(At.initApp().apply())
                /* X_SOURCE initialization */
                .compose(At.initSource().apply())
                /* Database initialization */
                .compose(At.initDatabase().apply())
                /* Extension initialization */
                .compose(this::initDefined)
                /* Data Loading */
                .compose(At.initData().apply())
                /* Image */
                .compose(Ke.image(KeField.App.LOGO));
    }

    /**
     * 「Async」( Edition ) This api is for application initialization at any time after 1st.
     *
     * Related Interface: {@link io.vertx.tp.optic.extension.Init}
     *
     * @param appName {@link java.lang.String} The application name that stored in `NAME` field of `X_APP`.
     *
     * @return {@link io.vertx.core.Future}<{@link io.vertx.core.json.JsonObject}>
     */
    @Override
    public Future<JsonObject> initEdition(final String appName) {
        return this.initModeling(appName)
                /* Data Loading */
                .compose(At.initData().apply());
    }

    /**
     * 「Async」Pre-Workflow before initialization when call this method.
     *
     * Related Interface: {@link io.vertx.tp.optic.extension.Prerequisite}
     *
     * @param appName {@link java.lang.String} The application name that stored in `NAME` field of `X_APP`.
     *
     * @return {@link io.vertx.core.Future}<{@link io.vertx.core.json.JsonObject}>
     */
    @Override
    public Future<JsonObject> prerequisite(final String appName) {
        /* Prerequisite Extension */
        final Prerequisite prerequisite = AtPin.getPrerequisite();
        if (Objects.isNull(prerequisite)) {
            At.infoInit(LOGGER, "`Prerequisite` configuration is null");
            return Ux.future(new JsonObject());
        } else {
            /*
             * Prerequisite for initialization
             */
            return prerequisite.prepare(appName);
        }
    }

    /**
     * 「Async」( Modeling Only ) This api is new for modeling initialization.
     *
     * @param appName {@link java.lang.String} The application name that stored in `NAME` field of `X_APP`.
     *
     * @return {@link io.vertx.core.Future}<{@link io.vertx.core.json.JsonObject}>
     */
    @Override
    public Future<JsonObject> initModeling(final String appName) {
        /* Fetch App */
        return Ux.Jooq.on(XAppDao.class)
                /* X_APP Fetching */
                .fetchOneAsync(KeField.NAME, appName)
                .compose(Ux::futureJ)
                /* X_SOURCE fetching, Fetching skip Database initialization */
                .compose(this::initCombine)
                .compose(this::initDefined)
                /* Image */
                .compose(Ke.image(KeField.App.LOGO));
    }

    /**
     * 「Async」Combine `X_APP` and `X_SOURCE`, mount `source` attribute to application json.
     *
     * @param appJson {@link io.vertx.core.json.JsonObject} Input application json here.
     *
     * @return {@link io.vertx.core.Future}<{@link io.vertx.core.json.JsonObject}>
     */
    private Future<JsonObject> initCombine(final JsonObject appJson) {
        return this.stub.fetchSource(appJson.getString(KeField.KEY))
                .compose(source -> Uson.create(appJson).append(KeField.SOURCE, source).toFuture());
    }

    /**
     * 「Async」Call `initializer` that defined in our configuration file.
     *
     * @param input {@link io.vertx.core.json.JsonObject} Input parameters related to {@link io.vertx.tp.optic.extension.Init}
     *
     * @return {@link io.vertx.core.Future}<{@link io.vertx.core.json.JsonObject}>
     */
    private Future<JsonObject> initDefined(final JsonObject input) {
        final Init initializer = AtPin.getInit();
        if (Objects.isNull(initializer)) {
            At.infoInit(LOGGER, "`Init` configuration is null");
            return Ux.future(input);
        } else {
            /*
             * Extension for initialization
             * Will call initializer method, it's implemented Init.class ( Interface )
             *  */
            return initializer.apply().apply(input);
        }
    }
}
