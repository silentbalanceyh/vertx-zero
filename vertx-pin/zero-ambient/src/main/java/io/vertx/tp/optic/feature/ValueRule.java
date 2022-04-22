package io.vertx.tp.optic.feature;

import cn.vertxup.ambient.domain.tables.daos.XActivityRuleDao;
import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.refine.At;
import io.vertx.up.atom.Refer;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.commune.wffs.Formula;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

        final Refer ruleRef = new Refer();
        /* Build Regulation ( Formula ) */
        return Ux.Jooq.on(XActivityRuleDao.class).<XActivityRule>fetchAsync(criteria)
            .compose(ruleRef::future)
            /* XActivity Rule -> Regulation */
            .compose(At::ruleRegulation)
            .compose(regulation -> {
                // Rule Map
                final List<XActivityRule> rules = ruleRef.get();
                final List<Future<JsonObject>> futures = new ArrayList<>();
                rules.forEach(rule -> futures.add(this.traceFn(rule, regulation.find(rule.getKey())).apply(data)));
                return Ux.thenCombine(futures);
            })
            .compose(nil -> Ux.future(data));
    }

    private Function<JsonObject, Future<JsonObject>> traceFn(final XActivityRule rule, final Formula formula) {
        return params -> {

            return null;
        };
    }
}
