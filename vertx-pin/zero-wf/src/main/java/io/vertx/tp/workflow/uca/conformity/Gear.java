package io.vertx.tp.workflow.uca.conformity;

import cn.zeroup.macrocosm.cv.em.NodeType;
import io.vertx.core.json.JsonObject;
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
    static Gear instance(final NodeType type) {
        final Gear gear;
        if (Objects.isNull(type)) {
            // MoveMode is null;
            gear = CC_GEAR.pick(GearStandard::new, GearStandard.class.getName());
            Wf.Log.infoInit(Gear.class,
                "( Gear ) <NodeType Null> Component Initialized: {0}", gear.getClass());
            return gear;
        }
        if (NodeType.Fork == type) {
            // Fork/Join
            gear = CC_GEAR.pick(GearForkJoin::new, GearForkJoin.class.getName());
        } else if (NodeType.Multi == type) {
            // Multi
            gear = CC_GEAR.pick(GearMulti::new, GearMulti.class.getName());
        } else {
            // Standard
            gear = CC_GEAR.pick(GearStandard::new, GearStandard.class.getName());
        }
        Wf.Log.infoInit(Gear.class,
            "( Gear ) Component Initialized: {0}, Mode = {1}", gear.getClass(), type);
        return gear;
    }

    default Gear configuration(final JsonObject config) {
        return this;
    }
}
