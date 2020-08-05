package cn.vertxup.ui.service;

import cn.vertxup.ui.domain.tables.daos.UiControlDao;
import cn.vertxup.ui.domain.tables.pojos.UiControl;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.unity.Ux;

import java.util.Objects;

public class ControlService implements ControlStub {

    @Override
    public Future<JsonArray> fetchControls(final String pageId) {
        return Ux.Jooq.on(UiControlDao.class)
                .<UiControl>fetchAsync("pageId", pageId)
                .compose(Ux::fnJArray)
                .compose(list -> {
                    /*
                     * Convert JsonArray field of Control
                     */
                    final JsonArray result = new JsonArray();
                    list.stream().filter(Objects::nonNull)
                            .map(item -> (JsonObject) item)
                            .map(item -> Ke.mount(item, KeField.Ui.CONTAINER_CONFIG))
                            .map(item -> Ke.mount(item, KeField.Ui.COMPONENT_CONFIG))
                            .map(item -> Ke.mount(item, KeField.Ui.ASSIST))
                            .map(item -> Ke.mount(item, KeField.Ui.GRID))
                            .forEach(result::add);
                    return Ux.future(result);
                });
    }

    @Override
    public Future<JsonObject> fetchById(final String control) {
        return Ux.Jooq.on(UiControlDao.class)
                .<UiControl>findByIdAsync(control)
                .compose(Ux::fnJObject)
                .compose(Ke.mount(KeField.Ui.CONTAINER_CONFIG))
                .compose(Ke.mount(KeField.Ui.COMPONENT_CONFIG))
                .compose(Ke.mount(KeField.Ui.ASSIST))
                .compose(Ke.mount(KeField.Ui.GRID));
    }
}
