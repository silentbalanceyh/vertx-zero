package io.vertx.tp.ui.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;

import java.util.function.Supplier;

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

    public static void infoView(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        UiLog.infoView(logger, pattern, args);
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

    /*
     * Cache Part
     */
    public static Future<JsonObject> cacheControl(final JsonObject body, final Supplier<Future<JsonObject>> executor) {
        return UiCache.cacheControl(body, executor);
    }

    public static Future<JsonArray> cacheOps(final JsonObject body, final Supplier<Future<JsonArray>> executor) {
        return UiCache.cacheOps(body, executor);
    }

}
