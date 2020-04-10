package cn.vertxup.rbac.service.accredit;

import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.up.commune.config.DataBound;

import java.util.ArrayList;
import java.util.List;

class MatrixFlow {

    static Future<List<SView>> toResult(final SView entity) {
        final List<SView> matrixList = new ArrayList<>();
        matrixList.add(entity);
        return Future.succeededFuture(matrixList);
    }

    static Future<DataBound> toBound(final List<SView> matrices) {
        final DataBound bound = new DataBound();
        matrices.forEach(matrix -> bound.addProjection(matrix.getProjection())
                .addRows(matrix.getRows())
                .addCriteria(matrix.getCriteria())
        );
        return Future.succeededFuture(bound);
    }
}
