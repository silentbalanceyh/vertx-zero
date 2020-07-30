package cn.vertxup.rbac.service.accredit;

import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.commune.config.DataBound;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class MatrixFlow {
    /*
     * Single View Process, Here we could attach visitant workflow
     */
    static Future<List<SView>> toResult(final SView entity) {
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
    static Future<DataBound> toBound(final List<SView> matrices) {
        final DataBound bound = new DataBound();
        matrices.forEach(matrix -> {
            /*
             * Basic view configuration reading
             */
            bound.addProjection(matrix.getProjection())
                    .addRows(matrix.getRows())
                    .addCriteria(matrix.getCriteria());
            /*
             * Build another configuration when visitant = true
             * Don't forget to store `visitantSyntax` to JsonObject
             * for future usage when DataRegion enabled.
             */
            final Boolean visitant = matrix.getVisitant();
            if (Objects.nonNull(visitant) && visitant) {
                final JsonObject viewJson = Ut.serializeJson(matrix);
                Ke.mount(viewJson, "visitantSyntax");
                bound.addView(viewJson);
            }
        });
        return Future.succeededFuture(bound);
    }
}
