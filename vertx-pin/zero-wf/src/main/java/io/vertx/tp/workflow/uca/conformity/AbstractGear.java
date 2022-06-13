package io.vertx.tp.workflow.uca.conformity;

import cn.zeroup.macrocosm.cv.em.NodeType;
import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.runtime.WTask;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.up.atom.Kv;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

interface GearSupplier {
    ConcurrentMap<NodeType, Kv<String, Supplier<Gear>>> SUPPLIERS = new ConcurrentHashMap<>() {
        {
            this.put(NodeType.Standard, Kv.create(GearStandard.class.getName(), GearStandard::new));
            this.put(NodeType.Fork, Kv.create(GearForkJoin.class.getName(), GearForkJoin::new));
            this.put(NodeType.Multi, Kv.create(GearMulti.class.getName(), GearMulti::new));
            this.put(NodeType.Grid, Kv.create(GearGrid.class.getName(), GearGrid::new));
        }
    };
}

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractGear implements Gear {

    protected NodeType type;

    protected AbstractGear(final NodeType type) {
        this.type = type;
    }

    /*
     * Node -> Node ( Single )
     * The next task is only one
     */
    @Override
    public Future<WTask> taskAsync(final ProcessInstance instance) {
        /*
         * One Size
         */
        final Io<Task> io = Io.ioTask();
        return io.children(instance.getId()).compose(taskList -> {
            final WTask wTask = new WTask(this.type);
            taskList.forEach(wTask::add);
            return Ux.future(wTask);
        });
    }
}
