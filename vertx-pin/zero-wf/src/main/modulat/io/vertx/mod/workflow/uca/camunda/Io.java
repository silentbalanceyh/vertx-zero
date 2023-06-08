package io.vertx.mod.workflow.uca.camunda;

import cn.vertxup.workflow.cv.WfPool;
import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

import java.util.List;
import java.util.Set;

/**
 * Two situations:
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public interface Io<I> extends IoDefine, IoRuntime<I> {
    /*
     * 1) Bpmn Definition Method:
     *    pElements
     *    pElement
     * 2) Process Definition
     *    pDefinition
     *    pInstance
     *    pHistory
     * 3) Common Runtime Instances:
     *    start
     *    run
     *    end
     */
    static Io<StartEvent> ioEventStart() {
        return WfPool.CC_IO.pick(IoEventStart::new, IoEventStart.class.getName());
    }

    static Io<EndEvent> ioEventEnd() {
        return WfPool.CC_IO.pick(IoEventEnd::new, IoEventEnd.class.getName());
    }

    static Io<Void> io() {
        return WfPool.CC_IO.pick(IoVoid::new, IoVoid.class.getName());
    }

    static Io<JsonObject> ioForm() {
        return WfPool.CC_IO.pick(IoForm::new, IoForm.class.getName());
    }

    static Io<JsonObject> ioFlow() {
        return WfPool.CC_IO.pick(IoFlow::new, IoFlow.class.getName());
    }

    static Io<Set<String>> ioHistory() {
        return WfPool.CC_IO.pick(IoHistory::new, IoHistory.class.getName());
    }

    static Io<Task> ioTask() {
        return ioTask(true);
    }

    static Io<Task> ioTask(final boolean active) {
        if (active) {
            return WfPool.CC_IO.pick(IoTask::new, IoTask.class.getName());
        } else {
            return WfPool.CC_IO.pick(IoTaskKo::new, IoTaskKo.class.getName());
        }
    }
}

interface IoRuntime<I> {
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
     *
     */
    // Definition Level
    default Future<I> run(final ProcessInstance instance) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }

    // Process / Task Level
    default Future<I> run(final Task task) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }

    // Task Level
    default Future<I> run(final String iKey) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }

    default Future<I> start(final ProcessDefinition definition) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }

    default Future<I> end(final HistoricProcessInstance instance) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }

    // --------------------- Output Method ------------------
    default Future<JsonObject> out(final JsonObject workflow, final I i) {
        return Ux.futureJ(workflow);
    }

    default Future<JsonObject> out(final JsonObject workflow, final List<I> i) {
        return Ux.futureJ(workflow);
    }

    // ---------------- Child Fetching -----------------
    default Future<List<I>> children(final String oKey) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }

    default Future<I> child(final String oKey) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }
}

interface IoDefine {
    /*
     * Fetch ProcessDefinition              : id -> key
     * Fetch ProcessInstance                : id
     * Fetch HistoricProcessInstance        : id
     */
    ProcessDefinition inProcess(String idOrKey);

    ProcessInstance inInstance(String instanceId);

    HistoricProcessInstance inHistoric(String instanceId);
}
