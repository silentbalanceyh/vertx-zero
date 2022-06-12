package io.vertx.tp.workflow.uca.camunda;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public interface Io<I, O> extends IoInternal {

    static Io<StartEvent, ProcessDefinition> ioEventStart() {
        return WfPool.CC_IO.pick(IoEventStart::new, IoEventStart.class.getName());
    }

    static Io<EndEvent, ProcessDefinition> ioEventEnd() {
        return WfPool.CC_IO.pick(IoEventEnd::new, IoEventEnd.class.getName());
    }

    static Io<Void, Void> io() {
        return WfPool.CC_IO.pick(IoVoid::new, IoVoid.class.getName());
    }

    static Io<FormData, ProcessDefinition> ioForm() {
        return WfPool.CC_IO.pick(IoForm::new, IoForm.class.getName());
    }

    static Io<JsonObject, ProcessDefinition> ioFlow() {
        return WfPool.CC_IO.pick(IoFlow::new, IoFlow.class.getName());
    }

    /*
     * Sync Method for following three types
     * - ProcessDefinition
     * - Task
     * - HistoricProcessInstance
     *
     * The T could be
     * 1. StartEvent                    - Based: Definition
     * 2. EndEvent                      - Based: Definition
     * 3. Form ( JsonObject )           - Based: Definition
     *                                           Task
     * 4. Workflow ( JsonObject )       - Based: Definition
     *                                           Task
     * 5. Task                          - Based: Instance
     * 6. Activities                    - Based: History Instance
     */
    default Future<I> run(final String iKey) {
        return Ux.thenError(_501NotSupportException.class, this.getClass());
    }

    default Future<I> start(final String iKey) {
        return Ux.thenError(_501NotSupportException.class, this.getClass());
    }

    default Future<I> end(final String iKey) {
        return Ux.thenError(_501NotSupportException.class, this.getClass());
    }

    // ---------------- Child Fetching -----------------
    // Child Element by Parent key
    default Future<List<I>> children(final String oKey) {
        return Ux.thenError(_501NotSupportException.class, this.getClass());
    }

    default Future<I> child(final String oKey) {
        return Ux.thenError(_501NotSupportException.class, this.getClass());
    }

    // --------------------- Output Method ------------------
    default Future<JsonObject> out(final JsonObject workflow, final I i) {
        return Ux.futureJ(workflow);
    }

    default Future<JsonObject> out(final JsonObject workflow, final List<I> i) {
        return Ux.futureJ(workflow);
    }
}

interface IoInternal {
    // Fetch ProcessDefinition              : id -> key
    ProcessDefinition pDefinition(String idOrKey);
    // Fetch ProcessInstance                : id

    ProcessInstance pInstance(String instanceId);
    // Fetch HistoricProcessInstance        : id

    HistoricProcessInstance pHistory(String instanceId);
}
