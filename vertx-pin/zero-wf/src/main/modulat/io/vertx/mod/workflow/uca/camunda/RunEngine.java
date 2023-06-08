package io.vertx.mod.workflow.uca.camunda;

import cn.vertxup.workflow.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.runtime.WTransition;
import io.vertx.mod.workflow.init.WfPin;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.camunda.bpm.engine.task.Task;

import java.util.Objects;

import static io.vertx.mod.workflow.refine.Wf.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class RunEngine implements RunOn {
    @Override
    public Future<ProcessInstance> moveAsync(final JsonObject params, final WTransition transition) {
        final TaskService service = WfPin.camundaTask();
        final Task task = transition.from();
        Objects.requireNonNull(task);
        service.complete(task.getId(), Ut.toMap(params));
        final ProcessInstance instance = transition.instance();
        LOG.Move.info(this.getClass(), "[ Move ] Ended = {0}, `instance = {1}` moving with params = {2} !!!",
            instance.isEnded(), instance.getId(), params.encode());
        return Ux.future(instance);
    }

    @Override
    public Future<ProcessInstance> startAsync(final JsonObject params, final WTransition transition) {
        final ProcessDefinition definition = transition.definition();
        final RuntimeService service = WfPin.camundaRuntime();
        final ProcessInstantiationBuilder builder = service.createProcessInstanceByKey(definition.getKey());
        builder.setVariables(Ut.toMap(params));
        final ProcessInstance instance = builder.execute();
        LOG.Move.info(this.getClass(), "[ Start ] `instance = {0}` has been started with params = {1}!!!",
            instance.getId(), params.encode());
        return Ux.future(instance);
    }

    @Override
    public Future<Boolean> stopAsync(final TodoStatus status, final WTransition transition) {
        final TaskService service = WfPin.camundaTask();
        return transition.start().compose(started -> {
            final Task task = started.from();
            // Fix issue of: The task cannot be deleted because is part of a running process
            // OLD Code: service.deleteTask(task.getId(), status.name());
            /*
             * Here are template solution for task processing
             * 1. When the process:task = 1:1
             *    The ProcessInstance will be deleted after the task has been marked DELETED
             * 2. When the process:task = 1:n
             *    Here will be many tasks that have been marked DELETED
             */
            service.setAssignee(task.getId(), IoTaskKo.DELEGATE_DELETE);
            // Read all task information based on instance after deleted.
            final ProcessInstance instance = transition.instance();
            final Io<Task> keeps = Io.ioTask(false);
            return keeps.children(instance.getId()).compose(tasks -> {
                if (tasks.isEmpty()) {
                    // There is no active tasks lefts
                    final RuntimeService runtime = WfPin.camundaRuntime();
                    runtime.deleteProcessInstanceIfExists(instance.getId(), status.name(),
                        false, false, false, false);
                }
                return Ux.futureT();
            });
        });
    }
}
