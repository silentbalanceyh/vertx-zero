package io.horizon.spi.feature;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * This interface is for X_LINKAGE processing between
 * different modules, It's implemented by service loader instead of
 * other method, that's why here we defined inner `Channel` part
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Linkage {
    /*
     * Save Operation
     * 1. Add
     * 2. Update
     *
     * The parameter vector means that whether it's double direction
     * 1. vector = true, source -> target, target -> source
     * 2. vector = false, source <-> target
     * The key point is that `linkKey` calculation are different between these two.
     */
    Future<JsonArray> link(JsonArray linkage, boolean vector);

    /*
     * Delete Operation
     * 1. Remove
     * Here the criteria is condition to remove previous linkage
     */
    Future<Boolean> unlink(JsonObject criteria);

    /*
     * Fetch Operation
     */
    Future<JsonArray> fetch(JsonObject criteria);
}
