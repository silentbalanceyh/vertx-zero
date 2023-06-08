package io.horizon.spi;

import cn.vertxup.ui.service.column.UiValve;
import io.horizon.spi.ui.Anchoret;
import io.horizon.spi.ui.Apeak;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ui.cv.UiMsg;
import io.vertx.up.commune.secure.Vis;

import static io.vertx.mod.ui.refine.Ui.LOG;

/*
 * Bridge design for call internal actual column service
 * 1. Dynamic Apeak
 * 2. Static Apeak
 */
public class ExColumnApeak extends Anchoret<Apeak> implements Apeak {

    @Override
    public Future<JsonArray> fetchFull(final JsonObject params) {
        LOG.Ui.info(this.getLogger(), UiMsg.COLUMN_FULL, params.encodePrettily());
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
