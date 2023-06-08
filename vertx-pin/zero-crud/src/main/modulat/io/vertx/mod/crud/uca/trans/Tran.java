package io.vertx.mod.crud.uca.trans;

import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.cv.Pooled;
import io.vertx.mod.crud.uca.desk.IxMod;

/*
 * {
 *      "transform": {
 *      }
 * }
 * Processing for "transform"
 */
public interface Tran {

    static Tran fabric(final boolean isFrom) {
        return Pooled.CC_TRAN.pick(() -> new FabricTran(isFrom), FabricTran.class.getName() + isFrom);
    }

    static Tran tree(final boolean isFrom) {
        return Pooled.CC_TRAN.pick(() -> new TreeTran(isFrom), TreeTran.class.getName() + isFrom);
    }

    static Tran map(final boolean isFrom) {
        return Pooled.CC_TRAN.pick(() -> new MapTran(isFrom), MapTran.class.getName() + isFrom);
    }

    static Tran initial() {
        return Pooled.CC_TRAN.pick(InitialTran::new, InitialTran.class.getName());
    }

    // JsonObject -> JsonObject
    default Future<JsonObject> inJAsync(final JsonObject data, final IxMod in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    // JsonArray -> JsonArray
    default Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    // JsonArray -> JsonObject
    default Future<JsonObject> inAJAsync(final JsonArray data, final IxMod in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    // JsonObject -> JsonArray
    default Future<JsonArray> inJAAsync(final JsonObject data, final IxMod in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }
}
