package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.atom.WInstance;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.up.eon.KName;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractTodo extends AbstractTransfer {

    private transient final KitTodo todoKit;

    public AbstractTodo() {
        super();
        this.todoKit = new KitTodo();
    }

    /*
     params:

     normalized:
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
                    "uid": "rc-upload-1643358391244-2",
                    "name": "error.jpeg",
                    "key": "f7f77109-fa6d-4fc2-a959-fa7b5877ef31",
                    "type": "image/jpeg",
                    "size": 1114042,
                    "sizeUi": "1.06MB",
                    "extension": "jpeg"
                }
            ],
            "category": "FILE.REQUEST",
            "extension": "jpeg",
            "key": "f7f77109-fa6d-4fc2-a959-fa7b5877ef31"
        },
        "toUserName": "开发者",
        "status": "DRAFT",
        "owner": "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
        "title": "TEST",
        "catalog": "w.document.request",
        "type": "workflow.doc",
        "description": "<p>TEST</p>",
        "openAt": "2022-01-28T08:26:46.246Z",
        "ownerName": "虞浪",
        "language": "cn",
        "active": true,
        "sigma": "Qxw5HDkluJFnAPmcQCtu9uhGdXEiGNtP",
        "workflow": {
            "definitionKey": "process.file.management",
            "definitionId": "process.file.management:1:c80c1ad1-7fd9-11ec-b990-f60fb9ea15d8"
        },
        "draft": true,
        "createdBy": "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
        "createdAt": "2022-01-28T08:27:00.281624Z",
        "updatedBy": "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
        "updatedAt": "2022-01-28T08:27:00.281624Z",
        "todo": {
            "name": "`文件申请：${serial}, ${title}`",
            "icon": "file",
            "todoUrl": "`/ambient/flow-view?tid=${key}&id=${modelKey}`",
            "modelComponent": "cn.vertxup.ambient.domain.tables.daos.XAttachmentDao",
            "modelId": "x.attachment",
            "indent": "W.File.Request"
        },
        "indent": "W.File.Request",
        "serial": "WFR22012800100007",
        "key": "283c98a1-4184-4cd1-8a5d-52194f7b38a4",
        "modelKey": "f7f77109-fa6d-4fc2-a959-fa7b5877ef31",
        "name": "文件申请：WFR22012800100007, TEST",
        "icon": "file",
        "todoUrl": "/ambient/flow-view?tid=283c98a1-4184-4cd1-8a5d-52194f7b38a4&id=f7f77109-fa6d-4fc2-a959-fa7b5877ef31",
        "modelComponent": "cn.vertxup.ambient.domain.tables.daos.XAttachmentDao",
        "modelId": "x.attachment"
    }
     */
    protected Future<WRecord> insertAsync(final JsonObject params, final ConfigTodo config, final ProcessInstance instance) {
        return this.todoKit.insertAsync(params, config, instance);
    }

    protected Future<WRecord> updateAsync(final JsonObject params) {
        return this.todoKit.updateAsync(params);
    }

    protected Future<WRecord> generateAsync(final WRecord record, final WInstance instance) {
        // final WTodo generated = KitTodo.inputNext(todo, instance);
        final WRecord generated = KitTodo.nextJ(record, instance);
        // return this.todoKit.generateAsync(todo, instance);
        return this.todoKit.generateAsync(generated);
    }

    // ------------------- Configuration Building with Json Parameter --------------------
    protected ConfigTodo configuration(final JsonObject params) {
        final JsonObject request = params.copy();
        request.put(KName.Flow.TODO, this.config.getJsonObject(KName.Flow.TODO, new JsonObject()));
        return new ConfigTodo(request);
    }
}
