package io.vertx.mod.workflow.uca.conformity;

import cn.vertxup.workflow.cv.em.PassWay;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.horizon.atom.common.Kv;
import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.runtime.WTask;
import io.vertx.mod.workflow.refine.Wf;
import io.vertx.mod.workflow.uca.camunda.Io;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

interface Gateway {
    ConcurrentMap<PassWay, Kv<String, Supplier<Gear>>> SUPPLIERS = new ConcurrentHashMap<>() {
        {
            this.put(PassWay.Standard, Kv.create(GearStandard.class.getName(), GearStandard::new));
            this.put(PassWay.Fork, Kv.create(GearForkJoin.class.getName(), GearForkJoin::new));
            this.put(PassWay.Multi, Kv.create(GearMulti.class.getName(), GearMulti::new));
            this.put(PassWay.Grid, Kv.create(GearGrid.class.getName(), GearGrid::new));
        }
    };
}

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractGear implements Gear {

    protected final JsonObject configuration = new JsonObject();
    protected PassWay type;

    protected AbstractGear(final PassWay type) {
        this.type = type;
    }

    @Override
    public Gear configuration(final JsonObject config) {
        if (Ut.isNotNil(config)) {
            this.configuration.mergeIn(config, true);
        }
        return this;
    }

    /*
     * Node -> Node ( Single )
     * The next task is only one
     */
    @Override
    public Future<WTask> taskAsync(final ProcessInstance instance, final Task from) {
        /*
         * One Size
         */
        final Io<Task> io = Io.ioTask();
        return io.children(instance.getId()).compose(taskList -> {
            final WTask wTask = new WTask(this.type);
            if (Objects.isNull(from) || VValue.ONE == taskList.size()) {
                /*
                 * 1) Start Point
                 * 2) Single Task Fetch
                 */
                taskList.forEach(wTask::add);
            } else {
                // Search the next task and find into `taskList` to determine the running
                Wf.taskNext(from, taskList).forEach(wTask::add);
            }
            return Ux.future(wTask);
        });
    }

    protected void buildSerial(final WTodo todo, final WTicket ticket, final Integer sequence) {
        // Based On SerialFork
        final String serialFork = todo.getSerialFork();
        final StringBuilder serialBuf = new StringBuilder();
        serialBuf.append(ticket.getSerial()).append(VString.DASH);
        serialBuf.append(Ut.fromAdjust(todo.getTraceOrder(), 2));
        if (Ut.isNil(serialFork)) {
            serialBuf.append(VValue.ZERO);
            if (Objects.nonNull(sequence)) {
                // XXX-010-01
                serialBuf.append(VString.DASH).append(Ut.fromAdjust(sequence, 2));
            } // else = XXX-010
        } else {
            if (Objects.isNull(sequence)) {
                // XXX-011
                serialBuf.append(serialFork);
            } else {
                // XXX-011-01
                serialBuf.append(serialFork).append(VString.DASH).append(Ut.fromAdjust(sequence, 2));
            }
        }

        todo.setCode(serialBuf.toString());
        todo.setSerial(todo.getCode());
    }
}
