package io.vertx.tp.workflow.plugin.activity;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.uca.sectio.After;
import io.vertx.up.uca.sectio.Around;
import io.vertx.up.unity.Ux;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ActivityTabb implements After {
    @Override
    public Set<ChangeFlag> types() {
        return Around.TYPE_ALL;
    }

    @Override
    public Future<JsonObject> afterAsync(final JsonObject data, final JsonObject config) {
        System.out.println(data.encodePrettily());
        return Ux.future(data);
    }
}
