package io.vertx.tp.rbac.region;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class CommonCosmo implements Cosmo {
    @Override
    public Future<Envelop> before(final Envelop request, final JsonObject matrix) {
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
        final HttpMethod method = request.getMethod();
        if (HttpMethod.POST == method || HttpMethod.PUT == method) {
            /* Projection Modification */
            final JsonArray projection = matrix.getJsonArray(Inquiry.KEY_PROJECTION);
            if (Objects.nonNull(projection) && !projection.isEmpty()) {
                request.onProjection(projection);
            }
            /* Criteria Modification */
            final JsonObject criteria = matrix.getJsonObject(Inquiry.KEY_CRITERIA);
            if (Objects.nonNull(criteria) && !criteria.isEmpty()) {
                request.onCriteria(criteria);
            }
        }
        return Ux.future(request);
    }

    @Override
    public Future<Envelop> after(final Envelop response, final JsonObject matrix) {
        /* Projection */
        DataOut.dwarfRecord(response, matrix);
        /* Rows */
        DataOut.dwarfRows(response, matrix);
        /* Projection For Array */
        DataOut.dwarfCollection(response, matrix);
        return Ux.future(response);
    }
}
