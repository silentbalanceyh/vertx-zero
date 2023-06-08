package io.vertx.mod.ui.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ui.atom.UiJson;
import io.vertx.up.util.Ut;

class UiOption {

    static JsonObject search(final JsonObject search) {
        return UiJson.create(search.copy())
            .remove("key")
            .convert("cond", "search.cond")
            .convert("enabled", "search.enabled")
            .convert("advanced", "search.advanced")
            .convert("opRedo", "search.op.redo")
            .convert("opAdvanced", "search.op.advanced")
            .convert("opView", "search.op.view")
            .convert("confirmClear", "search.confirm.clear")
            .convert("placeholder", "search.placeholder")
            .convert("advancedWidth", "search.advanced.width")
            .convert("advancedTitle", "search.advanced.title")
            .convert("advancedNotice", "search.advanced.notice")
            .convertChild("advancedView", "search.criteria")
            .to();
    }

    static JsonObject query(final JsonObject query) {
        return UiJson.create(query.copy())
            .remove("key")
            .to();
    }

    static JsonObject fragment(final JsonObject input) {
        final JsonObject fragment = UiJson.create(input.copy())
            .remove("key", "container", "config")
            .convert("buttonConnect", "button")
            .convert("buttonGroup", "buttons")
            .to();
        final JsonObject config = input.getJsonObject("config");
        if (Ut.isNotNil(config)) {
            fragment.mergeIn(config.copy(), true);
        }
        return fragment;
    }

    static JsonObject table(final JsonObject table) {
        final JsonObject tableJson = new JsonObject();
        /*
         * total
         */
        final JsonObject total = UiJson.create(table)
            .pickupWith("total")
            .convert("totalReport", "report")
            .convert("totalSelected", "selected")
            .to();
        tableJson.put("total", total);
        /*
         * row
         */
        final JsonObject row = UiJson.create(table)
            .pickupWith("row")
            .replaceWith("row", "on")
            .to();
        tableJson.put("row", row);
        /*
         * opColumn
         */
        final JsonObject column = UiJson.create(table)
            .pickupWith("op")
            .convert("opTitle", "title")
            .convert("opDataIndex", "dataIndex")
            .convert("opFixed", "fixed")
            .convert("opConfig", "$option")
            .add("$render", "EXECUTOR")
            .to();
        tableJson.put("columns", new JsonArray().add(column));
        /*
         * Other attribute
         */
        final JsonObject rest = UiJson.create(table)
            .removeWith("op")
            .removeWith("total")
            .removeWith("row")
            .remove("key")
            .to();
        tableJson.mergeIn(rest, true);
        return tableJson;
    }
}
