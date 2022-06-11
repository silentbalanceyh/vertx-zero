package io.vertx.tp.workflow.uca.coadjutor;

import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.tp.workflow.uca.camunda.RunOn;
import io.vertx.tp.workflow.uca.central.AbstractMovement;
import io.vertx.tp.workflow.uca.central.AidData;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class StayClose extends AbstractMovement implements Stay {
    @Override
    public Future<WRecord> keepAsync(final WRequest request, final WProcess wProcess) {
        /*
         * Instance deleting, but fetch the history and stored into `metadata` field as the final processing
         * Cancel for W_TODO and Camunda
         */
        final EventOn event = EventOn.get();
        final ProcessInstance instance = wProcess.instance();
        return event.taskHistory(instance).compose(historySet -> {
            // Cancel data processing
            final JsonObject todoData = AidData.closeJ(request.request(), wProcess, historySet);
            return this.updateAsync(todoData);
        }).compose(record -> {
            // Remove ProcessDefinition
            final RunOn runOn = RunOn.get();
            return runOn.stopAsync(instance, TodoStatus.FINISHED).compose(removed -> Ux.future(record));
        }).compose(record -> this.afterAsync(record, wProcess));
    }
}
