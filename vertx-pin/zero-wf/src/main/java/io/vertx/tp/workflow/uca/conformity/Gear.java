package io.vertx.tp.workflow.uca.conformity;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.MoveMode;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.runtime.WMove;
import io.vertx.tp.workflow.atom.runtime.WMoveWay;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.uca.cache.Cc;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.List;
import java.util.Objects;

/**
 * 1) Bind instance for Task seeking
 * 2) Fetch active Task
 *
 * This interface will be used internal WProcess for different mode
 *
 * 1. The WMove must be bind
 * 2. The ProcessInstance must be valid
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Gear {
    Cc<String, Gear> CC_GEAR = Cc.openThread();

    /*
     * ProcessInstance / WMove to processing
     * The final Gear instance
     */
    static Gear instance(final WMove move) {
        final Gear gear;


        if (Objects.isNull(move)) {
            // Move is null
            gear = CC_GEAR.pick(GearStandard::new, GearStandard.class.getName());
            Wf.Log.infoInit(Gear.class,
                "( Gear ) <Move Null> Component Initialized: {0}", gear.getClass());
            return gear;
        }


        final WMoveWay way = move.way();
        if (Objects.isNull(way)) {
            // MoveWay is null;
            gear = CC_GEAR.pick(GearStandard::new, GearStandard.class.getName());
            Wf.Log.infoInit(Gear.class,
                "( Gear ) <MoveWay Null> Component Initialized: {0}", gear.getClass());
            return gear;
        }


        final MoveMode mode = way.getType();
        if (MoveMode.Fork == mode) {
            // Fork/Join
            gear = CC_GEAR.pick(GearForkJoin::new, GearForkJoin.class.getName());
        } else if (MoveMode.Multi == mode) {
            // Multi
            gear = CC_GEAR.pick(GearMulti::new, GearMulti.class.getName());
        } else {
            // Standard
            gear = CC_GEAR.pick(GearStandard::new, GearStandard.class.getName());
        }
        Wf.Log.infoInit(Gear.class,
            "( Gear ) Component Initialized: {0}, Mode = {1}", gear.getClass(), mode);
        return gear.configuration(way.getConfig());
    }

    default Gear configuration(final JsonObject config) {
        return this;
    }

    /*
     * Step 1: Generation
     * This api is for todo record generation ( May be single / multi )
     */
    List<WTodo> todoInitialize(ProcessInstance instance, WTicket ticket);

    /*
     * Step 2: Fetch
     * Fetch Task instance by `taskId`
     */
    Task taskActive(String taskId);

    /*
     * Step 3: Fetch Next
     * Fetch next task list by current task ( May be closed )
     */
    List<Task> taskNext(Task task);

    /*
     * Step 4: Close / Cancel for task
     * 1. Close / Cancel current task
     * 2. Close / Cancel instance by calculating
     */
    boolean taskClose(String taskId, TodoStatus status);
}
