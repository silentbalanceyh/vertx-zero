package cn.originx.migration;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.atom.JtApp;

public interface MigrateStep {

    MigrateStep bind(final JtApp app);

    /*
     * 升级专用
     */
    Future<JsonObject> procAsync(JsonObject config);
}
