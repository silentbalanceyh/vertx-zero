package io.vertx.mod.workflow.uca.camunda;

import io.horizon.eon.VValue;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.error._409EventEndUniqueException;
import io.vertx.mod.workflow.error._501EventEndMissingException;
import io.vertx.mod.workflow.init.WfPin;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
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
            return Fn.outWeb(_501EventEndMissingException.class, this.getClass(), definitionId);
        }
        return Ux.future(new ArrayList<>(ends));
    }


    // 「IoBpmn」ProcessDefinition -> EndEvent
    @Override
    public Future<EndEvent> child(final String definitionId) {
        return this.children(definitionId).compose(list -> {
            final int size = list.size();
            if (VValue.ONE == size) {
                return Ux.future(list.get(VValue.IDX));
            } else {
                return Fn.outWeb(_409EventEndUniqueException.class, this.getClass(), size, definitionId);
            }
        });
    }

    @Override
    public Future<JsonObject> out(final JsonObject workflow, final List<EndEvent> ends) {
        if (1 == ends.size()) {
            final EndEvent event = ends.get(VValue.IDX);
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
