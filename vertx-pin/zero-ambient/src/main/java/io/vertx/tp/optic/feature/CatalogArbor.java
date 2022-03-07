package io.vertx.tp.optic.feature;

import cn.vertxup.ambient.domain.tables.daos.XCategoryDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.phantom.AbstractArbor;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CatalogArbor extends AbstractArbor {
    @Override
    public Future<JsonArray> generate(final JsonObject category, final JsonObject configuration) {
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.SIGMA, category.getValue(KName.SIGMA));
        final JsonObject query = configuration.getJsonObject(KName.QUERY, new JsonObject());
        condition.mergeIn(query, true);
        return Ux.Jooq.on(XCategoryDao.class)
            .fetchJAsync(condition)
            .compose(Ut.ifJArray(
                KName.METADATA,
                KName.Component.TREE_CONFIG,
                KName.Component.RUN_CONFIG
            ))
            .compose(children -> this.ensureChildren(category, children, configuration));
    }
}
