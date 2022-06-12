package io.vertx.tp.workflow.uca.camunda;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

/**
 * S - Start Flow
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IoFlow extends AbstractIo<JsonObject, ProcessDefinition> {

    // 「IoRuntime」ProcessDefinition before workflow start
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
    public Future<JsonObject> start(final String definitionId) {
        final ProcessDefinition definition = this.inProcess(definitionId);
        final JsonObject workflow = Wf.bpmnOut(definition);

        // Capture the definition from BPMN
        final Io<StartEvent, ProcessDefinition> io = Io.ioEventStart();
        return io.inElementChild(definition.getId())
            .compose(starts -> io.out(workflow, starts));
    }

    // 「IoRuntime」Instance when the workflow is running
    /*
     * Workflow Output
     * {
     *      "definitionId": "???",
     *      "definitionKey": "???",
     *      "bpmn": "???",
     *      "name": "???",
     *      "history": []
     * }
     */
    @Override
    public Future<JsonObject> run(final String instanceId) {
        return null;
    }

    @Override
    public Future<JsonObject> out(final JsonObject response, final JsonObject workflow) {
        if (Ut.notNil(workflow)) {
            response.put(KName.Flow.WORKFLOW, workflow);
        }
        return Ux.future(response);
    }
}
