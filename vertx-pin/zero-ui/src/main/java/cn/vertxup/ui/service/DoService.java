package cn.vertxup.ui.service;

import cn.vertxup.ui.domain.tables.daos.UiOpDao;
import cn.vertxup.ui.domain.tables.pojos.UiOp;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ui.init.UiPin;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import static io.vertx.mod.ui.refine.Ui.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DoService implements DoStub {
    private static final Annal LOGGER = Annal.get(DoService.class);

    // type = null, 旧的兼容流程
    @Override
    public Future<JsonArray> fetchSmart(final JsonObject params) {
        final String control = Ut.valueString(params, KName.Ui.CONTROL);
        if (Ut.isNil(control)) {
            return this.fetchWeb(params);
        } else {
            return this.fetchAtom(params);
        }
    }

    @Override
    public Future<JsonArray> fetchAtom(final JsonObject params) {
        final String control = Ut.valueString(params, KName.Ui.CONTROL);
        return Ux.Jooq.on(UiOpDao.class)
            .<UiOp>fetchAsync(KName.Ui.CONTROL_ID, control)
            .compose(Ux::futureA)
            .compose(Fn.ofJArray(KName.Ui.CONFIG));
    }

    @Override
    public Future<JsonArray> fetchWeb(final JsonObject params) {
        final String identifier = Ut.valueString(params, KName.IDENTIFIER);
        LOG.Ui.info(LOGGER, "The fixed identifier = `{0}`", identifier);
        return Ux.future(UiPin.getOp());
    }

    @Override
    public Future<JsonArray> fetchFlow(final JsonObject params) {
        final String workflow = Ut.valueString(params, KName.Flow.WORKFLOW);
        final String task = Ut.valueString(params, KName.Flow.NODE);
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.Ui.CONTROL_ID, workflow);
        condition.put(KName.EVENT, task);
        LOG.Ui.info(LOGGER, "The workflow condition = `{0}`", condition.encode());
        return Ux.Jooq.on(UiOpDao.class)
            .<UiOp>fetchAsync(condition)
            .compose(Ux::futureA)
            .compose(Fn.ofJArray(KName.Ui.CONFIG));
    }
}
