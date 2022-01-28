package cn.zeroup.macrocosm.service;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AclService implements AclStub {
    @Override
    public Future<JsonObject> authorize(final WProcess process, final WTodo todo, final String userId) {
        /*
         * Sample Rule Here
         */
        final TodoStatus todoStatus = Ut.toEnum(TodoStatus.class, todo.getStatus());
        final JsonObject edition = new JsonObject();
        if (TodoStatus.DRAFT == todoStatus) {
            // Owner != userId ( Non Self Disabled )
            edition.put(KName.EDITION, Boolean.FALSE);

        } else if (TodoStatus.PENDING == todoStatus) {
            if (userId.equals(todo.getToUser())) {
                // Owner == toUser ( Part Edition )
                final JsonObject fields = new JsonObject();
                fields.put(KName.Flow.COMMENT_APPROVAL, Boolean.TRUE);
                fields.put(KName.Flow.COMMENT_REJECT, Boolean.TRUE);
                edition.put(KName.EDITION, fields);
            } else {
                // Owner != toUser ( Disabled )
                edition.put(KName.EDITION, Boolean.FALSE);
            }
        }
        return Ux.futureJ(edition);
    }
}
