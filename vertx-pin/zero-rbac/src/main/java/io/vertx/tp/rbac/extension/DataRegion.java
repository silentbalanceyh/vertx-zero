package io.vertx.tp.rbac.extension;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.commune.Envelop;
import io.vertx.up.extension.region.AbstractRegion;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/*
 * Extension in RBAC module
 * 1) Region calculation
 * 2) Visitant calculation ( Extension More )
 */
public class DataRegion extends AbstractRegion {

    @Override
    public Future<Envelop> before(final RoutingContext context, final Envelop envelop) {
        if (this.isEnabled(context)) {
            /* Get Critical parameters */
            return Sc.cacheBound(context, envelop).compose(matrix -> {
                if (Objects.nonNull(matrix)) {
                    Sc.infoAuth(this.getLogger(), context.request().path(),
                            AuthMsg.REGION_BEFORE, matrix.encode());
                    /*
                     * Body modification is only available for POST/PUT
                     * 1) Because only POST/PUT support body parameter
                     * 2) JqTool engine parameters belong to body key such as
                     * {
                     *     criteria: {},
                     *     sorter: [],
                     *     projection: [],
                     *     pager:{
                     *         page: xx,
                     *         size: xx
                     *     }
                     * }
                     * 3) Get method will ignore this kind of situation and move the logical to
                     * After workflow
                     */
                    final HttpMethod method = envelop.getMethod();
                    if (HttpMethod.POST == method || HttpMethod.PUT == method) {
                        /* Projection Modification */
                        final JsonArray projection = matrix.getJsonArray(Inquiry.KEY_PROJECTION);
                        if (Objects.nonNull(projection) && !projection.isEmpty()) {
                            envelop.onProjection(projection);
                        }
                        /* Criteria Modification */
                        final JsonObject criteria = matrix.getJsonObject(Inquiry.KEY_CRITERIA);
                        if (Objects.nonNull(criteria) && !criteria.isEmpty()) {
                            envelop.onCriteria(criteria);
                        }
                    }
                }
                return Ux.future(envelop);
            });
        } else {
            return Ux.future(envelop);
        }
    }

    @Override
    public Future<Envelop> after(final RoutingContext context, final Envelop response) {
        if (this.isEnabled(context)) {
            /* Get Critical parameters */
            return Sc.cacheBound(context, response).compose(matrix -> {
                Sc.infoAuth(this.getLogger(), AuthMsg.REGION_AFTER, matrix.encode());
                /* Projection */
                DataMin.dwarfRecord(response, matrix);
                /* Rows */
                DataMin.dwarfRows(response, matrix);
                /* Projection For Array */
                DataMin.dwarfCollection(response, matrix);
                return Ux.future(response);
            });
        } else {
            return Ux.future(response);
        }
    }
}
