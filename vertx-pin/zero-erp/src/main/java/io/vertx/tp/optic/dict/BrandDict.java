package io.vertx.tp.optic.dict;

import cn.vertxup.erp.domain.tables.daos.EBrandDao;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.optic.component.DictionaryPlugin;
import io.vertx.up.commune.exchange.DictSource;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/*
 * Dict for `resource.brands` here
 */
public class BrandDict implements DictionaryPlugin {

    @Override
    public Future<JsonArray> fetchAsync(final DictSource source,
                                        final MultiMap paramMap) {
        final String sigma = paramMap.get(KeField.SIGMA);
        if (Ut.notNil(sigma)) {
            return Ux.Jooq.on(EBrandDao.class)
                    .fetchAsync(KeField.SIGMA, sigma)
                    .compose(Ux::futureA);
        } else {
            return Ux.future(new JsonArray());
        }
    }

    @Override
    public JsonArray fetch(final DictSource source,
                           final MultiMap paramMap) {
        final String sigma = paramMap.get(KeField.SIGMA);
        if (Ut.notNil(sigma)) {
            return Ux.Jooq.on(EBrandDao.class)
                    .fetchJ(KeField.SIGMA, sigma);
        } else {
            return new JsonArray();
        }
    }
}