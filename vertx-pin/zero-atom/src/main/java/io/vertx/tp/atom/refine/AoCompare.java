package io.vertx.tp.atom.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.up.eon.em.ChangeFlag;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AoCompare {
    private AoCompare() {
    }

    /*
     * Database Change
     */
    static ConcurrentMap<ChangeFlag, JsonArray> diffPure(
            final JsonArray original, final JsonArray current,
            final DataAtom atom, final Set<String> ignoreSet
    ) {
        return null;
    }

    /*
     * Database -> Integration
     */
    static ConcurrentMap<ChangeFlag, JsonArray> diffPush(
            final JsonArray original, final JsonArray current,
            final DataAtom atom, final Set<String> ignoreSet
    ) {
        return null;
    }

    /*
     * Integration -> Database
     */
    static ConcurrentMap<ChangeFlag, JsonArray> diffPull(
            final JsonArray original, final JsonArray current,
            final DataAtom atom, final Set<String> ignoreSet
    ) {
        return null;
    }
}
