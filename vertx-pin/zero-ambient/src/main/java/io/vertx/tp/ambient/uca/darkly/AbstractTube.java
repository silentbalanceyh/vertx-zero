package io.vertx.tp.ambient.uca.darkly;

import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.NormIndent;
import io.vertx.tp.optic.environment.Indent;
import io.vertx.up.eon.KName;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.experiment.mixture.HLoad;
import io.vertx.up.experiment.mixture.HLoadSmart;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.UUID;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractTube implements Tube {
    private static final Cc<String, HLoad> CC_SMART = Cc.openThread();
    private static final Cc<String, Indent> CC_INDENT = Cc.openThread();

    protected HAtom atom(final XActivityRule rule) {
        // Double-Checking for `rule`
        if (Objects.isNull(rule)) {
            return null;
        }
        if (Objects.isNull(rule.getRuleIdentifier())) {
            return null;
        }
        final HLoad loader = CC_SMART.pick(HLoadSmart::new, HLoadSmart.class.getName());
        /*
         * 「Multi App」
         * Ns may be `null`, here the design must not be break, it means that:
         * 1. When the `ns` is null, the system could run in standalone, it means that the database contain
         *    single application information here.
         *    When you want to run multi-app in one database, you must provide namespace to avoid conflicts.
         * 2. Here the multi-app structure will be used in future, in this situation the `ns` could identify
         *    the application model rules.
         */
        return loader.atom(rule.getRuleNs(), rule.getRuleIdentifier());
    }

    protected Future<XActivity> newActivity(final XActivityRule rule) {
        final JsonObject config = Ut.toJObject(rule.getRuleConfig());
        final JsonObject initData = Ut.valueJObject(config, KName.DATA);
        final String code = initData.getString(KName.INDENT);
        final XActivity activity = new XActivity();
        activity.setKey(UUID.randomUUID().toString());
        if (Ut.isNil(code)) {
            return Ux.future(activity);
        } else {
            final Indent indent = CC_INDENT.pick(NormIndent::new);
            return indent.indent(code, rule.getSigma()).compose(generated -> {
                activity.setSerial(generated);
                return Ux.future(activity);
            });
        }
    }
}
