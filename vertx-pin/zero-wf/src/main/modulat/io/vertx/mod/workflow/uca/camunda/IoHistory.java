package io.vertx.mod.workflow.uca.camunda;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.init.WfPin;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricActivityInstanceQuery;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IoHistory extends AbstractIo<Set<String>> {
    @Override
    public Future<Set<String>> run(final ProcessInstance instance) {
        return Ux.future(this.activities(instance.getId()));
    }

    @Override
    public Future<Set<String>> end(final HistoricProcessInstance instance) {
        return Ux.future(this.activities(instance.getId()));
    }

    private Set<String> activities(final String instanceId) {
        // HistoricActivityInstance -> List
        final HistoryService serviceH = WfPin.camundaHistory();
        final HistoricActivityInstanceQuery query = serviceH.createHistoricActivityInstanceQuery()
            .processInstanceId(instanceId);
        final List<HistoricActivityInstance> activities = query.list();
        final Set<String> historySet = new HashSet<>();
        /*
         * Capture Data here:
         * 1. Default `HistoricActivityInstance` contains node processing.
         * 2. Extension to set ExecutionListener to monitor the edge processing, user defined
         *    `HistoricActivityInstance` here.
         */
        activities.forEach(activity -> historySet.add(activity.getActivityId()));
        return historySet;
    }

    /*
     * Build Response:
     * {
     *     "history": []
     * }
     */
    @Override
    public Future<JsonObject> out(final JsonObject workflow, final Set<String> strings) {
        final JsonArray historyA = Ut.toJArray(strings);
        workflow.put(KName.HISTORY, historyA);
        return Ux.future(workflow);
    }
}
