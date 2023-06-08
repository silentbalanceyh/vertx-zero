package io.vertx.mod.workflow.uca.modeling;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;


/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Register {

    Cc<String, Register> CC_REGISTER = Cc.openThread();

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
            return CC_REGISTER.pick(RegisterV::new, RegisterV.class.getName());
        } else {
            return instance(params);
        }
    }

    static Register instance(final JsonObject params) {
        if (params.containsKey(KName.RECORD)) {
            final Object record = params.getValue(KName.RECORD);
            if (record instanceof JsonObject) {
                // Json Processing
                return CC_REGISTER.pick(RegisterJ::new, RegisterJ.class.getName());
            } else if (record instanceof JsonArray) {
                // Array Processing
                return CC_REGISTER.pick(RegisterA::new, RegisterA.class.getName());
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
