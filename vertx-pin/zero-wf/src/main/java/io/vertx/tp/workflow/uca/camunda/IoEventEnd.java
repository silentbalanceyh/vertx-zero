package io.vertx.tp.workflow.uca.camunda;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._409EventEndUniqueException;
import io.vertx.tp.error._501EventEndMissingException;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Values;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.EndEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IoEventEnd extends AbstractIo<EndEvent> {

    // 「IoBpmn」ProcessDefinition -> List<EndEvent>
    @Override
    public Future<List<EndEvent>> children(final String definitionId) {
        if (Ut.isNil(definitionId)) {
            return Ux.futureL();
        }
        final RepositoryService service = WfPin.camundaRepository();
        final BpmnModelInstance instance = service.getBpmnModelInstance(definitionId);
        final Collection<EndEvent> ends = instance.getModelElementsByType(EndEvent.class);
        if (ends.isEmpty()) {
            return Ux.thenError(_501EventEndMissingException.class, this.getClass(), definitionId);
        }
        return Ux.future(new ArrayList<>(ends));
    }


    // 「IoBpmn」ProcessDefinition -> EndEvent
    @Override
    public Future<EndEvent> child(final String definitionId) {
        return this.children(definitionId).compose(list -> {
            final int size = list.size();
            if (Values.ONE == size) {
                return Ux.future(list.get(Values.IDX));
            } else {
                return Ux.thenError(_409EventEndUniqueException.class, this.getClass(), size, definitionId);
            }
        });
    }

    @Override
    public Future<JsonObject> out(final JsonObject workflow, final List<EndEvent> ends) {
        if (1 == ends.size()) {
            final EndEvent event = ends.get(Values.IDX);
            /*
             * task:        id
             * taskName:    name
             */
            workflow.put(KName.Flow.TASK, event.getId());
            workflow.put(KName.Flow.TASK_NAME, event.getName());
        } else {
            final JsonObject taskMap = new JsonObject();
            ends.forEach(start -> taskMap.put(start.getId(), start.getName()));
            /*
             * id1:      name1
             * id2:      name2
             */
            workflow.put(KName.Flow.TASK, taskMap);
        }
        return Ux.future(workflow);
    }
}
