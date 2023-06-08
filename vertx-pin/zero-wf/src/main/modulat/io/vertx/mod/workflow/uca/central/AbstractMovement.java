package io.vertx.mod.workflow.uca.central;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.mod.workflow.atom.runtime.WRecord;
import io.vertx.mod.workflow.atom.runtime.WTransition;
import io.vertx.mod.workflow.uca.toolkit.ULinkage;
import io.vertx.mod.workflow.uca.toolkit.UTicket;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractMovement extends AbstractTransfer {

    private transient UTicket todoKit;
    private transient ULinkage linkageKit;

    /*
     * Overwrite the `Todo` Here
     */
    @Override
    public Behaviour bind(final MetaInstance metadata) {
        Objects.requireNonNull(metadata);
        this.todoKit = new UTicket(metadata);
        this.linkageKit = new ULinkage(metadata);
        return super.bind(metadata);
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
    protected Future<WRecord> insertAsync(final JsonObject params, final WTransition wTransition) {
        Objects.requireNonNull(this.todoKit);
        Objects.requireNonNull(this.linkageKit);
        // Todo Insert
        return this.todoKit.insertAsync(params, wTransition)
            // Linkage Sync
            .compose(record -> this.linkageKit.syncAsync(params, record));
    }

    protected Future<WRecord> updateAsync(final JsonObject params, final WTransition wTransition) {
        Objects.requireNonNull(this.todoKit);
        Objects.requireNonNull(this.linkageKit);
        // Todo Update
        return this.todoKit.updateAsync(params, wTransition)
            // Linkage Sync
            .compose(record -> this.linkageKit.syncAsync(params, record));
    }

    protected Future<WRecord> saveAsync(final JsonObject params, final WTransition wTransition) {
        Objects.requireNonNull(this.todoKit);
        Objects.requireNonNull(this.linkageKit);
        // Todo Saving ( Insert / Update )
        return this.todoKit.saveAsync(params, wTransition)
            // Linkage Sync
            .compose(record -> this.linkageKit.syncAsync(params, record));
    }
}
