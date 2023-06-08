package io.vertx.mod.workflow.uca.conformity;

import cn.vertxup.workflow.cv.em.PassWay;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.horizon.atom.common.Kv;
import io.horizon.exception.web._501NotSupportException;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.runtime.WTask;
import io.vertx.up.fn.Fn;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static io.vertx.mod.workflow.refine.Wf.LOG;

/**
 * 1) Bind instance for Task seeking
 * 2) Fetch active Task
 *
 * This interface will be used internal WProcess for different mode
 *
 * 1. The WMove must be bind
 * 2. The ProcessInstance must be valid
 *
 * * null value when processed
 * * 「Related」
 * *  - traceId
 * *  - traceOrder
 * *  - parentId
 * *
 * * 「Camunda」
 * *  - taskId
 * *  - taskKey
 * *
 * * 「Flow」
 * *  - assignedBy
 * *  - assignedAt
 * *  - acceptedBy
 * *  - acceptedAt
 * *  - finishedBy
 * *  - finishedAt
 * *  - comment
 * *  - commentApproval
 * *  - commentReject
 * *
 * * 「Future」
 * *  - metadata
 * *  - modelCategory
 * *  - activityId
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Gear {
    Cc<String, Gear> CC_GEAR = Cc.openThread();

    /*
     * ProcessInstance / WMove to processing
     * The final Gear instance
     */
    static Gear instance(final PassWay type) {
        final Gear gear;
        if (Objects.isNull(type) || !Gateway.SUPPLIERS.containsKey(type)) {
            // MoveMode is null;
            gear = CC_GEAR.pick(GearStandard::new, GearStandard.class.getName());
            LOG.Move.info(Gear.class,
                "( Gear ) <NodeType Null> Component Initialized: {0}", gear.getClass());
            return gear;
        }
        final Kv<String, Supplier<Gear>> kv = Gateway.SUPPLIERS.get(type);
        gear = CC_GEAR.pick(kv.value(), kv.key());
        LOG.Move.info(Gear.class,
            "( Gear ) Component Initialized: {0}, Mode = {1}", gear.getClass(), type);
        return gear;
    }

    default Gear configuration(final JsonObject config) {
        return this;
    }

    /*
     * Read the running ProcessInstance and capture the `active` tasks.
     */
    Future<WTask> taskAsync(ProcessInstance instance, Task from);

    default Future<List<WTodo>> todoAsync(final JsonObject parameters, final WTask wTask, final WTicket ticket) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }

    default Future<List<WTodo>> todoAsync(final JsonObject parameters, final WTask wTask, final WTicket ticket, final WTodo todo) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }
}
