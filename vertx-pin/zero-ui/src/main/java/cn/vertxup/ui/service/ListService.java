package cn.vertxup.ui.service;

import cn.vertxup.ui.domain.tables.daos.UiListDao;
import cn.vertxup.ui.domain.tables.daos.UiOpDao;
import cn.vertxup.ui.domain.tables.pojos.UiList;
import cn.vertxup.ui.domain.tables.pojos.UiOp;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.ui.init.UiPin;
import io.vertx.tp.ui.refine.Ui;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.Objects;

public class ListService implements ListStub {
    private static final Annal LOGGER = Annal.get(ListService.class);
    @Inject
    private transient OptionStub optionStub;

    @Override
    public Future<JsonObject> fetchById(final String listId) {
        /*
         * Read list configuration for configuration
         */
        return Ux.Jooq.on(UiListDao.class).<UiList>fetchByIdAsync(listId)
            .compose(list -> {
                if (Objects.isNull(list)) {
                    Ui.infoWarn(ListService.LOGGER, " Form not found, id = {0}", listId);
                    return Ux.future(new JsonObject());
                } else {
                    /*
                     * It means here are some additional configuration that should be
                     * fetch then
                     */
                    final JsonObject listJson = Ut.serializeJson(list);
                    return this.attachConfig(listJson);
                }
            });
    }

    @Override
    public Future<JsonArray> fetchByIdentifier(final String identifier, final String sigma) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.IDENTIFIER, identifier);
        condition.put(KName.SIGMA, sigma);
        return Ux.Jooq.on(UiListDao.class).<UiList>fetchAndAsync(condition)
            /* List<UiList> */
            .compose(Ux::futureA);
    }

    private Future<JsonObject> attachConfig(final JsonObject listJson) {
        /*
         * Capture important configuration here
         */
        Ut.ifJObject(listJson,
            ListStub.FIELD_OPTIONS,
            ListStub.FIELD_OPTIONS_AJAX,
            ListStub.FIELD_OPTIONS_SUBMIT,
            ListStub.FIELD_V_SEGMENT
        );
        return Ux.future(listJson)
            /* vQuery */
            .compose(Ux.attach(ListStub.FIELD_V_QUERY, this.optionStub::fetchQuery))
            /* vSearch */
            .compose(Ux.attach(ListStub.FIELD_V_SEARCH, this.optionStub::fetchSearch))
            /* vTable */
            .compose(Ux.attach(ListStub.FIELD_V_TABLE, this.optionStub::fetchTable))
            /* vSegment */
            .compose(Ux.attachJ(ListStub.FIELD_V_SEGMENT, this.optionStub::fetchFragment))
            /* Combiner for final processing */
            .compose(Ke.fabricAsync("classCombiner"));
    }

    @Override
    public Future<JsonArray> fetchOpDynamic(final String control) {
        return Ux.Jooq.on(UiOpDao.class)
            .<UiOp>fetchAsync(KName.Ui.CONTROL_ID, control)
            .compose(Ux::futureA)
            .compose(Ut.ifJArray(KName.Ui.CONFIG));
    }

    @Override
    public Future<JsonArray> fetchOpFixed(final String identifier) {
        Ui.infoUi(LOGGER, "The fixed identifier = `{0}`", identifier);
        return Ux.future(UiPin.getOp());
    }
}
