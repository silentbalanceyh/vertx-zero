package io.vertx.tp.optic.feature;

import cn.vertxup.ambient.domain.tables.daos.XActivityRuleDao;
import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.refine.At;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.commune.wffs.Regulation;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ValueRule implements Valve {
    @Override
    public Future<JsonObject> execAsync(final JsonObject data, final JsonObject config) {
        /*
         * If criteria is empty, return the input data directly
         */
        final JsonObject criteria = Ut.valueJObject(data, Qr.KEY_CRITERIA);
        if (Ux.Jooq.isEmpty(criteria)) {
            return Ux.future(data);
        }
        return this.fetchRegulation(criteria).compose(regulation -> {

            return null;
        });
    }

    private Future<Regulation> fetchRegulation(final JsonObject criteria) {
        return Ux.Jooq.on(XActivityRuleDao.class)
            .<XActivityRule>fetchAsync(criteria)
            .compose(At::ruleRegulation);
    }
}
