package io.vertx.mod.workflow.uca.conformity;

import cn.vertxup.workflow.cv.em.PassWay;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.runtime.WTask;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.task.Task;

import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class GearStandard extends AbstractGear {

    public GearStandard() {
        super(PassWay.Standard);
    }

    @Override
    public Future<List<WTodo>> todoAsync(final JsonObject parameters, final WTask wTask, final WTicket ticket) {
        final Task task = wTask.standard();
        if (Objects.isNull(task)) {
            return Ux.futureL();
        }
        // 0. Keep the same acceptedBy / toUser value and do nothing
        final Gain starter = Gain.starter(ticket);
        return starter.buildAsync(parameters, task, null).compose(generated -> {
            // 1. Select Method to set Serial
            generated.setSerialFork(null);
            this.buildSerial(generated, ticket, null);

            return Ux.futureL(generated);
        });
    }

    @Override
    public Future<List<WTodo>> todoAsync(final JsonObject parameters, final WTask wTask, final WTicket ticket,
                                         final WTodo todo) {
        final Task task = wTask.standard();
        if (Objects.isNull(task)) {
            return Ux.futureL();
        }

        // 0. Pre-Assignment: toUser -> acceptedBy
        this.buildAssign(parameters);

        // 1. Generate new WTodo
        final Gain generator = Gain.generator(ticket);
        return generator.buildAsync(parameters, task, todo).compose(generated -> {
            // 2. Select Method to set Serial
            generated.setSerialFork(todo.getSerialFork());
            this.buildSerial(generated, ticket, null);

            return Ux.futureL(generated);
        });
    }

    private void buildAssign(final JsonObject parameters) {
        // toUser -> acceptedBy
        final String toUser = parameters.getString(KName.Auditor.TO_USER);
        parameters.put(KName.Auditor.ACCEPTED_BY, toUser);
        parameters.remove(KName.Auditor.TO_USER);
    }
}
