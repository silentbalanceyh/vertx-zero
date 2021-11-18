package cn.zeroup.macrocosm.service;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
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
            // Owner != userId ( Self Edit )
            if (!userId.equals(todo.getOwner())) {
                edition.put(KName.EDITION, Boolean.FALSE);
            }
        } else if (TodoStatus.PENDING == todoStatus) {
            // Owner == userId ( Self Disabled )
            if (userId.equals(todo.getToUser())) {
                edition.put(KName.EDITION, new JsonArray()
                    .add(KName.Flow.COMMENT_APPROVAL)
                    .add(KName.Flow.COMMENT_REJECT)
                );
            }
        }
        return Ux.futureJ(edition);
    }
}
