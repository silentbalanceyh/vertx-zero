package io.vertx.tp.optic;

import cn.vertxup.ui.service.column.UiValve;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.ui.Anchoret;
import io.vertx.tp.optic.ui.Apeak;
import io.vertx.tp.ui.cv.UiMsg;
import io.vertx.tp.ui.refine.Ui;
import io.vertx.up.atom.secure.Vis;

/*
 * Bridge design for call internal actual column service
 * 1. Dynamic Apeak
 * 2. Static Apeak
 */
public class ExColumnApeak extends Anchoret<Apeak> implements Apeak {

    @Override
    public Future<JsonArray> fetchFull(final JsonObject params) {
        Ui.infoUi(this.getLogger(), UiMsg.COLUMN_FULL, params.encodePrettily());
        final Boolean dynamic = params.getBoolean(Apeak.ARG0);
        /* Ui valve initialization */
        final UiValve valve;
        if (dynamic) {
            valve = UiValve.dynamic();
        } else {
            valve = UiValve.fixed();
        }
        /* Whether this module used dynamic column here */
        final String identifier = params.getString(Apeak.ARG1);
        final String sigma = params.getString(Apeak.ARG2);
        final Vis view = Vis.smart(params.getValue(Apeak.ARG3));
        return valve.fetchColumn(view, identifier, sigma);
    }
}
