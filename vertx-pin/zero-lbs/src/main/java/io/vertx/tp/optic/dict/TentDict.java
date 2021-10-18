package io.vertx.tp.optic.dict;

import cn.vertxup.lbs.domain.tables.daos.LTentDao;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.optic.component.DictionaryPlugin;
import io.vertx.up.commune.exchange.DiSource;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/*
 * Dict for `location.tents` here
 */
public class TentDict implements DictionaryPlugin {

    @Override
    public Future<JsonArray> fetchAsync(final DiSource source,
                                        final MultiMap paramMap) {
        final String sigma = paramMap.get(KName.SIGMA);
        if (Ut.notNil(sigma)) {
            return Ux.Jooq.on(LTentDao.class)
                .fetchAsync(KName.SIGMA, sigma)
                .compose(Ux::futureA);
        } else {
            return Ux.future(new JsonArray());
        }
    }
}
