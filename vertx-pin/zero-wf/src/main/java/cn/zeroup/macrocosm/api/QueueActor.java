package cn.zeroup.macrocosm.api;

import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.zeroup.macrocosm.cv.HighWay;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class QueueActor {
    @Address(HighWay.Queue.BY_CREATED)
    public Future<JsonObject> fetchMyCreated(final JsonObject qr, final User user) {
        // Extract user id
        final String userId = Ux.keyUser(user);
        // Combine qr based on
        final JsonObject qrCombine = Ux.whereQrA(qr, KName.CREATED_BY, userId);
        // Todo Processing
        return Ux.Jooq.on(WTodoDao.class).searchAsync(qrCombine);
    }
}
