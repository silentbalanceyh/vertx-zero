package cn.vertxup.workflow.service;

import cn.vertxup.workflow.cv.em.TodoStatus;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.runtime.WRecord;
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
            final WTodo todo = record.task();
            final TodoStatus todoStatus = Ut.toEnum(todo.getStatus(), TodoStatus.class);
            final JsonObject edition = new JsonObject();

            if (TodoStatus.DRAFT == todoStatus ||
                TodoStatus.REJECTED == todoStatus ||
                TodoStatus.REDO == todoStatus) {
                /*
                 * Draft Edit
                 * Any one can edit the ticket information here
                 * It means that the edition is true when status is `draft`
                 *
                 * 1) Edit my ticket
                 * 2) Edit other's ticket
                 */
                edition.put(KName.EDITION, Boolean.TRUE);
            } else {
                /*
                 * Non Draft Edit
                 * You can edit the information of any
                 *
                 * 1) The submitter edit the information
                 * 2) The approval user edit the information
                 */
                // OpenBy != toUser
                if (userId.equals(todo.getAcceptedBy())) {
                    // OpenBy == acceptedBy ( Part Edition )
                    edition.put(KName.EDITION, Boolean.TRUE);
                } else {
                    // View Only OpenBy != acceptedBy ( Disabled )
                    // Could not do any information of `confirmed/confirmedDesc`
                    final JsonArray fields = new JsonArray();
                    fields.add("confirmedOk");
                    fields.add("confirmedDesc");
                    edition.put(KName.READ_ONLY, fields);
                }
            }
            return Ux.futureJ(edition);
        }
    }
}
