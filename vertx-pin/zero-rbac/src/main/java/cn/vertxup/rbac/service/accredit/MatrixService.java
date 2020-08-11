package cn.vertxup.rbac.service.accredit;

import cn.vertxup.rbac.domain.tables.pojos.SResource;
import cn.vertxup.rbac.service.view.ViewStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScRequest;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.commune.secure.DataBound;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.Objects;

public class MatrixService implements MatrixStub {

    @Inject
    private transient ViewStub stub;

    @Override
    public Future<DataBound> fetchBound(final ScRequest request,
                                        final SResource resource) {
        /* User fetch first */
        final String userId = request.getUser();
        final String resourceId = resource.getKey();
        final String profileKey = Sc.generateProfileKey(resource);
        /* Fetch User First */
        return this.stub.fetchMatrix(userId, resourceId, request.getView())
                /* Whether userId exist */
                .compose(result -> Objects.isNull(result) ?
                        /*
                         * There is no matrix stored into database related to current user.
                         * Then find all role related matrices instead of current matrix.
                         * */
                        request.openSession()
                                /* Extract Roles from Privilege */
                                .compose(privilege -> privilege.fetchRoles(profileKey))
                                .compose(roles -> this.stub.fetchMatrix(roles, resourceId, request.getView()))
                        :
                        /*
                         * It means that there is defined user resource instead of role resource.
                         * In this situation, return to user's resource matrix directly.
                         */
                        MatrixFlow.toResult(result)
                )
                /* DataBound calculate */
                .compose(MatrixFlow::toBound)
                /* DataBound for calculation of resource here */
                .compose(bound -> {
                    final Boolean virtual = resource.getVirtual();
                    /*
                     * Check whether current resource is virtual resource
                     * 1) If true, the resource is virtual resource, there need additional steps
                     * to calculated view in future instead of current view stored.
                     * 2) If false, the old workflow
                     */
                    if (Objects.nonNull(virtual) && virtual) {
                        final JsonObject seeker = new JsonObject();
                        seeker.put("config", Ut.toJObject(resource.getSeekConfig()));
                        seeker.put("syntax", Ut.toJObject(resource.getSeekSyntax()));
                        seeker.put("component", resource.getSeekComponent());
                        /*
                         * Store view object into json for future condition building
                         */
                        bound.addSeeker(seeker);
                    }
                    return Ux.future(bound);
                });
    }
}
