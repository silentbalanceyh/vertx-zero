package cn.vertxup.ambient.api.file;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class FileActor {


    @Address(Addr.File.MY_QUEUE)
    public Future<JsonObject> searchMy(final JsonObject query,
                                       final User user) {

        return null;
    }

    @Address(Addr.File.BY_KEY)
    public Future<JsonObject> fileByKey(final String key) {

        return null;
    }
}
