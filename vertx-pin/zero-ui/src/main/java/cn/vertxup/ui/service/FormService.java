package cn.vertxup.ui.service;

import cn.vertxup.ui.domain.tables.daos.UiFormDao;
import cn.vertxup.ui.domain.tables.pojos.UiForm;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.ui.refine.Ui;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.Objects;

public class FormService implements FormStub {
    private static final Annal LOGGER = Annal.get(FormService.class);

    @Inject
    private transient FieldStub fieldStub;

    @Override
    public Future<JsonObject> fetchById(final String formId) {
        return Ux.Jooq.on(UiFormDao.class)
                .<UiForm>findByIdAsync(formId)
                .compose(form -> {
                    if (Objects.isNull(form)) {
                        Ui.infoWarn(FormService.LOGGER, " Form not found, id = {0}", formId);
                        return Ux.future(new JsonObject());
                    } else {
                        /*
                         * form / fields combine here
                         */
                        final JsonObject formJson = Ut.serializeJson(form);
                        return this.attachConfig(formJson);
                    }
                });
    }

    @Override
    public Future<JsonObject> fetchByCode(final String code, final String sigma) {
        final JsonObject filters = new JsonObject();
        filters.put(KeField.CODE, code);
        filters.put(KeField.SIGMA, sigma);
        filters.put("", Boolean.TRUE);
        return Ux.Jooq.on(UiFormDao.class)
                .<UiForm>fetchOneAsync(filters)
                .compose(form -> {
                    if (Objects.isNull(form)) {
                        Ui.infoWarn(FormService.LOGGER, " Form not found, code = {0}, sigma = {1}", code, sigma);
                        return Ux.future(new JsonObject());
                    } else {
                        /*
                         * form / fields combine here
                         */
                        final JsonObject formJson = Ut.serializeJson(form);
                        return this.attachConfig(formJson)
                                /*
                                 * Adapter for form configuration
                                 */
                                .compose(config -> Ux.future(config.getJsonObject("form")));
                    }
                });
    }

    private Future<JsonObject> attachConfig(final JsonObject formJson) {
        final JsonObject config = new JsonObject();
        Ke.mount(formJson, KeField.METADATA);
        /*
         * Form configuration
         * window and columns are required
         */
        final JsonObject form = new JsonObject();
        form.put(KeField.Ui.WINDOW, formJson.getValue(KeField.Ui.WINDOW));
        form.put(KeField.Ui.COLUMNS, formJson.getValue(KeField.Ui.COLUMNS));
        if (formJson.containsKey(KeField.Ui.CLASS_NAME)) {
            form.put(KeField.Ui.CLASS_NAME, formJson.getValue(KeField.Ui.CLASS_NAME));
        }
        /*
         * hidden: JsonArray
         */
        if (formJson.containsKey(KeField.Ui.HIDDEN)) {
            form.put(KeField.Ui.HIDDEN, formJson.getValue(KeField.Ui.HIDDEN));
            Ke.mountArray(form, KeField.Ui.HIDDEN);
        }
        /*
         * row: JsonObject
         */
        if (formJson.containsKey(KeField.Ui.ROW)) {
            form.put(KeField.Ui.ROW, formJson.getValue(KeField.Ui.HIDDEN));
            Ke.mount(form, KeField.Ui.ROW);
        }
        /*
         * metadata: JsonObject
         */
        if (formJson.containsKey(KeField.METADATA)) {
            final JsonObject metadata = formJson.getJsonObject(KeField.METADATA);
            if (metadata.containsKey(KeField.Ui.INITIAL)) {
                form.put(KeField.Ui.INITIAL, metadata.getJsonObject(KeField.Ui.INITIAL));
            }
        }
        final String formId = formJson.getString(KeField.KEY);
        return this.fieldStub.fetchUi(formId)
                /* Put `ui` to form configuration */
                .compose(ui -> Ux.future(config.put("form", form.put("ui", ui))));
    }
}
