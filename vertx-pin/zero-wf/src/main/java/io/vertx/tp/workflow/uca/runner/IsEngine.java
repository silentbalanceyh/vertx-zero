package io.vertx.tp.workflow.uca.runner;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.impl.BpmnModelConstants;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IsEngine implements IsOn {
    private final transient KitEvent typed;
    private final transient KitHistory history;

    IsEngine() {
        this.typed = new KitEvent();
        this.history = new KitHistory();
    }

    @Override
    public boolean isUserEvent(final Task task) {
        if (Objects.isNull(task)) {
            return Boolean.FALSE;
        } else {
            final String eventType = this.typed.type(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
            return BpmnModelConstants.BPMN_ELEMENT_USER_TASK.equals(eventType);
        }
    }

    @Override
    public boolean isEnd(final ProcessInstance instance) {
        return Objects.nonNull(this.history.instance(instance.getId()));
    }
}
