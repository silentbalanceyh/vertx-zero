package io.vertx.tp.workflow.uca.conformity;

import cn.zeroup.macrocosm.cv.em.MoveMode;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.runtime.WMode;
import io.vertx.tp.workflow.atom.runtime.WMove;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.uca.cache.Cc;

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


        final WMode way = move.way();
        if (Objects.isNull(way)) {
            // MoveMode is null;
            gear = CC_GEAR.pick(GearStandard::new, GearStandard.class.getName());
            Wf.Log.infoInit(Gear.class,
                "( Gear ) <MoveMode Null> Component Initialized: {0}", gear.getClass());
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
}
