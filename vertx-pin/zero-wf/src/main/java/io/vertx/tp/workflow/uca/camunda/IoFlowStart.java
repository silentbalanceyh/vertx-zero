package io.vertx.tp.workflow.uca.camunda;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.refine.Wf;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

/**
 * S - Start Flow
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IoFlowStart extends AbstractIo<JsonObject, ProcessDefinition> {
    /*
     * Workflow Output
     * {
     *      "definitionId": "???",
     *      "definitionKey": "???",
     *      "bpmn": "???",
     *      "name": "???",
     *      "task": "???",
     *      "multiple": "???"
     * }
     */
    @Override
    public Future<JsonObject> downOne(final String definitionId) {
        final ProcessDefinition definition = this.pDefinition(definitionId);
        final JsonObject workflow = Wf.bpmnOut(definition);
        final Io<StartEvent, ProcessDefinition> io = Io.ioEventStart();
        return io.downOne(definition.getId())
            /*
             * {
             *      "task": "???",
             *      "multiple": "???"
             * }
             */
            .compose(starts -> io.out(workflow, starts));
    }
}
