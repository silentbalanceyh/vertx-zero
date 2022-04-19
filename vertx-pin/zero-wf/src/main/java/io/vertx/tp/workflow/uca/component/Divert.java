package io.vertx.tp.workflow.uca.component;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.Future;
import io.vertx.tp.error._404DivertSupplierException;
import io.vertx.tp.error._500EventTypeNullException;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.tp.workflow.uca.runner.AidOn;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.fn.Fn;
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
    ConcurrentMap<String, Supplier<Divert>> SUPPLIER = new ConcurrentHashMap<>() {
        {
            // UserTask
            this.put(BpmnModelConstants.BPMN_ELEMENT_USER_TASK, DivertUser::new);
        }
    };
}

public interface Divert extends Behaviour {

    static Future<Divert> event(final Task task) {
        Objects.requireNonNull(task);
        final AidOn is = AidOn.get();
        final String type = is.taskType(task);
        if (Objects.isNull(type)) {
            // Error-80606: event type could not be parsed and extracted from task
            return Ux.thenError(_500EventTypeNullException.class, Divert.class, task.getTaskDefinitionKey());
        }

        final Supplier<Divert> supplier = Pool.SUPPLIER.getOrDefault(type, null);
        if (Objects.isNull(supplier)) {
            // Error-80607: The supplier of event type could not be found.
            return Ux.thenError(_404DivertSupplierException.class, Divert.class, type);
        }
        return Ux.future(supplier.get());
    }

    static Divert instance(final Class<?> divertCls) {
        return Fn.poolThread(WfPool.POOL_DIVERT, () -> Ut.instance(divertCls), divertCls.getName());
    }

    Divert bind(ConcurrentMap<String, WMove> moveMap);

    /*
     *  Event Fire by Programming
     */
    default Future<WRecord> transferAsync(final WRequest request, final WProcess process) {
        return Ux.thenError(_501NotSupportException.class, this.getClass());
    }

    default Future<WProcess> moveAsync(final WRequest request, final WProcess process) {
        return Ux.thenError(_501NotSupportException.class, this.getClass());
    }
}
