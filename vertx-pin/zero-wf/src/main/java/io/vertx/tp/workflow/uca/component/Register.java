package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.MetaInstance;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Pool {
    ConcurrentMap<String, Register> THREAD_POOL = new ConcurrentHashMap<>();
}

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Register {

    static Register phantom(final JsonObject params, final MetaInstance metadata) {
        if (metadata.recordSkip()) {
            /*
             * Here are configuration in workflow as following:
             * {
             *      "record": {
             *          "virtual": true
             *      }
             * }
             *
             * - skip insert
             * - save = update
             * - update only
             */
            return Fn.poolThread(Pool.THREAD_POOL, RegisterV::new);
        } else {
            return instance(params);
        }
    }

    static Register instance(final JsonObject params) {
        if (params.containsKey(KName.RECORD)) {
            final Object record = params.getValue(KName.RECORD);
            if (record instanceof JsonObject) {
                // Json Processing
                return Fn.poolThread(Pool.THREAD_POOL, RegisterJ::new);
            } else if (record instanceof JsonArray) {
                // Array Processing
                return Fn.poolThread(Pool.THREAD_POOL, RegisterA::new);
            } else {
                // Single for nothing
                return Ut.singleton(RegisterN.class);
            }
        } else {
            // Single for nothing
            return Ut.singleton(RegisterN.class);
        }
    }

    /*
     * When insert new record
     */
    Future<JsonObject> insertAsync(JsonObject params, MetaInstance metadata);

    Future<JsonObject> updateAsync(JsonObject params, MetaInstance metadata);

    Future<JsonObject> saveAsync(JsonObject params, MetaInstance metadata);
}
