package io.vertx.tp.workflow.plugin.activity;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.feature.Valve;
import io.vertx.tp.workflow.refine.Wf;
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
        final JsonObject normalized = data.copy();
        return this.dataCond(normalized)
            .compose(criteria -> {
                // Criteria
                normalized.put(Qr.KEY_CRITERIA, criteria);
                return Ux.future(normalized);
            })
            .compose(this::dataFlow).compose(workflow -> {
                // Workflow
                normalized.put(KName.Flow.WORKFLOW, workflow);
                return Ux.future(normalized);
            })
            /*
             * {
             *     "__data": {},
             *     "__flag": xxx,
             *     "workflow": {},
             *     "criteria": {}
             * }
             */
            .compose(processed -> this.dataDelay(processed, config))
            .compose(processed -> Ux.channel(Valve.class,
                /*
                 * Returned original JsonObject
                 */
                () -> data, valve -> valve.execAsync(processed, config)
            )).compose(nil -> Ux.future(data));
    }

    /*
     * All fields are as following:
     *
     * 1) User Part
     * - toUser
     * - createdBy
     * - updatedBy
     * - owner
     * - supervisor
     * - cancelBy
     * - openBy
     * - closeBy
     * - finishedBy
     * - assignedBy
     * - acceptedBy
     *
     * 2) Dept/Company/Team Part
     * - toDept
     * - toTeam
     * - toRole
     * - toGroup
     */
    private Future<JsonObject> dataDelay(final JsonObject normalized, final JsonObject config) {
        /*
         * Collect all User fields:
         * These fields will be mapped to __user here as metadata part
         * It could be used in Script Engine only
         * __user data structure
         * {
         *     "user": [
         *     ],
         *     "role": [
         *     ],
         *     "group": [
         *     ],
         *     "dept": [
         *     ],
         *     "team": [
         *     ]
         * }
         */
        final JsonObject user = new JsonObject();
        final JsonArray auditorJ = Ut.valueJArray(config, KName.USER);
        auditorJ.addAll(Ut.toJArray(KName.Flow.FIELD_AUDITOR));
        user.put(KName.USER, auditorJ);
        user.put(KName.ROLE, new JsonArray()
            .add(KName.Flow.Auditor.TO_ROLE)
        );
        user.put(KName.GROUP, new JsonArray()
            .add(KName.Flow.Auditor.TO_GROUP)
        );
        user.put(KName.DEPT, new JsonArray()
            .add(KName.Flow.Auditor.TO_DEPT)
        );
        user.put(KName.TEAM, new JsonArray()
            .add(KName.Flow.Auditor.TO_TEAM)
        );
        normalized.put(KName.__.USER, user);
        return Ux.future(normalized);
    }

    private Future<JsonObject> dataCond(final JsonObject data) {
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
        return Ux.future(criteria);
    }

    private Future<JsonObject> dataFlow(final JsonObject data) {
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
        /*
         * Process Definition Extract
         */
        final String definitionKey = workflow.getString(KName.Flow.DEFINITION_KEY);
        return Wf.definitionByKey(definitionKey).compose(processDefinition -> {
            workflow.put(KName.NAME, processDefinition.getName());
            return Ux.future(workflow);
        });
    }
}
