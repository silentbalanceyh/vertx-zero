package io.vertx.tp.workflow.uca.runner;

import io.vertx.tp.workflow.init.WfPin;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricActivityInstanceQuery;
import org.camunda.bpm.engine.task.Task;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class EventTask {

    Task byInstanceId(final String instanceId) {
        final TaskService service = WfPin.camundaTask();
        return service.createTaskQuery()
            .initializeFormKeys()
            .processInstanceId(instanceId)
            .active().singleResult();
    }

    Task byTaskId(final String taskId) {
        final TaskService service = WfPin.camundaTask();
        return service.createTaskQuery()
            .taskId(taskId).singleResult();
    }

    Set<String> histories(final String instanceId) {
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
}
