package cn.vertxup.rbac.service.accredit;

import cn.vertxup.rbac.domain.tables.pojos.SResource;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import cn.vertxup.rbac.service.view.ViewStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScConfig;
import io.vertx.tp.rbac.init.ScPin;
import io.vertx.tp.rbac.logged.ScResource;
import io.vertx.tp.rbac.logged.ScUser;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.commune.secure.DataBound;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MatrixService implements MatrixStub {

    private final static ScConfig CONFIG = ScPin.getConfig();

    @Inject
    private transient ViewStub stub;

    @Override
    public Future<DataBound> fetchBound(final ScUser user, final ScResource request) {
        /* User fetch first */
        return Rapid.<String, JsonObject>t(CONFIG.getResourcePool()).read(request.key()).compose(data -> {
            final SResource resource = Ux.fromJson(data.getJsonObject(KName.RECORD), SResource.class);
            /* Fetch User First */
            return this.fetchViews(user, resource, request.view())
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

    private Future<List<SView>> fetchViews(final ScUser user, final SResource resource, final Vis view) {
        final String userId = user.user();
        return this.stub.fetchMatrix(userId, resource.getKey(), view).compose(viewData -> {
            if (Objects.isNull(viewData)) {
                /*
                 * No Personal View
                 * There is no matrix stored into database related to current user.
                 * Then find all role related matrices instead of current matrix.
                 */
                final String profileName = Sc.valueProfile(resource);
                return user.roles(profileName)
                    /*
                     * Fetch Role View
                     * It means that there is defined user resource instead of role resource.
                     * In this situation, return to user's resource matrix directly.
                     */
                    .compose(roles -> this.stub.fetchMatrix(roles, resource.getKey(), view));
            } else {
                return this.toResult(viewData);
            }
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
