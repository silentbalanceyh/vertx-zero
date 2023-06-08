package io.vertx.mod.crud.uca.op;

import io.horizon.spi.ui.Apeak;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.init.IxPin;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.uca.cache.RapidKey;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicFull implements Agonic {
    private static final Annal LOGGER = Annal.get(AgonicFull.class);

    @Override
    public Future<JsonArray> runJAAsync(final JsonObject input, final IxMod in) {
        final String cacheKey = in.cacheKey();
        return Rapid.<String, JsonArray>t(RapidKey.VIEW_FULL, Agonic.EXPIRED).cached(cacheKey, () -> {
            final UxJooq jooq = IxPin.jooq(in);
            return Ux.channel(Apeak.class, JsonArray::new, stub -> stub.on(jooq).fetchFull(input));
        });
    }
}
