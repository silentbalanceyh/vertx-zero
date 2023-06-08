package cn.vertxup.integration.api;

import cn.vertxup.integration.service.DirStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.is.cv.Addr;
import io.vertx.mod.ke.error._400FileNameInValidException;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import jakarta.inject.Inject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class DirActor {

    @Inject
    private transient DirStub stub;

    @Me
    @Address(Addr.Directory.ADD)
    public Future<JsonObject> create(final JsonObject data) {
        // isFileName Checking
        final String name = data.getString(KName.NAME);
        if (!Ut.isFileName(name)) {
            return Fn.outWeb(_400FileNameInValidException.class, this.getClass());
        }
        return this.stub.create(data);
    }

    @Me
    @Address(Addr.Directory.UPDATE)
    public Future<JsonObject> update(final String key, final JsonObject data) {
        // isFileName Checking
        final String name = data.getString(KName.NAME);
        if (!Ut.isFileName(name)) {
            return Fn.outWeb(_400FileNameInValidException.class, this.getClass());
        }
        return this.stub.update(key, data);
    }

    /*
     * Hard Delete for directory.
     * 1. Delete `I_DIRECTORY` records
     * 2. Remove folder from `admin`
     */
    @Address(Addr.Directory.DELETE)
    public Future<Boolean> remove(final String key) {
        return this.stub.remove(key);
    }
}
