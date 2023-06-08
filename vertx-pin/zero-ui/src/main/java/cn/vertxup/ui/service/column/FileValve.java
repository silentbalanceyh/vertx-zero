package cn.vertxup.ui.service.column;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.mod.ui.cv.UiMsg;
import io.vertx.mod.ui.init.UiPin;
import io.vertx.up.commune.secure.Vis;
import io.vertx.up.unity.Ux;

import static io.vertx.mod.ui.refine.Ui.LOG;

class FileValve implements UiValve {

    @Override
    public Future<JsonArray> fetchColumn(final Vis view, final String identifier, final String sigma) {
        /* Default column JsonArray */
        final JsonArray columns = UiPin.getColumn(identifier);
        LOG.Ui.info(this.getClass(), UiMsg.COLUMN_STATIC, sigma, columns.size(), columns.encode());
        return Ux.future(columns);
    }
}
