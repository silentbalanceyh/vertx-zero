package io.vertx.mod.workflow.plugin;

import cn.vertxup.workflow.cv.WfCv;
import io.vertx.mod.workflow.init.WfPin;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity;
import org.camunda.bpm.engine.impl.history.event.HistoryEventTypes;
import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.camunda.bpm.engine.impl.pvm.runtime.ActivityInstanceState;

import java.util.Date;

import static io.vertx.mod.workflow.refine.Wf.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FlowSequenceListener implements JavaDelegate {

    @Override
    public void execute(final DelegateExecution execution) throws Exception {
        // ActivityInstance
        final HistoricActivityInstanceEventEntity instance = new HistoricActivityInstanceEventEntity();
        /*
         * activityId
         * activityName
         * activityType
         * activityInstanceId
         */
        instance.setActivityId(execution.getCurrentTransitionId());
        instance.setActivityType(WfCv.BPMN_FLOW_TYPE);
        instance.setActivityName(execution.getCurrentActivityName());
        instance.setActivityInstanceId(execution.getActivityInstanceId());
        instance.setActivityInstanceState(ActivityInstanceState.DEFAULT.getStateCode());
        /*
         * executionId
         * tenantId
         * eventType
         */
        instance.setExecutionId(execution.getId());
        instance.setTenantId(execution.getTenantId());
        instance.setEventType(HistoryEventTypes.ACTIVITY_INSTANCE_START.getEventName());
        /*
         * eventType
         * startTime
         * endTime
         * durationInMillis
         */
        instance.setStartTime(new Date());
        instance.setEndTime(new Date());
        instance.setDurationInMillis(0L);

        /*
         * parentActivityInstanceId
         * processDefinitionId
         * processInstanceId
         */
        instance.setProcessDefinitionId(execution.getProcessDefinitionId());
        instance.setProcessInstanceId(execution.getProcessInstanceId());
        instance.setParentActivityInstanceId(execution.getParentActivityInstanceId());
        final HistoryEventHandler handler = WfPin.camundaLogger();
        handler.handleEvent(instance);
        LOG.Plugin.info(this.getClass(), "[ History ] `{0}` history generated {1}", instance.getActivityType(), instance.getActivityId());
    }
}
