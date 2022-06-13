package io.vertx.tp.workflow.uca.camunda;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._409EventStartUniqueException;
import io.vertx.tp.error._501EventStartMissingException;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Values;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IoEventStart extends AbstractIo<StartEvent> {


    // 「IoBpmn」ProcessDefinition -> List<StartEvent>
    @Override
    public Future<List<StartEvent>> children(final String definitionId) {
        if (Ut.isNil(definitionId)) {
            return Ux.futureL();
        }
        final RepositoryService service = WfPin.camundaRepository();
        final BpmnModelInstance instance = service.getBpmnModelInstance(definitionId);
        final Collection<StartEvent> starts = instance.getModelElementsByType(StartEvent.class);
        if (starts.isEmpty()) {
            return Ux.thenError(_501EventStartMissingException.class, this.getClass(), definitionId);
        }
        return Ux.future(new ArrayList<>(starts));
    }


    // 「IoBpmn」ProcessDefinition -> StartEvent
    @Override
    public Future<StartEvent> child(final String definitionId) {
        return this.children(definitionId).compose(list -> {
            final int size = list.size();
            if (Values.ONE == size) {
                return Ux.future(list.get(Values.IDX));
            } else {
                return Ux.thenError(_409EventStartUniqueException.class, this.getClass(), size, definitionId);
            }
        });
    }

    @Override
    public Future<JsonObject> out(final JsonObject workflow, final List<StartEvent> starts) {
        if (1 == starts.size()) {
            final StartEvent event = starts.get(Values.IDX);
            /*
             * task:        id
             * taskName:    name
             */
            workflow.put(KName.Flow.TASK, event.getId());
            workflow.put(KName.Flow.TASK_NAME, event.getName());
        } else {
            final JsonObject taskMap = new JsonObject();
            starts.forEach(start -> taskMap.put(start.getId(), start.getName()));
            /*
             * id1:      name1
             * id2:      name2
             */
            workflow.put(KName.Flow.TASK, taskMap);
        }
        return Ux.future(workflow);
    }
}
