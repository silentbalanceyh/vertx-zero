package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.Apeak;
import io.vertx.up.eon.Constants;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.UxPool;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicFull implements Agonic {
    private static final Annal LOGGER = Annal.get(AgonicFull.class);

    @Override
    public Future<JsonArray> runJAAsync(final JsonObject input, final IxMod in) {
        final UxPool pool = Ux.Pool.on(Constants.Pool.CACHE_VIEW);
        /* Get Stub */
        final String cacheKey = in.cacheKey();
        return pool.<String, JsonArray>get(cacheKey).compose(columns -> {
            if (Objects.isNull(columns)) {
                final UxJooq jooq = IxPin.jooq(in);
                return Ke.channel(Apeak.class, JsonArray::new, stub -> stub.on(jooq).fetchFull(input))
                    .compose(viewData -> pool.put(cacheKey, viewData, Agonic.EXPIRED).compose(nil -> Ux.future(viewData)));
            } else {
                LOGGER.info("[ My ] Full View cached hit by {0}", cacheKey);
                return Ux.future(columns);
            }
        });
    }
}
