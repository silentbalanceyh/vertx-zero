package cn.vertxup.ambient.service;

import cn.vertxup.ambient.domain.tables.daos.XAppDao;
import cn.vertxup.ambient.domain.tables.daos.XMenuDao;
import cn.vertxup.ambient.domain.tables.daos.XSourceDao;
import cn.vertxup.ambient.domain.tables.pojos.XApp;
import cn.vertxup.ambient.domain.tables.pojos.XMenu;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.business.ExApp;
import io.vertx.up.atom.unity.Uson;
import io.vertx.up.unity.Ux;

public class AppService implements AppStub {

    @Override
    public Future<JsonObject> fetchByName(final String name) {
        return Ux.Jooq.on(XAppDao.class)
                /* Fetch By Name */
                .<XApp>fetchOneAsync(KeField.NAME, name)
                /* Convert to Json */
                .compose(Ux::fnJObject)
                /* Before App Initialized ( Public Api ) */
                .compose(appData -> Uson.create(appData).remove(KeField.APP_KEY).toFuture())
                /* Image field: logo */
                .compose(Ke.image(KeField.App.LOGO));
    }

    @Override
    public Future<JsonObject> fetchById(final String appId) {
        return Ux.Jooq.on(XAppDao.class)
                /* Fetch By Id */
                .<XApp>fetchByIdAsync(appId)
                /* Convert to Json */
                .compose(Ux::fnJObject)
                /* Image field: logo */
                .compose(Ke.image(KeField.App.LOGO))
                /* App options: options for application */
                .compose(appJson -> Ke.channelAsync(ExApp.class,
                        () -> Ux.future(appJson),
                        stub -> stub.fetchOpts(appJson)));
    }

    @Override
    public Future<JsonArray> fetchMenus(final String appId) {
        return Ux.Jooq.on(XMenuDao.class)
                /* Fetch by appId */
                .<XMenu>fetchAsync(KeField.APP_ID, appId)
                /* Get Result */
                .compose(Ux::fnJArray);
    }

    @Override
    public Future<JsonObject> fetchSource(final String appId) {
        return Ux.Jooq.on(XSourceDao.class)
                /* Fetch One by appId */
                .fetchOneAsync(KeField.APP_ID, appId)
                /* Get Result */
                .compose(Ux::fnJObject);
    }
}
