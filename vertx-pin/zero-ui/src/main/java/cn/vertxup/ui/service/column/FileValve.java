package cn.vertxup.ui.service.column;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.ui.cv.UiMsg;
import io.vertx.tp.ui.init.UiPin;
import io.vertx.tp.ui.refine.Ui;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

class FileValve implements UiValve {

    private static final Annal LOGGER = Annal.get(FileValve.class);

    @Override
    public Future<JsonArray> fetchColumn(final Vis view, final String identifier, final String sigma) {
        /* Default column JsonArray */
        final JsonArray columns = UiPin.getColumn(identifier);
        Ui.infoUi(LOGGER, UiMsg.COLUMN_STATIC, sigma, columns.size(), columns.encode());
        return Ux.future(columns);
    }
}
