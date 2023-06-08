package io.vertx.mod.ui.uca;

import cn.vertxup.ui.service.ListStub;
import io.horizon.spi.ui.UiControl;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ui.refine.Ui;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/*
 * List configuration data processing.
 */
public class ListCombiner implements UiControl {
    @Override
    public Future<JsonObject> executeAsync(final JsonObject input) {
        final JsonObject normalized = new JsonObject();
        /*
         * query processing
         */
        final JsonObject query = input.getJsonObject(ListStub.FIELD_V_QUERY);
        if (Ut.isNotNil(query)) {
            normalized.put("query", Ui.optQuery(query));
        }
        /*
         * options -> search
         */
        final JsonObject options = new JsonObject();
        final JsonObject search = input.getJsonObject(ListStub.FIELD_V_SEARCH);
        if (Ut.isNotNil(search)) {
            final JsonObject processed = Ui.optSearch(search);
            options.mergeIn(processed, true);
        }
        /*
         * common options from record
         */
        options.put("dynamic.op", Boolean.TRUE);
        options.put("dynamic.switch", input.getBoolean("dynamicSwitch"));
        options.put("dynamic.column", input.getBoolean("dynamicColumn"));
        /*
         * Merged
         */
        final JsonObject commonOpts = input.getJsonObject("options");
        if (Ut.isNotNil(commonOpts)) {
            options.mergeIn(commonOpts.copy(), true);
        }
        final JsonObject optionAjax = input.getJsonObject("optionsAjax");
        if (Ut.isNotNil(optionAjax)) {
            options.mergeIn(optionAjax.copy(), true);
        }
        final JsonObject optionSubmit = input.getJsonObject("optionsSubmit");
        if (Ut.isNotNil(optionSubmit)) {
            options.mergeIn(optionSubmit.copy(), true);
        }
        /*
         * component calculate
         */
        final JsonObject fragment = input.getJsonObject(ListStub.FIELD_V_SEGMENT);
        final JsonObject component = new JsonObject();
        fragment.fieldNames().forEach(field -> {
            /*
             * fragment
             */
            final JsonObject fragmentJson = fragment.getJsonObject(field);
            if (Ut.isNotNil(fragmentJson)) {
                final JsonObject config = Ui.optFragment(fragmentJson);
                /*
                 * options, window + field
                 * component, field
                 */
                component.put(field, config);
                /*
                 * key
                 */
                final String opKey = "window." + field;
                options.put(opKey, fragmentJson.getValue("container"));
            }
        });
        normalized.put("options", options);
        normalized.put("component", component);
        /*
         * table calculate
         */
        final JsonObject table = input.getJsonObject(ListStub.FIELD_V_TABLE);
        if (Ut.isNotNil(table)) {
            normalized.put("table", Ui.optTable(table));
        }
        return Ux.future(normalized);
    }
}
