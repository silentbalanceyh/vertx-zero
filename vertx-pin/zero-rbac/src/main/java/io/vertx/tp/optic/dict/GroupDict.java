package io.vertx.tp.optic.dict;

import cn.vertxup.rbac.domain.tables.daos.SGroupDao;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.optic.component.DictionaryPlugin;
import io.vertx.up.commune.exchange.DSource;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/*
 * Dict for `security.groups` here
 */
public class GroupDict implements DictionaryPlugin {

    @Override
    public Future<JsonArray> fetchAsync(final DSource source,
                                        final MultiMap paramMap) {
        final String sigma = paramMap.get(KName.SIGMA);
        if (Ut.notNil(sigma)) {
            return Ux.Jooq.on(SGroupDao.class)
                .fetchAsync(KName.SIGMA, sigma)
                .compose(Ux::futureA);
        } else {
            return Ux.future(new JsonArray());
        }
    }
}
