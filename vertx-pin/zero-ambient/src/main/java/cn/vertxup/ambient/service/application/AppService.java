package cn.vertxup.ambient.service.application;

import cn.vertxup.ambient.domain.tables.daos.XAppDao;
import cn.vertxup.ambient.domain.tables.daos.XSourceDao;
import cn.vertxup.ambient.domain.tables.pojos.XApp;
import io.horizon.eon.VString;
import io.horizon.spi.business.ExApp;
import io.horizon.spi.feature.Attachment;
import io.horizon.spi.modeler.Modulat;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.refine.At;
import io.vertx.up.atom.typed.UObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

public class AppService implements AppStub {

    @Override
    public Future<JsonObject> fetchByName(final String name) {
        return Ux.Jooq.on(XAppDao.class)
            /* Fetch By Name */
            .<XApp>fetchOneAsync(KName.NAME, name)
            /* Convert to Json */
            .compose(Ux::futureJ)
            /* Before App Initialized ( Public Api ) */
            .compose(appData -> UObject.create(appData).remove(KName.APP_KEY).toFuture())
            /*
             * Storage of file definition, here are two parts:
             * 1) - Logo            to Object
             * 2) - storePath       extract from `configuration.json`
             * */
            .compose(At::fileMeta);
    }

    @Override
    public Future<JsonObject> fetchById(final String appId) {
        return Ux.Jooq.on(XAppDao.class)
            /* Fetch By Id */
            .<XApp>fetchByIdAsync(appId)
            /* Convert to Json */
            .compose(Ux::futureJ)
            /*
             * Storage of file definition, here are two parts:
             * 1) - Logo            to Object
             * 2) - storePath       extract from `configuration.json`
             * */
            .compose(At::fileMeta)
            /* ExApp Processing, options for application */
            .compose(appJ -> Ux.channel(ExApp.class, () -> appJ, stub -> stub.fetchOpts(appJ)))
            /* Modulat Processing */
            .compose(appJ -> Ux.channel(Modulat.class, () -> appJ, stub -> stub.extension(appJ)));
        /* Document Platform Initialized */
        // .compose(appJ -> AtPin.?nitDocument(appId).compose(nil -> Ux.future(appJ)));
    }

    @Override
    public Future<JsonObject> fetchSource(final String appId) {
        return Ux.Jooq.on(XSourceDao.class)
            /* Fetch One by appId */
            .fetchOneAsync(KName.APP_ID, appId)
            /* Get Result */
            .compose(Ux::futureJ)
            /* JDBC */
            .compose(Fn.ofJObject("jdbcConfig"));
    }

    @Override
    public Future<JsonObject> updateBy(final String appId, final JsonObject data) {
        return this.updateLogo(appId, data)
            .compose(updated -> Ux.Jooq.on(XAppDao.class).updateJAsync(appId, updated)
                /* Image field: logo */
                .compose(Fn.ofJObject(KName.App.LOGO)));
    }

    private Future<JsonObject> updateLogo(final String appId, final JsonObject data) {
        final JsonArray attachment = data.getJsonArray(KName.App.LOGO, new JsonArray());
        // Multi EmApp Needed
        Ut.itJArray(attachment).forEach(each -> each.put(KName.MODEL_KEY, appId));
        final JsonObject condition = new JsonObject();
        condition.put(KName.MODEL_ID, "x.application");
        condition.put(KName.MODEL_CATEGORY, KName.App.LOGO);
        condition.put(KName.MODEL_KEY, appId);
        condition.put(VString.EMPTY, Boolean.TRUE);
        return Ux.channel(Attachment.class, () -> data,
            // Sync Attachment with channel
            file -> file.saveAsync(condition, attachment).compose(saved -> {
                data.put(KName.App.LOGO, saved.encode());
                return Ux.future(data);
            }));
    }
}
