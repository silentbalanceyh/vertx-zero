package cn.zeroup.macrocosm.service;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AclService implements AclStub {
    @Override
    public Future<JsonObject> authorize(final WRecord record, final String userId) {
        if (Objects.isNull(userId)) {
            /*
             * `userId` is null, it means that the user haven't logged into
             * the system to read the data, following data will be set:
             *
             * -- edition = false
             */
            return Ux.future(new JsonObject().put(KName.EDITION, Boolean.FALSE));
        } else {
            /* Sample Rule Here ( Here are no definition ) */
            final WTodo todo = record.todo();
            final TodoStatus todoStatus = Ut.toEnum(TodoStatus.class, todo.getStatus());
            final JsonObject edition = new JsonObject();


            /*
             * Rule 1:
             * 1. - openBy `userId`
             * 2. - status = `DRAFT` ( Draft Version )
             */
            final WTicket ticket = record.ticket();
            if (userId.equals(ticket.getOpenBy())) {
                // Draft Edit
                edition.put(KName.EDITION, TodoStatus.DRAFT == todoStatus);
            } else {
                // OpenBy != toUser
                if (userId.equals(todo.getToUser()) && TodoStatus.PENDING == todoStatus) {
                    // OpenBy == toUser ( Part Edition )
                    final JsonObject fields = new JsonObject();
                    fields.put(KName.Flow.COMMENT_APPROVAL, Boolean.TRUE);
                    fields.put(KName.Flow.COMMENT_REJECT, Boolean.TRUE);
                    edition.put(KName.EDITION, fields);
                } else {
                    // View Only OpenBy != toUser ( Disabled )
                    edition.put(KName.EDITION, Boolean.FALSE);
                }
            }
            return Ux.futureJ(edition);
        }
    }
}
