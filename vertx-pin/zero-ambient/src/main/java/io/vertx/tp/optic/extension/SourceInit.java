package io.vertx.tp.optic.extension;

import cn.vertxup.ambient.domain.tables.daos.XSourceDao;
import cn.vertxup.ambient.domain.tables.pojos.XSource;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.AtMsg;
import io.vertx.tp.ambient.refine.At;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class SourceInit implements Init {

    private static final Annal LOGGER = Annal.get(SourceInit.class);

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return appJson -> {
            At.infoApp(LOGGER, AtMsg.INIT_SOURCE, appJson.encode());
            /* X_SOURCE initialization */
            final JsonObject sourceJson = appJson.getJsonObject(KeField.SOURCE);
            final XSource source = this.init(sourceJson, appJson);

            return Ux.Jooq.on(XSourceDao.class)
                    /*
                     * Init second step: appId as condition, save X_APP
                     */
                    .upsertAsync(this.whereUnique(appJson), source)
                    .compose(Ux::fnJObject)
                    /*
                     * Result Building
                     */
                    .compose(updated -> Ux.future(this.result(appJson, updated)));
        };
    }

    @Override
    public JsonObject whereUnique(final JsonObject appJson) {
        final JsonObject filters = new JsonObject();
        filters.put(KeField.APP_ID, appJson.getValue(KeField.KEY));
        return filters;
    }

    @Override
    public JsonObject result(final JsonObject input,
                             final JsonObject sourceJson) {
        input.put(KeField.SOURCE, sourceJson);
        return input;
    }

    private XSource init(final JsonObject input,
                         final JsonObject appJson) {
        /* key set */
        final XSource source = Ut.deserialize(input.copy(), XSource.class);
        source.setActive(Boolean.TRUE);
        source.setAppId(appJson.getString(KeField.KEY));
        /* Basic Configuration */
        source.setJdbcConfig(new JsonObject().encode());
        source.setMetadata(new JsonObject().encode());
        source.setLanguage(appJson.getString(KeField.LANGUAGE));
        if (Objects.isNull(source.getKey())) {
            source.setKey(UUID.randomUUID().toString());
        }
        return source;
    }
}
