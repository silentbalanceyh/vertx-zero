package io.horizon.spi;

import cn.vertxup.rbac.service.accredit.ActionService;
import cn.vertxup.rbac.service.accredit.ActionStub;
import io.horizon.spi.ui.Anchoret;
import io.horizon.spi.web.Seeker;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.AuthMsg;
import io.vertx.mod.rbac.error._404ActionMissingException;
import io.vertx.up.atom.typed.UObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.Objects;

import static io.vertx.mod.rbac.refine.Sc.LOG;

/*
 * Seek impact resource for params here, it will be passed by crud
 */
public class ExIntimitySeeker extends Anchoret<Seeker> implements Seeker {

    private transient final ActionStub stub = Ut.singleton(ActionService.class);

    @Override
    public Future<JsonObject> fetchImpact(final JsonObject params) {
        /*
         * Uri, Method
         */
        final String uri = params.getString(ARG0);
        final HttpMethod method = HttpMethod.valueOf(params.getString(ARG1));
        final String sigma = params.getString(ARG2);
        LOG.Resource.info(this.getLogger(), AuthMsg.SEEKER_RESOURCE, uri, method, sigma);
        return this.stub.fetchAction(uri, method, sigma).compose(action -> Objects.isNull(action) ?
            Future.failedFuture(new _404ActionMissingException(this.getClass(), method + " " + uri)) :
            UObject.create(params).append(KName.RESOURCE_ID, action.getResourceId())
                .toFuture());
    }
}
