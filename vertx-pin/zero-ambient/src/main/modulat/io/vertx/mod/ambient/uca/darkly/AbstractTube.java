package io.vertx.mod.ambient.uca.darkly;

import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.aeon.experiment.mixture.HLoadSmart;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.compare.Vs;
import io.modello.specification.action.HLoad;
import io.modello.specification.atom.HAtom;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.wffs.Playbook;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import static io.vertx.mod.ambient.refine.At.LOG;

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
        final XActivity activity = new XActivity();
        /*
         * key          ( System Generated )
         * type         ( Came from Rule )
         */
        activity.setKey(UUID.randomUUID().toString());
        activity.setType(rule.getType());
        activity.setSerial(data.getString(KName.Flow.TRACE_SERIAL));
        data.remove(KName.Flow.TRACE_SERIAL);
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
         * LocalDateTime processing
         */
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());
        /*
         * serial       ( System Generated )
         */
        // Message Processing
        final Playbook playbook = Playbook.open(rule.getRuleMessage());
        return playbook.format(data).compose(description -> {
            activity.setDescription(description);
            return Ux.future(activity);
        });
    }

    protected Future<JsonObject> diffAsync(final JsonObject data, final XActivityRule rule,
                                           final String field,
                                           final Supplier<Future<JsonObject>> executor) {
        final HAtom atom = this.atom(rule);
        final Vs vs = atom.vs();
        // Processing the data
        final JsonObject dataN = Ut.aiDataN(data);
        final JsonObject dataO = Ut.aiDataO(data);
        if (vs.isChange(dataO.getValue(field), dataN.getValue(field), field)) {
            return executor.get();
        } else {
            LOG.Flow.info(this.getClass(), "The field = {0} of Atom (  identifier = {1} ) has not been changed!",
                field, atom.identifier());
            return Ux.future(data);
        }
    }

    protected Future<JsonObject> sameAsync(final JsonObject data, final XActivityRule rule,
                                           final String field,
                                           final Supplier<Future<JsonObject>> executor) {
        final HAtom atom = this.atom(rule);
        final Vs vs = atom.vs();
        // Processing the data
        final JsonObject dataN = Ut.aiDataN(data);
        final JsonObject dataO = Ut.aiDataO(data);
        if (!vs.isChange(dataO.getValue(field), dataN.getValue(field), field)) {
            return executor.get();
        } else {
            LOG.Tabb.info(this.getClass(), "The field = {0} of Atom (  identifier = {1} ) has been changed!",
                field, atom.identifier());
            return Ux.future(data);
        }
    }
}
