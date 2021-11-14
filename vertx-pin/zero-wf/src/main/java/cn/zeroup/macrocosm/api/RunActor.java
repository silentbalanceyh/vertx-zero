package cn.zeroup.macrocosm.api;

import cn.zeroup.macrocosm.cv.HighWay;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WEngine;
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
        final WEngine engine = WEngine.connect(data.getJsonObject(KName.Flow.WORKFLOW));
        final Transfer transfer = engine.componentStart();
        final Movement runner = engine.componentRun();
        // Camunda Processing
        return runner.moveAsync(data).compose(instance -> transfer.moveAsync(data, instance)
            // Callback
            .compose(nil -> Ux.futureJ())
        );
    }

    @Me
    @Address(HighWay.Do.FLOW_COMPLETE)
    public Future<JsonObject> complete(final JsonObject data) {
        final WEngine engine = WEngine.connect(data.getJsonObject(KName.Flow.WORKFLOW));
        final Transfer transfer = engine.componentGenerate();
        final Movement runner = engine.componentRun();
        return runner.moveAsync(data).compose(instance -> transfer.moveAsync(data, instance)
            // Callback
            .compose(nil -> Ux.futureJ())
        );
    }
}
