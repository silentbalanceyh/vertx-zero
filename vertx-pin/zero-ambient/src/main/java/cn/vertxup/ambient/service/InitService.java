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

public class InitService implements InitStub {
    private static final Annal LOGGER = Annal.get(InitService.class);
    @Inject
    private transient AppStub stub;

    @Override
    public Future<JsonObject> initApp(final String appId,
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

    @Override
    public Future<JsonObject> initExtension(final String appName) {
        /* Fetch App */
        return Ux.Jooq.on(XAppDao.class)
                /* X_APP Fetching */
                .fetchOneAsync(KeField.NAME, appName)
                .compose(Ux::fnJObject)
                /* X_SOURCE fetching, Fetching skip Database initialization */
                .compose(appJson -> this.stub.fetchSource(appJson.getString(KeField.KEY))
                        .compose(source -> Uson.create(appJson).append(KeField.SOURCE, source).toFuture())
                )
                .compose(this::initDefined)
                /* Data Loading */
                .compose(At.initData().apply())
                /* Image */
                .compose(Ke.image(KeField.App.LOGO));
    }

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
