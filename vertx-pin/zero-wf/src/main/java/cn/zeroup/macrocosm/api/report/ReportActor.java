package cn.zeroup.macrocosm.api.report;

import cn.vertxup.ambient.domain.tables.daos.XActivityDao;
import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import cn.zeroup.macrocosm.cv.HighWay;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import cn.zeroup.macrocosm.service.ReportStub;
import cn.zeroup.macrocosm.service.TaskStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.tp.workflow.atom.EngineOn;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.component.Movement;
import io.vertx.tp.workflow.uca.component.Stay;
import io.vertx.tp.workflow.uca.component.Transfer;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class ReportActor {
    @Inject
    private transient ReportStub reportStub;
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
    @Address(HighWay.Report.TICKET_LIST)
    public Future<JsonObject> list(final JsonObject qr) {
        Wf.Log.initQueue(this.getClass(), "Qr Queue Input: {0}", qr.encode());
        // Status Must be in following
        // -- PENDING
        // -- DRAFT
        // -- ACCEPTED
        final JsonObject qrStatus = Ux.whereAnd();
        qrStatus.put(KName.STATUS + ",i", new JsonArray()
                .add(TodoStatus.PENDING.name())
                .add(TodoStatus.ACCEPTED.name())    // Accepted, Accepted for long term ticket
                .add(TodoStatus.DRAFT.name())       // Draft,  Edit the draft for redo submitting
        );
        final JsonObject qrCombine = Ux.whereQrA(qr, "$Q$", qrStatus);
        Wf.Log.initQueue(this.getClass(), "Qr Queue Combined: {0}", qrCombine.encode());
        return this.reportStub.fetchQueue(qrCombine); // this.condStub.qrQueue(qr, userId)
    }

    @Address(HighWay.Report.TICKET_ACTIVITY)
    public Future<JsonObject> fetchActivity(final String key, final User user) {
//        return Ux.Jooq.on(XActivityDao.class)
//                .<XActivity>fetchAndAsync(new JsonObject().put("modelKey",key))
//                .compose(activity -> {
//                    return this.reportStub.fetchUserByActivity(key);
//                })
//                ;
        return this.reportStub.fetchActivity(key, user);
    }

    @Address(HighWay.Report.ASSETS_LIST)
    public Future<JsonObject> fetchAssets(final User user) {
        final String userId = Ux.keyUser(user);
        final JsonObject qrStatus = Ux.whereAnd();
        qrStatus.put("createdBy", userId);
        final JsonObject condition = new JsonObject();
        condition.put(Qr.KEY_CRITERIA,new JsonObject().put("sigma","Qxw5HDkluJFnAPmcQCtu9uhGdXEiGNtP"));
        final JsonObject qrCombine = Ux.whereQrA(condition, "$Q$", qrStatus);
//        Wf.Log.initQueue(this.getClass(), "Qr Queue Combined: {0}", qrCombine.encode());
        return this.reportStub.fetchAssets(qrCombine);
    }
}
