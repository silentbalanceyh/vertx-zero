package cn.vertxup.workflow.api;

import cn.vertxup.workflow.cv.HighWay;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.EngineOn;
import io.vertx.mod.workflow.atom.runtime.WRecord;
import io.vertx.mod.workflow.atom.runtime.WRequest;
import io.vertx.mod.workflow.uca.coadjutor.Stay;
import io.vertx.mod.workflow.uca.component.Movement;
import io.vertx.mod.workflow.uca.component.Transfer;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.unity.Ux;

import static io.vertx.mod.workflow.refine.Wf.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class RunActor {
    /*
    DRAFT Generation Data Request:
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
        final WRequest request = new WRequest(data);
        final EngineOn engine = EngineOn.connect(request);

        // Camunda Processing
        final Movement movement = engine.componentRun();
        // Transfer Processing
        final Transfer transfer = engine.componentStart();

        LOG.Web.info(this.getClass(), "Movement = {0}, Transfer = {1}",
            movement.getClass(), transfer.getClass());

        return movement.moveAsync(request)
            .compose(instance -> transfer.moveAsync(request, instance))
            .compose(WRecord::futureJ);
    }


    /*
    DRAFT Saving Data Request:
     {
        "openBy": "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
        "openByNo": "EMP00001",
        "openByMobile": "15922611447",
        "toUserTeam": "deeaf16d-8903-4b22-9877-01498a81d0e6",
        "toUser": "c43efe17-2431-40ad-8ed7-7a412208003f",
        "openByDept": "88775480-bf0b-462e-9266-063f2e9afbd1",
        "toUserNo": "EM000004",
        "toUserMobile": "15922611442",
        "toUserDept": "4ee61296-bf84-499e-89df-a4d40e3f80bc",
        "openByName": "虞浪",
        "record": {
            "size": 1114042,
            "fileKey": "JdScpmRDhzYk1jlpH2vXC7dBPWxBpuCpCiC44XJz0Z4VHx0ixWLISqkktKqy1e7R",
            "name": "error.jpeg",
            "type": "image/jpeg",
            "extension": "jpeg",
            "key": "deb2cfc2-2a53-4a4f-84e5-cb479e5f5d13"
        },
        "toUserName": "审批者",
        "owner": "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
        "serial": "WFR22020200100015-01",
        "toUserEmail": "approver@126.com",
        "title": "AAA",
        "catalog": "w.document.request",
        "type": "workflow.doc",
        "phase": "DRAFT",
        "openByEmail": "silentbalanceyh@126.com",
        "description": "<p>AAA</p>",
        "openAt": "2022-02-02T08:37:06.000Z",
        "openByTeam": "013e543f-0569-476a-bfb1-85fecfe4b5eb",
        "key": "33cb12d9-a51c-4c64-adc1-cad829b9788c",
        "language": "cn",
        "active": true,
        "sigma": "Qxw5HDkluJFnAPmcQCtu9uhGdXEiGNtP",
        "workflow": {
            "definitionKey": "process.file.management",
            "definitionId": "process.file.management:1:549abe4c-8229-11ec-9943-c2ddbf8634fa",
            "instanceId": "5af9ef57-8403-11ec-a2c8-acde48001122",
            "taskId": "5b0009df-8403-11ec-a2c8-acde48001122"
        },
        "updatedBy": "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
        "updatedAt": "2022-02-02T08:53:24.616055Z"
    }
     */

    @Me
    @Address(HighWay.Do.FLOW_COMPLETE)
    public Future<JsonObject> complete(final JsonObject data) {
        final WRequest request = new WRequest(data);
        final EngineOn engine = EngineOn.connect(request);
        final Transfer transfer = engine.componentGenerate();
        final Movement movement = engine.componentRun();
        return movement.moveAsync(request)
            .compose(instance -> transfer.moveAsync(request, instance))
            // Callback
            .compose(WRecord::futureJ);
    }

    @Me
    @Address(HighWay.Do.FLOW_BATCH)
    public Future<JsonObject> batch(final JsonObject data) {
        return Ux.futureJ();
    }

    @Me
    @Address(HighWay.Do.FLOW_CANCEL)
    public Future<JsonObject> cancel(final JsonObject data) {
        final WRequest request = new WRequest(data);
        final EngineOn engine = EngineOn.connect(request);
        // ProcessDefinition
        final Stay stay = engine.stayCancel();
        LOG.Web.info(this.getClass(), "( Cancel ) Stay = {0}", stay.getClass());
        final Movement movement = engine.stayMovement();
        return movement.moveAsync(request)
            .compose(instance -> stay.keepAsync(request, instance))
            // Callback
            // Fix issue:
            // No serializer found for class io.vertx.mod.runtime.atom.workflow.WRecord
            // and no properties discovered to create BeanSerializer
            .compose(WRecord::futureJ);
    }

    @Me
    @Address(HighWay.Do.FLOW_CLOSE)
    public Future<JsonObject> close(final JsonObject data) {
        final WRequest request = new WRequest(data);
        final EngineOn engine = EngineOn.connect(request);
        // ProcessDefinition
        final Stay stay = engine.stayClose();
        LOG.Web.info(this.getClass(), "( Close ) Stay = {0}", stay.getClass());
        final Movement movement = engine.stayMovement();
        return movement.moveAsync(request)
            .compose(instance -> stay.keepAsync(request, instance))
            // Callback
            // Fix issue:
            // No serializer found for class io.vertx.mod.runtime.atom.workflow.WRecord
            // and no properties discovered to create BeanSerializer
            .compose(WRecord::futureJ);
    }

    @Me
    @Address(HighWay.Do.FLOW_DRAFT)
    public Future<JsonObject> draft(final JsonObject data) {
        final WRequest request = new WRequest(data);
        final EngineOn engine = EngineOn.connect(request);

        // Camunda Processing
        final Stay stay = engine.stayDraft();
        LOG.Web.info(this.getClass(), "Stay = {0}", stay.getClass());
        final Movement movement = engine.stayMovement();
        return movement.moveAsync(request)
            .compose(instance -> stay.keepAsync(request, instance))
            // Callback
            .compose(wData -> wData.futureJ(true));
    }
}
