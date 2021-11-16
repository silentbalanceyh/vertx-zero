package io.vertx.tp.workflow.plugin;

import cn.zeroup.macrocosm.cv.WfCv;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.tp.workflow.refine.Wf;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.camunda.bpm.engine.impl.pvm.runtime.ActivityInstanceState;

import java.util.Date;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FlowSequenceListener implements JavaDelegate {

    @Override
    public void execute(final DelegateExecution execution) throws Exception {
        // ActivityInstance
        final HistoricActivityInstanceEntity instance = new HistoricActivityInstanceEntity();
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
         */
        instance.setExecutionId(execution.getId());
        instance.setTenantId(execution.getTenantId());

        /*
         * eventType
         * startTime
         * endTime
         * durationInMillis
         */
        instance.setStartTime(new Date());
        instance.setEndTime(new Date());
        instance.setDurationInMillis(0L);
        instance.setEventType(execution.getEventName());

        /*
         * parentActivityInstanceId
         * processDefinitionId
         * processInstanceId
         */
        instance.setProcessDefinitionId(execution.getProcessDefinitionId());
        instance.setProcessInstanceId(execution.getProcessInstanceId());
        instance.setParentActivityInstanceId(execution.getParentActivityInstanceId());
        WfPin.handlerHistory().handleEvent(instance);
        Wf.Log.infoMove(this.getClass(), "( History ) `{0}` history generated {1}", instance.getActivityType(), instance.getActivityId());
    }
}
