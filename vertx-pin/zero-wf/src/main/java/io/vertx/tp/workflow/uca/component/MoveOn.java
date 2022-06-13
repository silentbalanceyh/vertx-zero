package io.vertx.tp.workflow.uca.component;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.Future;
import io.vertx.tp.error._404RunOnSupplierException;
import io.vertx.tp.error._500EventTypeNullException;
import io.vertx.tp.workflow.atom.runtime.WMove;
import io.vertx.tp.workflow.atom.runtime.WRecord;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.atom.runtime.WTransition;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.central.Behaviour;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.impl.BpmnModelConstants;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Pool {
    ConcurrentMap<String, Supplier<MoveOn>> SUPPLIER = new ConcurrentHashMap<>() {
        {
            // UserTask
            this.put(BpmnModelConstants.BPMN_ELEMENT_USER_TASK, MoveOnUser::new);
        }
    };
}

public interface MoveOn extends Behaviour {

    static Future<MoveOn> event(final Task task) {
        Objects.requireNonNull(task);
        final String eventType = Wf.nameEvent(task);
        if (Objects.isNull(eventType)) {
            // Error-80606: event type could not be parsed and extracted from task
            return Ux.thenError(_500EventTypeNullException.class, MoveOn.class, task.getTaskDefinitionKey());
        }

        final Supplier<MoveOn> supplier = Pool.SUPPLIER.getOrDefault(eventType, null);
        if (Objects.isNull(supplier)) {
            // Error-80607: The supplier of event type could not be found.
            return Ux.thenError(_404RunOnSupplierException.class, MoveOn.class, eventType);
        }
        final MoveOn moveOn = supplier.get();
        Wf.Log.infoWeb(MoveOn.class, "Divert {0} has been selected, type = {0}",
            moveOn.getClass(), eventType);
        return Ux.future(moveOn);
    }

    static MoveOn instance(final Class<?> divertCls) {
        final MoveOn moveOn = WfPool.CC_MOVE_ON.pick(() -> Ut.instance(divertCls), divertCls.getName());
        Wf.Log.infoWeb(MoveOn.class, "Divert {0} has been selected", moveOn.getClass());
        return moveOn;
    }

    MoveOn bind(ConcurrentMap<String, WMove> moveMap);

    /*
     *  Event Fire by Programming
     */
    default Future<WRecord> transferAsync(final WRequest request, final WTransition process) {
        return Ux.thenError(_501NotSupportException.class, this.getClass());
    }

    default Future<WTransition> moveAsync(final WRequest request, final WTransition process) {
        return Ux.thenError(_501NotSupportException.class, this.getClass());
    }
}
