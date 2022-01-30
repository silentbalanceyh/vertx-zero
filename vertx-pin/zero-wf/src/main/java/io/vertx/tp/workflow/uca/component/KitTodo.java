package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.daos.WTicketDao;
import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.atom.WInstance;
import io.vertx.tp.workflow.atom.WMoveRule;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class KitTodo {
    /*
     * Close current
     * {
     *      "status": "FINISHED",
     *      "finishedAt": "",
     *      "finishedBy": "",
     *      "traceEnd": "Depend on instance",
     *      "active": true
     * }
     */
    static JsonObject inputClose(final JsonObject params, final WInstance wInstance) {
        final JsonObject updatedData = params.copy();
        updatedData.put(KName.STATUS, TodoStatus.FINISHED);
        final String user = params.getString(KName.UPDATED_BY);
        updatedData.put(KName.Flow.Auditor.FINISHED_AT, Instant.now());
        updatedData.put(KName.Flow.Auditor.FINISHED_BY, user);
        // updatedAt / updatedBy contain values
        updatedData.put(KName.ACTIVE, Boolean.TRUE);

        if (wInstance.isEnd()) {
            updatedData.put(KName.Flow.TRACE_END, Boolean.TRUE);
        }
        // Todo based on previous
        final WMoveRule rule = wInstance.rule();
        if (Objects.nonNull(rule) && Ut.notNil(rule.getTodo())) {
            updatedData.mergeIn(rule.getTodo());
        }
        return updatedData;
    }

    /*
     * Modification
     * {
     *      "traceEnd": true
     *      "traceExtra":{
     *          "history": "ADD"
     *      },
     *      "status": "CANCELED",
     *      "finishedAt": "",
     *      "finishedBy": ""
     * }
     */
    static JsonObject inputCancel(final JsonObject params, final WInstance wInstance, final Set<String> historySet) {
        final JsonObject todoData = params.copy();
        /*
         * History building
         */
        final Set<String> traceSet = new HashSet<>(historySet);
        final Task task = wInstance.task();
        if (Objects.nonNull(task)) {
            traceSet.add(task.getTaskDefinitionKey());
        }
        /*
         * Todo Processing（Record Keep）
         */
        {
            final JsonObject history = new JsonObject();
            history.put(KName.HISTORY, Ut.toJArray(traceSet));
            todoData.put(KName.Flow.TRACE_EXTRA, history.encode());

            final String user = todoData.getString(KName.UPDATED_BY);
            todoData.put(KName.STATUS, TodoStatus.CANCELED.name());
            todoData.put(KName.Flow.Auditor.FINISHED_AT, Instant.now());
            todoData.put(KName.Flow.Auditor.FINISHED_BY, user);
            todoData.put(KName.Flow.TRACE_END, Boolean.TRUE);
        }
        return todoData;
    }

    /*
     * Next
     * {
     *      "key": "New generation",
     *      "serial": "Re-calculation",
     *      "code": "synced with serial",
     *      "traceOrder": "Add 1 on original",
     *
     *      "traceId": "Trace Id",
     *      "traceTaskId": "Trace Task Id ( Next )",
     *      "status":  "PENDING",
     *
     *      "finishedBy": null,
     *      "finishedAt": null,
     *      "createdBy": "Original created by",
     *      "createdAt": Now,
     *      "updatedBy": Current User
     *      "updatedAt": Now
     * }
     * Extra combine
     * WMove todo part such as
     * {
     *      "status": "DRAFT",
     *      "toUser": null
     * }
     */
    static WTodo inputNext(final WTodo todo, final WInstance wInstance) {
        // Todo New
        final JsonObject newJson = Ux.toJson(todo);
        WTodo entity = Ux.fromJson(newJson, WTodo.class);
        {
            // key remove
            entity.setKey(UUID.randomUUID().toString());
            // major ticket number
            final String serialPrev = todo.getSerial();
            final String serial = serialPrev.substring(0, serialPrev.lastIndexOf('-'));
            entity.setTraceOrder(todo.getTraceOrder() + 1);
            entity.setSerial(serial + "-" + entity.getTraceOrder());
            entity.setCode(entity.getSerial());
            // Clear comments
            entity.setCommentApproval(null);
            entity.setCommentReject(null);
        }
        {
            final Task nextTask = wInstance.task();
            entity.setTraceId(nextTask.getProcessInstanceId());
            // entity.setTraceTaskId(nextTask.getId());
            entity.setStatus(TodoStatus.PENDING.name());           // Force Pending
        }
        {
            // Auditor Processing
            entity.setFinishedAt(null);
            entity.setFinishedBy(null);

            entity.setCreatedAt(LocalDateTime.now());
            entity.setCreatedBy(todo.getCreatedBy());

            entity.setUpdatedAt(LocalDateTime.now());
            entity.setUpdatedBy(todo.getUpdatedBy());
        }
        final WMoveRule rule = wInstance.rule();
        if (Objects.nonNull(rule)) {
            final JsonObject todoUpdate = rule.getTodo();
            entity = Ux.updateT(entity, todoUpdate);
        }
        return entity;
    }

    Future<WTodo> updateAsync(final JsonObject params) {
        // Todo Updating
        final UxJooq jooq = Ux.Jooq.on(WTodoDao.class);
        final String key = params.getString(KName.KEY);
        return jooq.<WTodo>fetchByIdAsync(key).compose(query -> {
            final WTodo updated = Ux.updateT(query, params);
            return jooq.updateAsync(updated);
        });
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
    Future<WTodo> insertAsync(final JsonObject params, final ConfigTodo config,
                              final ProcessInstance instance) {
        // Todo Build
        return config.generate(params.getString(KName.KEY)).compose(normalized -> {
            // Ticket Workflow
            normalized.remove(KName.KEY);
            final WTicket ticket = Ux.fromJson(normalized, WTicket.class);
            /*
             * null value when ticket processed
             *
             *  - code: came from serial
             * 「Camunda」
             *  - flowDefinitionKey: came from json
             *  - flowDefinitionId: came from json
             *  - flowInstanceId: came from process
             *  - flowEnd: false when insert todo
             *
             * 「Flow」
             *  - cancelBy
             *  - cancelAt
             *  - closeBy
             *  - closeAt
             *  - closeSolution
             *  - closeCode
             *
             * 「Future」
             *  - metadata
             *  - modelCategory
             *  - category
             *  - categorySub
             */
            ticket.setFlowEnd(Boolean.FALSE);
            ticket.setFlowInstanceId(instance.getId());
            return Ux.Jooq.on(WTicketDao.class).insertAsync(ticket).compose(inserted -> {
                // Todo Workflow
                final WTodo todo = Ux.fromJson(normalized, WTodo.class);
                /*
                 * null value when processed
                 * 「Related」
                 *  - traceId
                 *  - traceOrder
                 *  - parentId
                 *
                 * 「Camunda」
                 *  - taskId
                 *  - taskKey
                 *
                 * 「Flow」
                 *  - assignedBy
                 *  - assignedAt
                 *  - acceptedBy
                 *  - acceptedAt
                 *  - finishedBy
                 *  - finishedAt
                 *  - comment
                 *  - commentApproval
                 *  - commentReject
                 *
                 * 「Future」
                 *  - metadata
                 *  - modelCategory
                 *  - activityId
                 */
                todo.setTraceId(inserted.getKey());
                todo.setTraceOrder(1);
                todo.setCode(inserted.getCode() + "-" + Ut.fromAdjust(todo.getTraceOrder(), 2));
                todo.setSerial(inserted.getSerial() + "-" + Ut.fromAdjust(todo.getTraceOrder(), 2));
                return Ux.Jooq.on(WTodoDao.class).insertAsync(todo);
            });
        });
    }

    /*
     *  Connect WTodo and ProcessInstance
     *  1. instance = true
     *  2. traceId = instanceId
     *  3. traceTaskId = taskId
     */
    private Future<WTodo> traceAsync(final WTodo todo, final ProcessInstance instance) {
        final EventOn event = EventOn.get();
        return event.taskActive(instance).compose(task -> {
            // todo.setInstance(Boolean.TRUE);                   // Camunda Engine
            todo.setTraceId(instance.getId());                // Trace ID Related
            // todo.setTraceTaskId(task.getId());                // Trace Task ID
            return Ux.future(todo);
        });
    }
}
