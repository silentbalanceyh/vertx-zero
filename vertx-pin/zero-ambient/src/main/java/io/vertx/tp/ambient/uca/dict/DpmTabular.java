package io.vertx.tp.ambient.uca.dict;

import cn.vertxup.ambient.domain.tables.daos.XTabularDao;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.commune.exchange.DictSource;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentMap;

/**
 * ## `X_TABULAR` Dict
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DpmTabular implements Dpm {

    @Override
    public Future<ConcurrentMap<String, JsonArray>> fetchAsync(final DictSource source, final MultiMap params) {
        return Ux.Jooq.on(XTabularDao.class)
                .fetchAndAsync(DpmTool.condition(params, source))
                .compose(Ux::futureG);
    }

    @Override
    public ConcurrentMap<String, JsonArray> fetch(final DictSource source, final MultiMap params) {
        final JsonArray dataArray = Ux.Jooq.on(XTabularDao.class)
                .fetchJAnd(DpmTool.condition(params, source));
        return Ut.elementGroup(dataArray, KeField.TYPE);
    }
}
