package io.vertx.tp.optic.extension;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.AtConstant;
import io.vertx.tp.ambient.cv.AtMsg;
import io.vertx.tp.ambient.refine.At;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.commune.config.Database;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

import java.util.function.Function;

public class DatabaseInit implements Init {

    private static final Annal LOGGER = Annal.get(DatabaseInit.class);

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return appJson -> {
            At.infoApp(LOGGER, AtMsg.INIT_DATABASE, appJson.encode());
            /* Database Json */
            final JsonObject databaseJson = appJson.getJsonObject(KeField.SOURCE);
            final Database database = new Database();
            database.fromJson(databaseJson);
            /*
             * Init third step: X_SOURCE stored into pool
             */
            return Ux.Pool.on(AtConstant.POOL_DATABASE).put(appJson.getString(KeField.KEY), database)
                    .compose(item -> Ux.future(item.getValue()))
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
