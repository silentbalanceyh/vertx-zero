package io.vertx.tp.optic;

import cn.vertxup.ui.service.column.UiValve;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.fantom.Anchoret;
import io.vertx.tp.ui.cv.UiMsg;
import io.vertx.tp.ui.refine.Ui;

/*
 * Bridge design for call internal actual column service
 * 1. Dynamic Apeak
 * 2. Static Apeak
 */
public class ExColumnApeak extends Anchoret<Apeak> implements Apeak {

    @Override
    public Future<JsonArray> fetchFull(final JsonObject config) {
        Ui.infoUi(this.getLogger(), UiMsg.COLUMN_FULL, config.encodePrettily());
        final Boolean dynamic = config.getBoolean(Apeak.ARG0);
        /* Ui valve initialization */
        final UiValve valve;
        if (dynamic) {
            valve = UiValve.dynamic();
        } else {
            valve = UiValve.fixed();
        }
        /* Whether this module used dynamic column here */
        final String identifier = config.getString(Apeak.ARG1);
        final String sigma = config.getString(Apeak.ARG2);
        final String view = config.getString(Apeak.ARG3);
        return valve.fetchColumn(view, identifier, sigma);
    }
}
