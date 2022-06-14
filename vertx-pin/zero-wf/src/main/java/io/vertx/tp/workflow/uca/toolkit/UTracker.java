package io.vertx.tp.workflow.uca.toolkit;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.configuration.MetaInstance;
import io.vertx.tp.workflow.atom.runtime.WRecord;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.atom.runtime.WTransition;
import io.vertx.tp.workflow.plugin.activity.ActivityTabb;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.uca.sectio.Around;
import io.vertx.up.uca.sectio.Aspect;
import io.vertx.up.uca.sectio.AspectConfig;
import org.camunda.bpm.engine.task.Task;

import java.util.List;
import java.util.Objects;

/**
 * Tracker for different component history record
 * This tracker implementation is based on X_ACTIVITY_RULE
 *
 * 1) The default logging will be put into camunda
 * 2) Tracker is for business activities
 *
 * Internal call Journal interface for `zero-ambient` activity generation instead.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class UTracker {

    private final transient MetaInstance metadata;

    /*
     * Here must be noted that the record type inner WRequest
     * 1) Activity of Workflow ( Moving )
     *    before - input record
     *    after  - generated record
     * 2) Activity of Saving / Cancel
     *    before - input record
     *    after  - finished record
     *
     * All notification must be based on `after` instead of before such as:
     * - Notification
     * - Workflow Moving
     * - Start Job
     * - Change Activity ( after + before )
     *
     * The whole workflow should be:
     * 1) Movement -> Transfer
     *                Transfer ( Generated ) -> Generator
     * 2) Movement -> Stay
     *
     * In Camunda Workflow Engine, here before and after should be configured on
     * WMove rule node such as
     * {
     *     "e.start": {
     *         "data": { },
     *         "aspect": {
     *              "plugin.component.before": [],
     *              "plugin.component.after": [],
     *              "plugin.component.job": [],
     *              "plugin.config": {
     *              }
     *         }
     *     }
     * }
     *
     * 1) beforeAsync -> Movement -> Transfer -> afterAsync
     * 2) beforeAsync -> Movement -> Transfer -> afterAsync ->
     *                               beforeAsync -> Generator -> afterAsync
     * 3) beforeAsync -> Movement -> Stay     -> afterAsync
     *
     * Actually you can ignore Movement because it's only for Camunda Workflow Moving
     * The rule of tracker is for objective:
     * 1) Impact Request Data
     * 2) Callback after the whole operations
     */
    public UTracker(final MetaInstance metadata) {
        this.metadata = metadata;
    }

    public Future<WRequest> beforeAsync(final WRequest request, final WTransition transition) {
        // Build Aspect Component
        final Aspect aspect = this.aspect(transition);
        return aspect.wrapJBefore(Around.TYPE_ALL.toArray(new ChangeFlag[0]))
            .apply(request.request())
            .compose(request::future);
    }

    public Future<WRecord> afterAsync(final WRecord record, final WTransition transition) {
        // Build Aspect Component
        final Aspect aspect = this.aspect(transition);
        return aspect.wrapJAfter(Around.TYPE_ALL.toArray(new ChangeFlag[0]))
            .apply(aspectParameter(record, transition))
            .compose(record::futureAfter);
    }

    private JsonObject aspectParameter(final WRecord record, final WTransition process) {
        /*
         * Data Analyzing on
         * {
                "flowDefinitionKey": "process.asset.income",
                "flowDefinitionId": "process.asset.income:1:c175f471-c0fb-11ec-8c0e-161b1b9eb817",
                "flowInstanceId": "4fecbc57-c128-11ec-945b-161b1b9eb817",
                "flowEnd": false,

                "taskId": "4ff39a2f-c128-11ec-945b-161b1b9eb817",
                "taskKey": "e.draft",
                "taskCode": "WAI220421100003-01",
                "taskSerial": "WAI220421100003-01",
                "taskName": "xxxx"
         * }
         */
        final JsonObject parameters = record.data();
        {
            // Add new parameters of instance
            final Task task = process.from();
            if (Objects.nonNull(task)) {
                // Task Name
                parameters.put(KName.Flow.TASK_NAME, task.getName());
            }
        }
        return parameters;
    }

    private Aspect aspect(final WTransition transition) {
        final AspectConfig aspectConfig = transition.aspect();
        final List<Class<?>> afterList = aspectConfig.nameAfter();
        if (!afterList.contains(ActivityTabb.class)) {
            afterList.add(ActivityTabb.class);
            // Add External Configuration for current Component
            final JsonObject config = new JsonObject();
            config.put(KName.AUDITOR, this.metadata.childAuditor());
            aspectConfig.config(ActivityTabb.class, config);
        }
        Wf.Log.infoWeb(getClass(), "Aspect Config: {0}", aspectConfig.toString());
        return Aspect.create(aspectConfig);
    }
}
