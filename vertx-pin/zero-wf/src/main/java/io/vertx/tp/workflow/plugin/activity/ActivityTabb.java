package io.vertx.tp.workflow.plugin.activity;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.feature.Valve;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.uca.sectio.After;
import io.vertx.up.uca.sectio.Around;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Set;

/**
 * Plugin for
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ActivityTabb implements After {
    @Override
    public Set<ChangeFlag> types() {
        return Around.TYPE_ALL;
    }

    @Override
    public Future<JsonObject> afterAsync(final JsonObject data, final JsonObject config) {
        // Criteria
        final JsonObject normalized = data.copy();
        final JsonObject criteria = this.dataCond(normalized);
        final JsonObject workflow = this.dataFlow(normalized);
        normalized.put(KName.Flow.WORKFLOW, workflow);
        normalized.put(Qr.KEY_CRITERIA, criteria);
        /*
         * {
         *     "__data": {},
         *     "__flag": xxx,
         *     "workflow": {},
         *     "criteria": {}
         * }
         */
        return Ke.channel(Valve.class, () -> data, valve -> valve.execAsync(normalized, config))
            .compose(nil -> Ux.future(data));
    }

    private JsonObject dataCond(final JsonObject data) {
        /*
         * Build the condition of workflow
         * {
         *     "definitionKey": "workflow definition key",
         *     "taskKey": "task key",
         *     "": true
         * }
         *
         * The source field
         * {
         *     "flowDefinitionKey": "xxx",
         *     "taskKey": "xxx"
         * }
         */
        final JsonObject criteria = Ux.whereAnd();
        criteria.put(KName.Flow.DEFINITION_KEY, data.getValue(KName.Flow.FLOW_DEFINITION_KEY));
        criteria.put(KName.Flow.TASK_KEY, data.getValue(KName.Flow.TASK_KEY));
        return criteria;
    }

    private JsonObject dataFlow(final JsonObject data) {
        final JsonObject workflow = new JsonObject();
        Ut.elementCopy(workflow, data,
            // Source -> flow
            KName.Flow.FLOW_DEFINITION_KEY,
            KName.Flow.FLOW_DEFINITION_ID,
            KName.Flow.FLOW_INSTANCE_ID,
            // Source -> task
            KName.Flow.TASK_ID,
            KName.Flow.TASK_NAME,
            KName.Flow.TASK_SERIAL,
            KName.Flow.TASK_KEY,
            KName.Flow.TASK_CODE
        );
        /*
         *  flowDefinitionKey   -> definitionKey
         *  flowDefinitionId    -> definitionId
         *  flowInstanceId      -> instanceId
         */
        workflow.put(KName.Flow.INSTANCE_ID, data.getValue(KName.Flow.FLOW_INSTANCE_ID));
        workflow.put(KName.Flow.DEFINITION_KEY, data.getValue(KName.Flow.FLOW_DEFINITION_KEY));
        workflow.put(KName.Flow.DEFINITION_ID, data.getValue(KName.Flow.FLOW_DEFINITION_ID));
        return workflow;
    }
}
