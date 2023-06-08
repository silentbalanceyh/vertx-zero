package io.horizon.spi.extension;

import cn.vertxup.ambient.domain.tables.daos.XAppDao;
import cn.vertxup.ambient.domain.tables.pojos.XApp;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.cv.AtMsg;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.function.Function;

import static io.vertx.mod.ambient.refine.At.LOG;

/*
 * EmApp Initialization
 */
public class AppInit implements Init {
    private static final Annal LOGGER = Annal.get(AppInit.class);

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return appJson -> {
            LOG.App.info(LOGGER, AtMsg.INIT_APP, appJson.encode());
            /* Deserialization */
            final XApp app = this.init(appJson);
            return Ux.Jooq.on(XAppDao.class)
                /*
                 * Init first step: UPSERT ( Insert / Update )
                 */
                .upsertAsync(this.whereUnique(appJson), app)
                .compose(Ux::futureJ)
                /*
                 * Result Building
                 */
                .compose(input -> Ux.future(this.result(input, appJson)));
        };
    }

    @Override
    public JsonObject result(final JsonObject input,
                             final JsonObject appJson) {
        final JsonObject result = new JsonObject();
        if (!Ut.isNil(appJson)) {
            result.mergeIn(appJson);
        }
        /* Data Source Input */
        if (!Ut.isNil(input)) {
            result.put(KName.SOURCE, input.getValue(KName.SOURCE));
        }
        return result;
    }

    @Override
    public JsonObject whereUnique(final JsonObject input) {
        final JsonObject filters = new JsonObject();
        filters.put(KName.KEY, input.getValue(KName.KEY));
        return filters;
    }

    private XApp init(final JsonObject input) {
        /* appKey generation */
        if (!input.containsKey(KName.APP_KEY)) {
            input.put(KName.APP_KEY, Ut.randomString(64));
        }
        /* logo */
        final JsonArray files = input.getJsonArray(KName.App.LOGO);
        if (null != files) {
            input.put(KName.App.LOGO, files.encode());
        }
        final XApp app = Ut.deserialize(input.copy(), XApp.class);
        /* active = true */
        app.setActive(Boolean.TRUE);
        return app;
    }
}
