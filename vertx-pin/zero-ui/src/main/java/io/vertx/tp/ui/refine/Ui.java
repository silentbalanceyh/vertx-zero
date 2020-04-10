package io.vertx.tp.ui.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;

public class Ui {
    /*
     * Log
     */
    public static void infoInit(final Annal logger, final String pattern, final Object... args) {
        UiLog.infoInit(logger, pattern, args);
    }

    public static void infoUi(final Annal logger, final String pattern, final Object... args) {
        UiLog.infoUi(logger, pattern, args);
    }

    public static void infoWarn(final Annal logger, final String pattern, final Object... args) {
        UiLog.infoWarn(logger, pattern, args);
    }

    /*
     * Search Option
     */
    public static JsonObject optSearch(final JsonObject input) {
        return UiOption.search(input);
    }

    public static JsonObject optQuery(final JsonObject input) {
        return UiOption.query(input);
    }

    public static JsonObject optFragment(final JsonObject input) {
        return UiOption.fragment(input);
    }

    public static JsonObject optTable(final JsonObject input) {
        return UiOption.table(input);
    }
}
