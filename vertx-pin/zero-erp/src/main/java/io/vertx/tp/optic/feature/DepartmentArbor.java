package io.vertx.tp.optic.feature;

import cn.vertxup.erp.domain.tables.daos.EDeptDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DepartmentArbor implements Arbor {
    @Override
    public Future<JsonArray> generate(final JsonObject category, final JsonObject configuration) {
        return Ux.Jooq.on(EDeptDao.class)
            .fetchJAsync(KName.SIGMA, category.getValue(KName.SIGMA))
            .compose(children -> {

                return Ux.future(children);
            });
    }
}
