package io.vertx.tp.workflow.uca.coadjutor;

import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.runtime.WProcess;
import io.vertx.tp.workflow.atom.runtime.WRecord;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.tp.workflow.uca.camunda.RunOn;
import io.vertx.tp.workflow.uca.central.AbstractMovement;
import io.vertx.tp.workflow.uca.central.AidData;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class StayCancel extends AbstractMovement implements Stay {
    @Override
    public Future<WRecord> keepAsync(final WRequest request, final WProcess wProcess) {
        /*
         * Instance deleting, but fetch the history and stored into `metadata` field as the final processing
         * Cancel for W_TODO and Camunda
         */
        final ProcessInstance instance = wProcess.instance();
        final Io<Set<String>> ioHistory = Io.ioHistory();
        return ioHistory.run(instance.getId()).compose(historySet -> {
            // Cancel data processing
            final JsonObject todoData = AidData.cancelJ(request.request(), wProcess, historySet);
            return this.updateAsync(todoData);
        }).compose(record -> {
            // Remove ProcessDefinition
            final RunOn runOn = RunOn.get();
            return runOn.stopAsync(instance, TodoStatus.CANCELED).compose(removed -> Ux.future(record));
        }).compose(record -> this.afterAsync(record, wProcess));
    }
}
