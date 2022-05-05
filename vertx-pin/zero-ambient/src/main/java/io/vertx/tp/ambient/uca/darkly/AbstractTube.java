package io.vertx.tp.ambient.uca.darkly;

import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._501IndentMissingException;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.experiment.mixture.HLoad;
import io.vertx.up.experiment.mixture.HLoadSmart;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.uca.wffs.Playbook;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.UUID;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractTube implements Tube {
    private static final Cc<String, HLoad> CC_SMART = Cc.openThread();

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

    protected Future<XActivity> newActivity(final JsonObject data, final XActivityRule rule) {
        final JsonObject config = Ut.toJObject(rule.getRuleConfig());
        final JsonObject initData = Ut.valueJObject(config, KName.DATA);
        final String code = initData.getString(KName.INDENT);
        if (Ut.isNil(code)) {
            return Ux.thenError(_501IndentMissingException.class, this.getClass(), initData);
        }
        final XActivity activity = new XActivity();
        /*
         * key          ( System Generated )
         * type         ( Came from Rule )
         */
        activity.setKey(UUID.randomUUID().toString());
        activity.setType(rule.getType());
        {
            /*
             * taskSerial
             * taskName
             * modelKey
             */
            activity.setTaskName(rule.getRuleName());
            activity.setTaskSerial(data.getString(KName.Flow.TASK_SERIAL));

            if (data.containsKey(KName.Flow.TRACE_ID)) {
                // traceId reflect w.ticket key ( First Priority )
                activity.setModelKey(data.getString(KName.Flow.TRACE_ID));
            } else {
                // key reflect todo key ( Secondary Priority )
                activity.setModelKey(data.getString(KName.KEY));
            }
        }
        /*
         * 8 normalized fields
         *      - active
         *      - language
         *      - sigma
         *      - metadata
         *      - updatedAt
         *      - updatedBy
         *      - createdAt
         *      - createdBy
         */
        Ke.umCreated(activity, data);
        /*
         * serial       ( System Generated )
         */
        return Ke.umIndent(activity, rule.getSigma(), code, XActivity::setSerial).compose(normalized -> {
            final Playbook playbook = Playbook.open(rule.getRuleExpression());
            return playbook.format(data).compose(description -> {
                normalized.setDescription(description);
                return Ux.future(normalized);
            });
        });
        /*
         * After this method, Required:
         * [x] modelId
         * [x] recordOld
         * [x] recordNew
         */
    }
}
