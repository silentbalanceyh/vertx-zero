package cn.zeroup.macrocosm.api;

import cn.zeroup.macrocosm.cv.HighWay;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WfEngine;
import io.vertx.tp.workflow.uca.component.Movement;
import io.vertx.tp.workflow.uca.component.Transfer;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class RunActor {

    @Me
    @Address(HighWay.Do.FLOW_START)
    public Future<JsonObject> start(final JsonObject data) {
        final WfEngine engine = WfEngine.connect(data.getJsonObject(KName.Flow.WORKFLOW));
        final Transfer transfer = engine.componentStart();
        final Movement runner = engine.componentRun();
        return Ux.future(data)
            // X_TODO, MODEL_PROCESSING
            .compose(transfer::startAsync)
            // Camunda Processing
            .compose(todo -> runner.moveAsync(data, todo));
    }
}
