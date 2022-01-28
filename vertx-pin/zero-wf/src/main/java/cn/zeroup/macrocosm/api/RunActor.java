package cn.zeroup.macrocosm.api;

import cn.zeroup.macrocosm.cv.HighWay;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.EngineOn;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.component.Movement;
import io.vertx.tp.workflow.uca.component.Stay;
import io.vertx.tp.workflow.uca.component.Transfer;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class RunActor {
    /*
    {
        "openBy": "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
        "toUser": "a0b1c6bc-4162-47e2-8f16-c9f4dd162739",
        "record": {
            "size": 1114042,
            "name": "error.jpeg",
            "sizeUi": "1.06MB",
            "type": "image/jpeg",
            "file": [
                {
                    "uid": "rc-upload-1643355423248-2",
                    "name": "error.jpeg",
                    "key": "ceafc8ec-0137-46df-a60f-38ae475b0242",
                    "type": "image/jpeg",
                    "size": 1114042,
                    "sizeUi": "1.06MB",
                    "extension": "jpeg"
                }
            ],
            "category": "FILE.REQUEST",
            "extension": "jpeg",
            "key": "ceafc8ec-0137-46df-a60f-38ae475b0242"
        },
        "toUserName": "开发者",
        "status": "DRAFT",
        "owner": "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
        "title": "TEST",
        "catalog": "w.document.request",
        "type": "workflow.doc",
        "description": "<p>TEST</p>",
        "openAt": "2022-01-28T07:37:06.141Z",
        "ownerName": "虞浪",
        "language": "cn",
        "active": true,
        "sigma": "Qxw5HDkluJFnAPmcQCtu9uhGdXEiGNtP",
        "workflow": {
            "definitionKey": "process.file.management",
            "definitionId": "process.file.management:1:c80c1ad1-7fd9-11ec-b990-f60fb9ea15d8"
        },
        "draft": true
    }
     */
    @Me
    @Address(HighWay.Do.FLOW_START)
    public Future<JsonObject> start(final JsonObject data) {
        final EngineOn engine = EngineOn.connect(data);


        // Camunda Processing
        final Movement runner = engine.componentRun();
        // Transfer Processing
        final Transfer transfer = engine.componentStart();
        Wf.Log.infoWeb(this.getClass(), "Movement = {0}, Transfer = {1}", runner.getClass(), transfer.getClass());


        return runner.moveAsync(data).compose(instance -> transfer.moveAsync(data, instance).compose(Ux::futureJ));
    }

    @Me
    @Address(HighWay.Do.FLOW_COMPLETE)
    public Future<JsonObject> complete(final JsonObject data) {
        final EngineOn engine = EngineOn.connect(data);
        final Transfer transfer = engine.componentGenerate();
        final Movement runner = engine.componentRun();
        return runner.moveAsync(data)
            .compose(instance -> transfer.moveAsync(data, instance)
                // Callback
                .compose(Ux::futureJ)
            );
    }

    @Me
    @Address(HighWay.Do.FLOW_DRAFT)
    public Future<JsonObject> draft(final JsonObject data) {
        final EngineOn engine = EngineOn.connect(data);
        // ProcessDefinition
        final Stay stay = engine.stayDraft();
        return stay.keepAsync(data, null)
            // Callback
            .compose(Ux::futureJ);
    }

    @Me
    @Address(HighWay.Do.FLOW_BATCH)
    public Future<JsonObject> batch(final JsonObject data) {
        return Ux.futureJ();
    }

    @Me
    @Address(HighWay.Do.FLOW_CANCEL)
    public Future<JsonObject> cancel(final JsonObject data) {
        final EngineOn engine = EngineOn.connect(data);
        // ProcessDefinition
        final Stay stay = engine.stayCancel();
        final Movement runner = engine.environmentPre();
        return runner.moveAsync(data)
            .compose(instance -> stay.keepAsync(data, instance))
            // Callback
            .compose(Ux::futureJ);
    }
}
