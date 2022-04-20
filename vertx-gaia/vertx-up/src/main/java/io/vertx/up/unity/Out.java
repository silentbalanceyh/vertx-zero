package io.vertx.up.unity;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class Out {
    /*
     * Output Normalized for new specification such as following:
     *
     * {
     *     "__data": "previous data before updating",
     *     "__flag": "current operation flag: ADD | UPDATE | DELETE "
     * }
     *
     * Here are specification that's stored in KName.__
     * - __metadata:    Metadata data that are stored ( field = type ) part
     * - __data:        This node stored previous data record
     * - __flag:        This node identified current operation: ADD | UPDATE | DELETE
     *
     * The final situation is as following
     * 1) Add new record:       __data = null,     __flag   ( Could ignored )
     * 2) Update record:        __data = previous, __flag   ( Could ignored )
     * 3) Delete record:        __data = previous, __flag   ( Must be DELETE )
     */
    static Future<JsonObject> effect(final JsonObject current, final JsonObject previous) {

        return null;
    }

    static Future<JsonArray> effect(final JsonArray current, final JsonArray previous, final String field) {

        return null;
    }
}
