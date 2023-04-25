package io.horizon.spi.extension;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.AtMsg;
import io.vertx.tp.ambient.refine.At;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.uca.cache.RapidKey;
import io.vertx.up.unity.Ux;

import java.util.function.Function;

public class DatabaseInit implements Init {

    private static final Annal LOGGER = Annal.get(DatabaseInit.class);

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return appJson -> {
            At.infoApp(LOGGER, AtMsg.INIT_DATABASE, appJson.encode());
            /* Database Json */
            final JsonObject databaseJson = appJson.getJsonObject(KName.SOURCE);
            final Database database = new Database();
            database.fromJson(databaseJson);
            /*
             * Init third step: X_SOURCE stored into pool
             */
            return Rapid.<String, Database>t(RapidKey.DATABASE)
                .write(appJson.getString(KName.KEY), database)
                .compose(item -> Ux.future(item.toJson()))
                .compose(item -> Ux.future(this.result(appJson, item)));
        };
    }

    @Override
    public JsonObject result(final JsonObject input,
                             final JsonObject database) {
        At.infoApp(LOGGER, AtMsg.INIT_DB_RT, database.encodePrettily());
        return input;
    }
}
