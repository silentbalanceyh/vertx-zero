package cn.vertxup.ambient.service.application;

import cn.vertxup.ambient.domain.tables.daos.XAppDao;
import io.horizon.spi.extension.Init;
import io.horizon.spi.extension.Prerequisite;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.init.AtPin;
import io.vertx.mod.ambient.refine.At;
import io.vertx.up.atom.typed.UObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import jakarta.inject.Inject;

import java.util.Objects;

import static io.vertx.mod.ambient.refine.At.LOG;

/**
 * ## EmApp initializer Implementation
 *
 * Please refer {@link InitStub}
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class InitService implements InitStub {
    /**
     * Zero standard logger of {@link Annal} instance.
     */
    private static final Annal LOGGER = Annal.get(InitService.class);
    /**
     * Injection for {@link AppStub}
     */
    @Inject
    private transient AppStub stub;

    /**
     * 「Async」( Creation ) This api is for application initialization at first time.
     *
     * Related Interface: {@link Init}
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
        return Ux.future(data.put(KName.KEY, appId))
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
            .compose(Fn.ofJObject(KName.App.LOGO));
    }

    /**
     * 「Async」( Edition ) This api is for application initialization at any time after 1st.
     *
     * Related Interface: {@link Init}
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
     * Related Interface: {@link Prerequisite}
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
            LOG.Init.info(LOGGER, "`Prerequisite` configuration is null");
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
        return this.initModeling(appName, null);
    }

    @Override
    public Future<JsonObject> initModeling(final String appName, final String outPath) {
        /* Fetch App */
        return Ux.Jooq.on(XAppDao.class)
            /* X_APP Fetching */
            .fetchOneAsync(KName.NAME, appName)
            .compose(Ux::futureJ)
            /* X_SOURCE fetching, Fetching skip Database initialization */
            .compose(this::initCombine)
            /* Output Path Injection */
            .compose(appJson -> this.initOutput(appJson, outPath))
            .compose(this::initDefined)
            /* Image */
            .compose(Fn.ofJObject(KName.App.LOGO));
    }

    private Future<JsonObject> initOutput(final JsonObject combined, final String outPath) {
        if (Ut.isNotNil(outPath)) {
            combined.put(KName.OUTPUT, outPath);
        }
        return Ux.future(combined);
    }

    /**
     * 「Async」Combine `X_APP` and `X_SOURCE`, mount `source` attribute to application json.
     *
     * @param appJson {@link io.vertx.core.json.JsonObject} Input application json here.
     *
     * @return {@link io.vertx.core.Future}<{@link io.vertx.core.json.JsonObject}>
     */
    private Future<JsonObject> initCombine(final JsonObject appJson) {
        return this.stub.fetchSource(appJson.getString(KName.KEY))
            .compose(source -> UObject.create(appJson).append(KName.SOURCE, source).toFuture());
    }

    /**
     * 「Async」Call `initializer` that defined in our configuration file.
     *
     * @param input {@link io.vertx.core.json.JsonObject} Input parameters related to {@link Init}
     *
     * @return {@link io.vertx.core.Future}<{@link io.vertx.core.json.JsonObject}>
     */
    private Future<JsonObject> initDefined(final JsonObject input) {
        final Init initializer = AtPin.getInit();
        if (Objects.isNull(initializer)) {
            LOG.Init.info(LOGGER, "`Init` configuration is null");
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
