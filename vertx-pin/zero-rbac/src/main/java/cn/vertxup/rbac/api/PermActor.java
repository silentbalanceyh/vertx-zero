package cn.vertxup.rbac.api;

import cn.vertxup.rbac.service.business.PermStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.config.XHeader;

import javax.inject.Inject;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@Queue
public class PermActor {

    @Inject
    private transient PermStub stub;

    /*
     * Calculate Permission Groups
     */
    @Address(Addr.Authority.PERMISSION_GROUP)
    public Future<JsonArray> calculate(final XHeader header) {
        return this.stub.groupAsync(header.getSigma());
    }

    @Address(Addr.Authority.PERMISSION_SAVING)
    public Future<JsonObject> savingPerm(final JsonObject processed,
                                         final XHeader header) {
        return this.stub.savingPerm(processed, header.getSigma());
    }
}
