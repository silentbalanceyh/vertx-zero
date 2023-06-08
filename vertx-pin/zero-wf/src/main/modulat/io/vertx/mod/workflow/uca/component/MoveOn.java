package io.vertx.mod.workflow.uca.component;

import cn.vertxup.workflow.cv.WfPool;
import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Future;
import io.vertx.mod.workflow.atom.runtime.WRecord;
import io.vertx.mod.workflow.atom.runtime.WRequest;
import io.vertx.mod.workflow.atom.runtime.WTransition;
import io.vertx.mod.workflow.error._404RunOnSupplierException;
import io.vertx.mod.workflow.error._500EventTypeNullException;
import io.vertx.mod.workflow.refine.Wf;
import io.vertx.mod.workflow.uca.central.Behaviour;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.impl.BpmnModelConstants;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import static io.vertx.mod.workflow.refine.Wf.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Pool {
    ConcurrentMap<String, Supplier<MoveOn>> SUPPLIER = new ConcurrentHashMap<>() {
        {
            // UserTask
            this.put(BpmnModelConstants.BPMN_ELEMENT_USER_TASK, MoveOnGenerate::new);
        }
    };
}

public interface MoveOn extends Behaviour {

    static Future<MoveOn> event(final Task task) {
        Objects.requireNonNull(task);
        final String eventType = Wf.nameEvent(task);
        if (Objects.isNull(eventType)) {
            // Error-80606: event type could not be parsed and extracted from task
            return Fn.outWeb(_500EventTypeNullException.class, MoveOn.class, task.getTaskDefinitionKey());
        }

        final Supplier<MoveOn> supplier = Pool.SUPPLIER.getOrDefault(eventType, null);
        if (Objects.isNull(supplier)) {
            // Error-80607: The supplier of event type could not be found.
            return Fn.outWeb(_404RunOnSupplierException.class, MoveOn.class, eventType);
        }
        final MoveOn moveOn = supplier.get();
        LOG.Web.info(MoveOn.class, "MoveOn {0} has been selected, type = {0}",
            moveOn.getClass(), eventType);
        return Ux.future(moveOn);
    }

    static MoveOn instance(final Class<?> moveOnCls) {
        final MoveOn moveOn = WfPool.CC_MOVE_ON.pick(() -> Ut.instance(moveOnCls), moveOnCls.getName());
        LOG.Web.info(MoveOn.class, "MoveOn {0} has been selected", moveOn.getClass());
        return moveOn;
    }

    /*
     *  Event Fire by Programming
     */
    default Future<WRecord> transferAsync(final WRequest request, final WTransition process) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }

    default Future<WTransition> moveAsync(final WRequest request, final WTransition process) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }
}
