package io.vertx.tp.optic.dict;

import cn.vertxup.erp.domain.tables.daos.ECompanyDao;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.optic.component.DictionaryPlugin;
import io.vertx.up.commune.exchange.DictSource;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/*
 * Dict for `resource.companys` here
 */
public class CompanyDict implements DictionaryPlugin {

    @Override
    public Future<JsonArray> fetchAsync(final DictSource source,
                                        final MultiMap paramMap) {
        final String sigma = paramMap.get(KeField.SIGMA);
        if (Ut.notNil(sigma)) {
            return Ux.Jooq.on(ECompanyDao.class)
                    .fetchAsync(KeField.SIGMA, sigma)
                    .compose(Ux::fnJArray);
        } else {
            return Ux.future(new JsonArray());
        }
    }
}
