package io.horizon.spi.dict;

import cn.vertxup.rbac.domain.tables.daos.SGroupDao;
import io.horizon.spi.component.DictionaryPlugin;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.up.atom.exchange.DSource;
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
        if (Ut.isNotNil(sigma)) {
            return Ux.Jooq.on(SGroupDao.class)
                .fetchAsync(KName.SIGMA, sigma)
                .compose(Ux::futureA);
        } else {
            return Ux.future(new JsonArray());
        }
    }
}
