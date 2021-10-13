package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.Apeak;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.uca.cache.RapidKey;
import io.vertx.up.uca.jooq.UxJooq;

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
            return Ke.channel(Apeak.class, JsonArray::new, stub -> stub.on(jooq).fetchFull(input));
        });
    }
}
