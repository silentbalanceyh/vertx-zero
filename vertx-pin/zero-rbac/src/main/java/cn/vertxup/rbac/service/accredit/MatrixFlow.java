package cn.vertxup.rbac.service.accredit;

import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.secure.DataBound;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;

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
