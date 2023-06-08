package io.horizon.spi.extension;

import cn.vertxup.ambient.domain.tables.daos.XSourceDao;
import cn.vertxup.ambient.domain.tables.pojos.XSource;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.cv.AtMsg;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import static io.vertx.mod.ambient.refine.At.LOG;

public class SourceInit implements Init {

    private static final Annal LOGGER = Annal.get(SourceInit.class);

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return appJson -> {
            LOG.App.info(LOGGER, AtMsg.INIT_SOURCE, appJson.encode());
            /* X_SOURCE initialization */
            final JsonObject sourceJson = appJson.getJsonObject(KName.SOURCE);
            final XSource source = this.init(sourceJson, appJson);

            return Ux.Jooq.on(XSourceDao.class)
                /*
                 * Init second step: appId as condition, save X_APP
                 */
                .upsertAsync(this.whereUnique(appJson), source)
                .compose(Ux::futureJ)
                /*
                 * Result Building
                 */
                .compose(updated -> Ux.future(this.result(appJson, updated)));
        };
    }

    @Override
    public JsonObject whereUnique(final JsonObject appJson) {
        final JsonObject filters = new JsonObject();
        filters.put(KName.APP_ID, appJson.getValue(KName.KEY));
        return filters;
    }

    @Override
    public JsonObject result(final JsonObject input,
                             final JsonObject sourceJson) {
        input.put(KName.SOURCE, sourceJson);
        return input;
    }

    private XSource init(final JsonObject input,
                         final JsonObject appJson) {
        /* key set */
        final XSource source = Ut.deserialize(input.copy(), XSource.class);
        source.setActive(Boolean.TRUE);
        source.setAppId(appJson.getString(KName.KEY));
        /* Basic Configuration */
        source.setJdbcConfig(new JsonObject().encode());
        source.setMetadata(new JsonObject().encode());
        source.setLanguage(appJson.getString(KName.LANGUAGE));
        if (Objects.isNull(source.getKey())) {
            source.setKey(UUID.randomUUID().toString());
        }
        return source;
    }
}
