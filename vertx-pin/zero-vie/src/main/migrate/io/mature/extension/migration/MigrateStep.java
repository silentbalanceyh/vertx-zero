package io.mature.extension.migration;

import io.macrocosm.specification.program.HArk;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface MigrateStep {

    MigrateStep bind(final HArk ark);

    /*
     * 升级专用
     */
    Future<JsonObject> procAsync(JsonObject config);
}
