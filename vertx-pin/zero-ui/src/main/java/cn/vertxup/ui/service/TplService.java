package cn.vertxup.ui.service;

import cn.vertxup.ui.domain.tables.daos.UiLayoutDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

public class TplService implements TplStub {

    private static final String LAYOUT_POOL = "LAYOUT_POOL";

    @Override
    public Future<JsonObject> fetchLayout(final String layoutId) {
        /*
         * Enable Cache for Layout
         */
        return Ke.poolAsync(LAYOUT_POOL, layoutId, () -> Ux.Jooq.on(UiLayoutDao.class)
            .fetchByIdAsync(layoutId)
            .compose(Ux::futureJ)
            /*
             * Configuration converted to Json
             */
            .compose(Ut.ifJObject(KName.Ui.CONFIG)));
    }
}
