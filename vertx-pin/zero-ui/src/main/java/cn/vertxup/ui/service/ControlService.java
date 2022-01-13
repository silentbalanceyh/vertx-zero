package cn.vertxup.ui.service;

import cn.vertxup.ui.domain.tables.daos.UiControlDao;
import cn.vertxup.ui.domain.tables.daos.UiVisitorDao;
import cn.vertxup.ui.domain.tables.pojos.UiControl;
import cn.vertxup.ui.domain.tables.pojos.UiVisitor;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ui.cv.em.ControlType;
import io.vertx.tp.ui.refine.Ui;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

public class ControlService implements ControlStub {

    private static final Annal LOGGER = Annal.get(ControlService.class);

    @Override
    public Future<JsonArray> fetchControls(final String pageId) {
        return Ux.Jooq.on(UiControlDao.class)
            .<UiControl>fetchAsync("pageId", pageId)
            .compose(Ux::futureA)
            .compose(list -> {
                /*
                 * Convert JsonArray field of Control
                 */
                final JsonArray result = new JsonArray();
                list.stream().filter(Objects::nonNull)
                    .map(item -> (JsonObject) item)
                    .map(item -> Ut.ifJObject(item,
                        KName.Ui.CONTAINER_CONFIG,
                        KName.Ui.COMPONENT_CONFIG,
                        KName.Ui.ASSIST,
                        KName.Ui.GRID
                    ))
                    .forEach(result::add);
                return Ux.future(result);
            });
    }

    @Override
    public Future<JsonObject> fetchById(final String control) {
        return Ux.Jooq.on(UiControlDao.class)
            .<UiControl>fetchByIdAsync(control)
            .compose(Ux::futureJ)
            .compose(Ut.ifJObject(
                KName.Ui.CONTAINER_CONFIG,
                KName.Ui.COMPONENT_CONFIG,
                KName.Ui.ASSIST,
                KName.Ui.GRID
            ));
    }

    @Override
    public Future<JsonObject> fetchControl(final ControlType controlType, final JsonObject params) {
        final JsonObject criteria = Ux.whereAnd();
        criteria.put(KName.TYPE, controlType.name());
        criteria.mergeIn(params);
        Ui.infoUi(LOGGER, "Control ( type = {0} ) with parameters = `{1}`", controlType, criteria.encode());
        return Ux.Jooq.on(UiVisitorDao.class)
            .<UiVisitor>fetchOneAsync(criteria)
            .compose(Ux::futureJ);
    }
}
