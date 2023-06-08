package io.horizon.spi.feature;

import cn.vertxup.ambient.domain.tables.daos.XActivityRuleDao;
import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.horizon.atom.common.Refer;
import io.horizon.spi.modeler.Indent;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.cv.em.TubeType;
import io.vertx.mod.ambient.error._501IndentMissingException;
import io.vertx.mod.ambient.uca.darkly.Tube;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.wffs.Formula;
import io.vertx.up.uca.wffs.Regulation;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import static io.vertx.mod.ambient.refine.At.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ValueRule implements Valve {

    @Override
    public Future<JsonObject> execAsync(final JsonObject data, final JsonObject config) {
        /* If criteria is empty, return the input data directly */
        final JsonObject criteria = Ut.valueJObject(data, Ir.KEY_CRITERIA);
        if (Ux.irNil(criteria)) {
            return Ux.future(data);
        }
        LOG.Tabb.info(this.getClass(), "Qr condition for ActivityRule: {0}", criteria.encode());
        /* Not Skip */
        final Refer ruleRef = new Refer();
        final Refer inputRef = new Refer();
        final Refer serialRef = new Refer();
        return Ke.umUser(data)
            .compose(inputRef::future)

            /* Build Regulation ( Formula ) */
            .compose(normalized -> Ux.Jooq.on(XActivityRuleDao.class).<XActivityRule>fetchAsync(criteria))
            .compose(ruleRef::future)

            /* Build Serial Ready */
            .compose(rules -> this.ruleSerial(rules)
                .compose(serialRef::future)
                .compose(nil -> Ux.future(rules))
            )

            /* XActivity Rule -> Regulation */
            .compose(this::ruleRegulation)
            /* Grouped all the rule and run each */
            .compose(regulation -> regulation.run(((JsonObject) inputRef.get()).copy(),
                this.ruleFn(ruleRef.get(), serialRef.get())));
    }

    private ConcurrentMap<String, Function<JsonObject, Future<JsonObject>>> ruleFn(
        final List<XActivityRule> ruleList, final ConcurrentMap<String, Queue<String>> serialMap) {
        final ConcurrentMap<String, Function<JsonObject, Future<JsonObject>>> ruleMap = new ConcurrentHashMap<>();
        // Ordered Internal By ruleOrder
        ruleList.sort(Comparator.comparing(XActivityRule::getRuleOrder));
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
                final String code = this.ruleIndent(rule);
                final Queue<String> serialQ = serialMap.get(code);
                final String serial = serialQ.poll();

                ruleMap.put(rule.getKey(), params -> {
                    final TubeType type = Ut.toEnum(rule::getType, TubeType.class, null);
                    final Tube tube = Tube.instance(type);
                    params.put(KName.Flow.TRACE_SERIAL, serial);
                    return tube.traceAsync(params, rule);
                });
            } else {
                ruleMap.put(rule.getKey(), Ux::future);
            }
        });
        return ruleMap;
    }

    /*
     * To avoid number conflicts, here we should generate the number before running here
     */
    private Future<ConcurrentMap<String, Queue<String>>> ruleSerial(final List<XActivityRule> ruleList) {
        if (ruleList.isEmpty()) {
            return Ux.future(new ConcurrentHashMap<>());
        }
        /*
         * Grouped ruleList by `ruleConfig -> data -> indent`
         */
        final ConcurrentMap<String, Future<Queue<String>>> serialQ = new ConcurrentHashMap<>();
        final ConcurrentMap<String, Integer> serialC = new ConcurrentHashMap<>();
        ruleList.forEach(rule -> {
            final String code = this.ruleIndent(rule);
            Integer counter = serialC.get(code);
            if (Objects.isNull(counter)) {
                serialC.put(code, 1);
            } else {
                counter = counter + 1;
                serialC.put(code, counter);
            }
        });
        final XActivityRule rule = ruleList.iterator().next();
        serialC.forEach((code, size) ->
            serialQ.put(code, Ux.channel(Indent.class, ConcurrentLinkedQueue::new, stub -> stub.indent(code, rule.getSigma(), size))));
        return Fn.combineM(serialQ).compose(Ux::future);
    }

    private String ruleIndent(final XActivityRule rule) {
        final JsonObject config = Ut.toJObject(rule.getRuleConfig());
        final JsonObject initData = Ut.valueJObject(config, KName.DATA);
        final String code = initData.getString(KName.INDENT);
        if (Ut.isNil(code)) {
            throw new _501IndentMissingException(this.getClass(), initData);
        }
        return code;
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
