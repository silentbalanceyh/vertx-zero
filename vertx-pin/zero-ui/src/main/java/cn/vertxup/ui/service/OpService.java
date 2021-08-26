package cn.vertxup.ui.service;

import cn.vertxup.ui.domain.tables.daos.UiOpDao;
import cn.vertxup.ui.domain.tables.pojos.UiOp;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.ui.init.UiPin;
import io.vertx.tp.ui.refine.Ui;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class OpService implements OpStub {
    private static final Annal LOGGER = Annal.get(OpService.class);

    @Override
    public Future<JsonArray> fetchDynamic(final String control) {
        return Ux.Jooq.on(UiOpDao.class)
            .<UiOp>fetchAsync(KName.Ui.CONTROL_ID, control)
            .compose(Ux::futureA)
            .compose(array -> {
                Ut.itJArray(array).forEach(each ->
                    Ke.mount(each, KName.Ui.CONFIG));
                return Ux.future(array);
            });
    }

    @Override
    public Future<JsonArray> fetchFixed(final String identifier) {
        Ui.infoUi(LOGGER, "The fixed identifier = `{0}`", identifier);
        return Ux.future(UiPin.getOp());
    }
}
