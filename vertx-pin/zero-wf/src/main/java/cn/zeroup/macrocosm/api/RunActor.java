package cn.zeroup.macrocosm.api;

import cn.zeroup.macrocosm.cv.HighWay;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.EngineOn;
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
    /*
    {
      "roleName" : "开发人员",
      "typeName" : "文件管理服务",
      "toUser" : "63b5383e-5a2e-44ec-87d4-add096aac548",
      "toGroupMode" : "ROLE",
      "name" : "TEST",
      "record" : {
        "size" : 4262533,
        "name" : "4k0001.jpg",
        "instance" : {
          "uid" : "rc-upload-1636893185841-2",
          "name" : "4k0001.jpg",
          "key" : "b840cd3a-8510-420d-b5c1-7f030111ef21",
          "type" : "image/jpeg",
          "size" : 4262533,
          "sizeUi" : "4.07MB",
          "extension" : "jpg"
        },
        "sizeUi" : "4.07MB",
        "type" : "image/jpeg",
        "category" : "FILE.REQUEST",
        "extension" : "jpg",
        "key" : "b840cd3a-8510-420d-b5c1-7f030111ef21"
      },
      "toRole" : "1f27530f-38db-4662-81d4-46ea15b04205",
      "status" : "DRAFT",
      "userName" : "开发者",
      "type" : "cat.doc.file",
      "description" : "Description",
      "language" : "cn",
      "active" : true,
      "sigma" : "Qxw5HDkluJFnAPmcQCtu9uhGdXEiGNtP",
      "draft" : true,
      "workflow" : {
        "definitionKey" : "process.file.management",
        "definitionId" : "process.file.management:1:6",
        "instanceId": "instance id",
        "language" : "cn"
      },
      "createdBy" : "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
      "createdAt" : "2021-11-14T12:33:51.019091Z",
      "updatedBy" : "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
      "updatedAt" : "2021-11-14T12:33:51.019091Z"
    }
     */
    @Me
    @Address(HighWay.Do.FLOW_START)
    public Future<JsonObject> start(final JsonObject data) {
        final EngineOn engine = EngineOn.connect(data.getJsonObject(KName.Flow.WORKFLOW));
        final Transfer transfer = engine.componentStart();
        final Movement runner = engine.componentRun();
        // Camunda Processing
        return runner.moveAsync(data).compose(instance -> transfer.moveAsync(data, instance)
            // Callback
            .compose(Ux::futureJ)
        );
    }

    @Me
    @Address(HighWay.Do.FLOW_COMPLETE)
    public Future<JsonObject> complete(final JsonObject data) {
        final EngineOn engine = EngineOn.connect(data.getJsonObject(KName.Flow.WORKFLOW));
        final Transfer transfer = engine.componentGenerate();
        final Movement runner = engine.componentRun();
        return runner.moveAsync(data).compose(instance -> transfer.moveAsync(data, instance)
            // Callback
            .compose(nil -> Ux.futureJ())
        );
    }

    @Me
    @Address(HighWay.Do.FLOW_DRAFT)
    public Future<JsonObject> draft(final JsonObject data) {
        final JsonObject workflow = data.getJsonObject(KName.Flow.WORKFLOW);
        final EngineOn engine = EngineOn.connect(workflow);
        // ProcessDefinition
        final Transfer transfer = engine.componentDraft();
        return transfer.moveAsync(data, null)
            // Callback
            .compose(Ux::futureJ);
    }

    @Me
    @Address(HighWay.Do.FLOW_BATCH)
    public Future<JsonObject> batch(final JsonObject body) {

        return Ux.futureJ();
    }
}
