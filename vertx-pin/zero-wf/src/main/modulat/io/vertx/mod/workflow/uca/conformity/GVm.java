package io.vertx.mod.workflow.uca.conformity;

import cn.vertxup.workflow.cv.em.TodoStatus;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.runtime.WTransition;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.Objects;

/*
 * 自动状态机，用于控制状态
 * 1）主单 Ticket 状态
 * 2）任务单 Task 状态
 */
public final class GVm {
    /*
     * Generate for status parsing
     */
    public static void generate(final WTodo generated, final WTodo wTask,
                                final WTicket ticket) {
        final TodoStatus todoStatus = Ut.toEnum(wTask.getStatus(), TodoStatus.class);
        /*
         * 状态计算，若没有目标状态则直接设置 PENDING
         * 若存在目标状态（可能配置到Move中）则不设置目标状态
         */
        if (TodoStatus.FINISHED == todoStatus) {
            /*
             * 状态计算，若没有目标状态则直接设置 PENDING
             * 若存在目标状态（可能配置到Move中）则不设置目标状态
             */
            generated.setStatus(TodoStatus.PENDING.name());
        } else if (TodoStatus.REJECTED == todoStatus) {
            // 拒绝
            generated.setStatus(TodoStatus.DRAFT.name());
            if (Objects.isNull(generated.getToUser())) {
                // 拒绝到开单人手中
                generated.setToUser(ticket.getOpenBy());
                generated.setAcceptedBy(ticket.getOpenBy());
                generated.setAcceptedAt(LocalDateTime.now());
            }
        } else if (TodoStatus.REDO == todoStatus) {
            // 驳回
            generated.setStatus(TodoStatus.PENDING.name());
            // 临时版本
            if (Objects.isNull(generated.getToUser())) {
                // 拒绝到开单人手中
                generated.setToUser(ticket.getOpenBy());
                generated.setAcceptedBy(ticket.getOpenBy());
                generated.setAcceptedAt(LocalDateTime.now());
            }
        }
    }

    public static void finish(final JsonObject params, final WTransition wTransition) {
        final String status = Ut.valueString(params, KName.STATUS);
        /*
         * 状态改动成 FINISHED 的基础条件设置
         */
        if (Ut.isNil(status) || TodoStatus.PENDING.name().equals(status)) {
            params.put(KName.STATUS, TodoStatus.FINISHED.name());
        }
    }

    public interface Status {
        // Basic
        JsonArray QUEUE = new JsonArray()
            .add(TodoStatus.PENDING.name())
            .add(TodoStatus.ACCEPTED.name())    // Accepted, Accepted for long term ticket
            .add(TodoStatus.DRAFT.name());       // Draft,  Edit the draft for redo submitting

        JsonArray FAILURE = new JsonArray()
            .add(TodoStatus.REJECTED.name())
            .add(TodoStatus.REDO.name());
    }
}
