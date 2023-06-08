package cn.vertxup.ui.service;

import cn.vertxup.ui.domain.tables.daos.UiFormDao;
import cn.vertxup.ui.domain.tables.pojos.UiForm;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import jakarta.inject.Inject;

import java.util.Objects;

import static io.vertx.mod.ui.refine.Ui.LOG;

public class FormService implements FormStub {
    private static final Annal LOGGER = Annal.get(FormService.class);

    @Inject
    private transient FieldStub fieldStub;

    @Override
    public Future<JsonObject> fetchById(final String formId) {
        return Ux.Jooq.on(UiFormDao.class).<UiForm>fetchByIdAsync(formId)
            .compose(form -> {
                if (Objects.isNull(form)) {
                    LOG.Ui.warn(LOGGER, " Form not found, id = {0}", formId);
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
    public Future<JsonArray> fetchByIdentifier(final String identifier, final String sigma) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.IDENTIFIER, identifier);
        condition.put(KName.SIGMA, sigma);
        return Ux.Jooq.on(UiFormDao.class).<UiForm>fetchAndAsync(condition)
            /* List<UiForm> */
            .compose(Ux::futureA)
            .compose(Fn.ofJArray(
                KName.Ui.HIDDEN,
                KName.METADATA
            ));
        //            .compose(forms -> {
        //                Ut.itJArray(forms).forEach(form -> {
        //                    Ke.mountArray(form, KName.Ui.HIDDEN);
        //                    Ke.mount(form, KName.METADATA);
        //                });
        //                return Ux.future(forms);
        //            });
    }

    @Override
    public Future<JsonObject> fetchByCode(final String code, final String sigma) {
        final JsonObject filters = new JsonObject();
        filters.put(KName.CODE, code);
        filters.put(KName.SIGMA, sigma);
        filters.put("", Boolean.TRUE);
        return Ux.Jooq.on(UiFormDao.class)
            .<UiForm>fetchOneAsync(filters)
            .compose(form -> {
                if (Objects.isNull(form)) {
                    LOG.Ui.warn(LOGGER, " Form not found, code = {0}, sigma = {1}", code, sigma);
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

    @Override
    public Future<JsonObject> update(final String key, final JsonObject data) {
        // 1. mountIn form, convert those into object from string
        final JsonObject form = Ut.valueToString(data,
            KName.Ui.HIDDEN,
            KName.Ui.ROW,
            KName.METADATA
        );
        final UiForm uiForm = Ux.fromJson(form, UiForm.class);
        // 2. save ui-form record
        return Ux.Jooq.on(UiFormDao.class)
            .updateAsync(key, uiForm)
            .compose(Ux::futureJ)
            // 3. mountOut
            .compose(Fn.ofJObject(
                KName.Ui.HIDDEN,
                KName.Ui.ROW,
                KName.METADATA
            ));
    }

    @Override
    public Future<Boolean> delete(final String key) {
        return Ux.Jooq.on(UiFormDao.class)
            .deleteByIdAsync(key);
    }

    private Future<JsonObject> attachConfig(final JsonObject formJson) {
        final JsonObject config = new JsonObject();
        Ut.valueToJObject(formJson, KName.METADATA);
        /*
         * Form configuration
         * window and columns are required
         */
        final JsonObject form = new JsonObject();
        form.put(KName.Ui.WINDOW, formJson.getValue(KName.Ui.WINDOW));
        form.put(KName.Ui.COLUMNS, formJson.getValue(KName.Ui.COLUMNS));
        if (formJson.containsKey(KName.Ui.CLASS_NAME)) {
            form.put(KName.Ui.CLASS_NAME, formJson.getValue(KName.Ui.CLASS_NAME));
        }
        /*
         * hidden: JsonArray
         */
        if (formJson.containsKey(KName.Ui.HIDDEN)) {
            form.put(KName.Ui.HIDDEN, formJson.getValue(KName.Ui.HIDDEN));
            Ut.valueToJObject(form, KName.Ui.HIDDEN);
        }
        /*
         * row: JsonObject
         */
        if (formJson.containsKey(KName.Ui.ROW)) {
            form.put(KName.Ui.ROW, formJson.getValue(KName.Ui.ROW));
            Ut.valueToJObject(form, KName.Ui.ROW);
        }
        /*
         * metadata: JsonObject
         */
        if (formJson.containsKey(KName.METADATA)) {
            final JsonObject metadata = formJson.getJsonObject(KName.METADATA);
            if (metadata.containsKey(KName.Ui.INITIAL)) {
                form.put(KName.Ui.INITIAL, metadata.getJsonObject(KName.Ui.INITIAL));
            }
        }
        final String formId = formJson.getString(KName.KEY);
        return this.fieldStub.fetchUi(formId)
            /* Put `ui` to form configuration */
            .compose(ui -> Ux.future(config.put("form", form.put("ui", ui))));
    }

    //    private JsonObject mountIn(final JsonObject data) {
    //        Ke.mountString(data, KName.Ui.HIDDEN);
    //        Ke.mountString(data, KName.Ui.ROW);
    //        Ke.mountString(data, KName.METADATA);
    //        return data;
    //    }
    //
    //    private JsonObject mountOut(final JsonObject data) {
    //        Ke.mountArray(data, KName.Ui.HIDDEN);
    //        Ke.mount(data, KName.Ui.ROW);
    //        Ke.mount(data, KName.METADATA);
    //        return data;
    //    }
}
