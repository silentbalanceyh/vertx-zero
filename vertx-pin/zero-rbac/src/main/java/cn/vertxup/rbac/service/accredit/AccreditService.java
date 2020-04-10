package cn.vertxup.rbac.service.accredit;

import cn.vertxup.rbac.domain.tables.pojos.SAction;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScRequest;
import io.vertx.up.atom.Refer;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;
import java.util.function.Supplier;

public class AccreditService implements AccreditStub {

    @Inject
    private transient ActionStub actionStub;

    @Inject
    private transient MatrixStub matrixStub;

    @Override
    public Future<Boolean> authorize(final JsonObject data) {
        final ScRequest request = new ScRequest(data);
        /* Refer for action / resource */
        final Refer actionHod = new Refer();
        final Refer resourceHod = new Refer();
        return this.authorizedWithCache(request, () -> this.actionStub.fetchAction(request.getNormalizedUri(), request.getMethod(), request.getSigma())

                /* SAction checking for ( Uri + Method ) */
                .compose(action -> AccreditFlow.inspectAction(this.getClass(), action, request))
                .compose(actionHod::future)
                .compose(action -> this.actionStub.fetchResource(action.getResourceId()))

                /* SResource checking for ( ResourceId */
                .compose(resource -> AccreditFlow.inspectResource(this.getClass(), resource, request, actionHod.get()))

                /* Action Level Comparing */
                .compose(resource -> AccreditFlow.inspectLevel(this.getClass(), resource, actionHod.get()))
                .compose(resourceHod::future)

                /* Find Profile Permission and Check Profile */
                .compose(resource -> AccreditFlow.inspectPermission(this.getClass(), resource, request))

                /* Permission / Action Comparing */
                .compose(permissions -> AccreditFlow.inspectAuthorized(this.getClass(), actionHod.get(), permissions))

                /* The Final steps to execute matrix data here. */
                .compose(result -> this.authorized(result, request, resourceHod.get(), actionHod.get())));
    }

    private Future<Boolean> authorizedWithCache(final ScRequest request, final Supplier<Future<Boolean>> supplier) {
        final String authorizedKey = request.getAuthorizedKey();
        return request.openSession()
                /* Get data from cache */
                .compose(privilege -> privilege.fetchAuthorized(authorizedKey))
                /* */
                .compose(result -> result ? Ux.future(Boolean.TRUE) :
                        supplier.get());
    }

    private Future<Boolean> authorized(final Boolean result, final ScRequest request,
                                       final SResource resource, final SAction action) {
        if (result) {
            return this.matrixStub.fetchBound(request, resource)
                    /* DataBound credit parsing from SAction */
                    .compose(bound -> Ux.future(bound.addCredit(action.getRenewalCredit())))
                    /* DataBound stored */
                    .compose(bound -> AccreditFlow.inspectBound(bound, request))
                    /* Authorized cached and get result */
                    .compose(nil -> AccreditFlow.inspectAuthorized(request));
        } else {
            return Future.succeededFuture(Boolean.FALSE);
        }
    }
}
