package io.vertx.mod.ui.refine;

import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.function.Supplier;

public class Ui {

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

    public interface LOG {
        String MODULE = "διεπαφή χρήστη";

        LogModule Init = Log.modulat(MODULE).program("Init");
        LogModule Ui = Log.modulat(MODULE).program("Ui");
        LogModule View = Log.modulat(MODULE).program("View");
    }
}
