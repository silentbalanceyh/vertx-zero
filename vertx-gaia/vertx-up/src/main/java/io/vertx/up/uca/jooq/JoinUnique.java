package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class JoinUnique {
    private final transient JoinStore store;

    JoinUnique(final JoinStore store) {
        this.store = store;
    }

    // Unique
    Future<JsonObject> fetchById(final String key, final boolean isASub) {

        return Ux.futureJ();
    }
}
