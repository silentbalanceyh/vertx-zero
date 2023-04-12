package io.vertx.tp.workflow.uca.conformity;

import cn.vertxup.workflow.cv.em.TodoStatus;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.Objects;

/*
 * 自动状态机，用于控制状态
 * 1）主单 Ticket 状态
 * 2）任务单 Task 状态
 */
final class Vm {
    /*
     * Generate for status parsing
     */
    static void generate(final WTodo generated, final WTicket ticket, final JsonObject parameters) {
        final JsonObject moveData = Ut.valueJObject(parameters, KName.__.MOVE);
        final String status = Ut.valueString(moveData, KName.STATUS);

        /*
         * 状态计算，若没有目标状态则直接设置 PENDING
         * 若存在目标状态（可能配置到Move中）则不设置目标状态
         */
        if (Ut.notNil(status)) {
            generated.setStatus(status);
            // REJECTED / REDO 忘记设置 toUser 时执行
            final TodoStatus todoStatus = Ut.toEnum(TodoStatus.class, generated.getStatus());
            if (Objects.isNull(generated.getToUser())) {
                if (TodoStatus.REJECTED == todoStatus) {
                    // 拒绝到开单人手中
                    generated.setToUser(ticket.getOpenBy());
                    generated.setAcceptedBy(ticket.getOpenBy());
                    generated.setAcceptedAt(LocalDateTime.now());
                }
                // 驳回走额外流程
            }
        } else {
            /*
             * 状态计算，若没有目标状态则直接设置 PENDING
             * 若存在目标状态（可能配置到Move中）则不设置目标状态
             */
            generated.setStatus(TodoStatus.PENDING.name());
        }
    }
}
