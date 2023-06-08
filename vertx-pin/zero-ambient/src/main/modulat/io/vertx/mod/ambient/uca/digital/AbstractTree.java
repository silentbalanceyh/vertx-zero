package io.vertx.mod.ambient.uca.digital;

import cn.vertxup.ambient.domain.tables.daos.XCategoryDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractTree extends AbstractAide implements Tree {

    // --------------- fetchArray method ------------------
    /* X_CATEGORY */
    protected Future<JsonArray> fetchTree(final JsonObject criteria) {
        return Ux.Jooq.on(XCategoryDao.class).fetchAsync(criteria)
            .compose(Ux::futureA)
            .compose(Fn.ofJArray(
                KName.METADATA,
                KName.Component.TREE_CONFIG,
                KName.Component.RUN_CONFIG
            ));
    }

    protected JsonObject condApp(final String appId, final String type, final String code,
                                 final Boolean leaf) {
        final JsonObject criteria = this.condApp(appId, type, code);
        if (Objects.nonNull(leaf) && !leaf) {
            criteria.put("leaf", Boolean.FALSE);
        }
        return criteria;
    }

    protected JsonObject condSigma(final String sigma, final String type, final String code,
                                   final Boolean leaf) {
        final JsonObject criteria = this.condSigma(sigma, type, code);
        if (Objects.nonNull(leaf) && !leaf) {
            criteria.put("leaf", Boolean.FALSE);
        }
        return criteria;
    }
}
