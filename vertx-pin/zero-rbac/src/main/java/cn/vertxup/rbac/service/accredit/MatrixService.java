package cn.vertxup.rbac.service.accredit;

import cn.vertxup.rbac.domain.tables.pojos.SResource;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import cn.vertxup.rbac.service.view.ViewStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.logged.ScResource;
import io.vertx.up.commune.secure.DataBound;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MatrixService implements MatrixStub {

    @Inject
    private transient ViewStub stub;

    @Override
    public Future<DataBound> fetchBound(final String userId, final ScResource request) {
        /* User fetch first */
        return request.resource().compose(data -> {
            final SResource resource = Ux.fromJson(data.getJsonObject(KName.RECORD), SResource.class);
            final String profileKey = data.getString(KName.KEY);
            /* Fetch User First */
            return this.stub.fetchMatrix(userId, resource.getKey(), request.view())
                /* Whether userId exist */
                .compose(result -> Objects.isNull(result) ?
                    /*
                     * There is no matrix stored into database related to current user.
                     * Then find all role related matrices instead of current matrix.
                     * */
                    this.stub.fetchMatrix(new JsonArray(), resource.getKey(), request.view())
                    :
                    /*
                     * It means that there is defined user resource instead of role resource.
                     * In this situation, return to user's resource matrix directly.
                     */
                    this.toResult(result)
                )
                /* DataBound calculate */
                .compose(this::toBound)
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
        });
    }

    /*
     * Single View Process, Here we could attach visitant workflow
     */
    private Future<List<SView>> toResult(final SView entity) {
        final List<SView> matrixList = new ArrayList<>();
        matrixList.add(entity);
        return Future.succeededFuture(matrixList);
    }

    /*
     * Data Bound building
     * 1) projection append
     * 2) rows append
     * 3) criteria append
     * 4) attach `visitant` process here
     *
     */
    private Future<DataBound> toBound(final List<SView> matrices) {
        final DataBound bound = new DataBound();
        matrices.forEach(matrix -> {
            final JsonObject viewData = Ut.serializeJson(matrix);
            /*
             * Basic view configuration reading
             */
            bound.addProjection(matrix.getProjection())
                .addRows(matrix.getRows())
                .addCriteria(matrix.getCriteria())
                .addView(viewData);
        });
        return Future.succeededFuture(bound);
    }
}
