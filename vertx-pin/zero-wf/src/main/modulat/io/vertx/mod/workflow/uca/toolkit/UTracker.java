package io.vertx.mod.workflow.uca.toolkit;

import io.horizon.eon.VValue;
import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.uca.aop.Around;
import io.horizon.uca.aop.Aspect;
import io.horizon.uca.aop.AspectRobin;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.mod.workflow.atom.runtime.WRecord;
import io.vertx.mod.workflow.atom.runtime.WRequest;
import io.vertx.mod.workflow.atom.runtime.WTransition;
import io.vertx.mod.workflow.plugin.activity.ActivityTabb;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.vertx.mod.workflow.refine.Wf.LOG;

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
        final JsonArray todo = record.dataAop();
        if (Ut.isNil(todo)) {
            return Ux.future(record);
        }
        if (VValue.ONE == todo.size()) {
            final JsonObject input = todo.getJsonObject(VValue.IDX);
            final Task task = transition.from();
            return aspect.wrapJAfter(Around.TYPE_ALL.toArray(new ChangeFlag[0]))
                .apply(aspectParameter(input, task))
                .compose(record::futureAfter);
        } else {
            final List<Future<JsonObject>> runner = new ArrayList<>();
            Ut.itJArray(todo).forEach(json -> {
                final Future<JsonObject> future = aspect.wrapJAfter(Around.TYPE_ALL.toArray(new ChangeFlag[0]))
                    .apply(json);
                runner.add(future);
            });
            return Fn.combineA(runner).compose(nil -> Ux.future(record));
        }
    }

    private JsonObject aspectParameter(final JsonObject parameters, final Task task) {
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
        // final JsonObject parameters = record.data();
        // Add new parameters of instance
        if (Objects.nonNull(task)) {
            // Task Name
            parameters.put(KName.Flow.TASK_NAME, task.getName());
        }
        return parameters;
    }

    private Aspect aspect(final WTransition transition) {
        final AspectRobin aspectConfig = transition.aspect();
        // Add External Configuration for current Component
        final JsonObject config = new JsonObject();
        config.put(KName.AUDITOR, this.metadata.childAuditor());
        aspectConfig.afterQueue(ActivityTabb.class, config);

        //        final List<Class<?>> afterList = aspectConfig.afterQueue();
        //        if (!afterList.contains(ActivityTabb.class)) {
        //            afterList.add(ActivityTabb.class);
        //            // Add External Configuration for current Component
        //            final JsonObject config = new JsonObject();
        //            config.put(KName.AUDITOR, this.metadata.childAuditor());
        //            aspectConfig.config(ActivityTabb.class, config);
        //        }
        LOG.Web.info(getClass(), "Aspect Config: {0}", aspectConfig.toString());
        return Aspect.create(aspectConfig);
    }
}
