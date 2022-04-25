package io.vertx.tp.ambient.refine;

import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.wffs.Formula;
import io.vertx.up.commune.wffs.Regulation;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AtExpr {

    static Future<Regulation> ruleRegulation(final List<XActivityRule> rules) {
        final Regulation regulation = new Regulation();
        /* Expression is Null, Ignored the rule triggered */
        rules.stream().filter(rule -> Objects.nonNull(rule.getRuleExpression())).forEach(rule -> {
            /* Formula Building */
            final JsonObject config = Ut.toJObject(rule.getRuleConfig());
            final Formula formula = new Formula(rule.getKey(), config);

            // bind(tpl, config)
            final JsonObject tpl = Ut.toJObject(rule.getRuleTpl());
            formula.bind(rule.getRuleExpression(), tpl);

            // bind(clazz, config)
            final Class<?> hookerCls = Ut.clazz(rule.getHookComponent(), null);
            if (Objects.nonNull(hookerCls)) {
                final JsonObject hookerConfig = Ut.toJObject(rule.getHookConfig());
                formula.bind(hookerCls, hookerConfig);
            }
            // name / logging
            formula.name(rule.getRuleName());

            regulation.add(formula);
        });
        return Ux.future(regulation);
    }
}
