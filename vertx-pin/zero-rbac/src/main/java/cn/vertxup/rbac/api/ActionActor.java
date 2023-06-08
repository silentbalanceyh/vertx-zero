package cn.vertxup.rbac.api;

import cn.vertxup.rbac.service.accredit.ActionStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.unity.Ux;
import jakarta.inject.Inject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class ActionActor {

    @Inject
    private transient ActionStub actionStub;

    @Address(Addr.Authority.ACTION_SEEK)
    public Future<JsonArray> searchAuthorized(final String sigma, final JsonObject params) {
        final String keyword = params.getString("keyword");
        return this.actionStub.searchAuthorized(keyword, sigma).compose(Ux::futureA);
    }

    @Address(Addr.Authority.ACTION_READY)
    public Future<JsonArray> searchAll(final String sigma, final JsonObject params) {
        final String keyword = params.getString("keyword");
        return this.actionStub.searchAll(keyword, sigma).compose(Ux::futureA);
    }
}
