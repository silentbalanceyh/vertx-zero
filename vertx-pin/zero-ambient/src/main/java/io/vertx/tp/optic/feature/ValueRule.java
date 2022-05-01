package io.vertx.tp.optic.feature;

import cn.vertxup.ambient.domain.tables.daos.XActivityRuleDao;
import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.em.TubeType;
import io.vertx.tp.ambient.uca.darkly.Tube;
import io.vertx.up.atom.Refer;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.commune.wffs.Formula;
import io.vertx.up.commune.wffs.Regulation;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ValueRule implements Valve {
    @Override
    public Future<JsonObject> execAsync(final JsonObject data, final JsonObject config) {
        /* If criteria is empty, return the input data directly */
        final JsonObject criteria = Ut.valueJObject(data, Qr.KEY_CRITERIA);
        if (Ux.Jooq.isEmpty(criteria)) {
            return Ux.future(data);
        }

        final Refer ruleRef = new Refer();
        /* Build Regulation ( Formula ) */
        return Ux.Jooq.on(XActivityRuleDao.class).<XActivityRule>fetchAsync(criteria)
            .compose(ruleRef::future)
            /* XActivity Rule -> Regulation */
            .compose(this::ruleRegulation)
            /* Grouped all the rule and run each */
            .compose(regulation -> regulation.run(data.copy(), this.ruleFn(ruleRef.get())));
    }

    private ConcurrentMap<String, Function<JsonObject, Future<JsonObject>>> ruleFn(final List<XActivityRule> ruleList) {
        final ConcurrentMap<String, Function<JsonObject, Future<JsonObject>>> ruleMap = new ConcurrentHashMap<>();
        ruleList.forEach(rule -> {
            // Logging get
            final Boolean isLog = Objects.isNull(rule.getLogging()) ? Boolean.FALSE : rule.getLogging();
            if (isLog) {
                /*
                 * Rule Type detect Tube component to record the history
                 * 1) When the tube could not be detected   ( TubeEmpty )
                 * 2) When the type is :
                 * -- ATOM:             TubeAtom
                 * -- WORKFLOW:         TubeFlow
                 * -- ( Reserved )
                 */
                ruleMap.put(rule.getKey(), params -> {
                    final TubeType type = Ut.toEnum(rule::getType, TubeType.class, null);
                    final Tube tube = Tube.instance(type);
                    return tube.traceAsync(params, rule);
                });
            } else {
                ruleMap.put(rule.getKey(), Ux::future);
            }
        });
        return ruleMap;
    }


    /*
     * Combine the list of activity rule to `Regulation` object
     *
     * 1) The rule must contain `ruleExpression` field first.
     * 2) The all the rules will be put into one Regulation here
     */
    private Future<Regulation> ruleRegulation(final List<XActivityRule> rules) {
        final Regulation regulation = new Regulation();
        /* Expression is Null, Ignored the rule triggered */
        rules.stream().filter(rule -> Objects.nonNull(rule.getRuleExpression())).forEach(rule -> {
            /* Formula Building */
            final Formula formula = new Formula(rule.getKey());

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
