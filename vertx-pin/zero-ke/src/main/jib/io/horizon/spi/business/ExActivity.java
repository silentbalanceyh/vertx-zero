package io.horizon.spi.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Fetch `Activity` based on `different condition`
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface ExActivity {
    // --------------------- X_ACTIVITY ------------------------
    /*
     * Activities fetching from the system by
     * 1) model identifier
     * 2) model key: primary key for record
     */
    Future<JsonArray> activities(String modelId, String modelKey);

    Future<JsonArray> activities(JsonObject condition);

    /*
     * Activity & Activity Change
     */
    Future<JsonObject> activity(String activityId);

    // --------------------- X_ACTIVITY_CHANGE ------------------------
    /*
     * Activities fetching from the system by ( field )
     * 1) model identifier
     * 2) model key: primary key for record
     * 3) model field
     *
     * The results should be all activity changes of one field that selected,
     * it will be mapped to X_ACTIVITY_CHANGE table instead of X_ACTIVITY
     */
    Future<JsonArray> changes(String modelId, String modelKey, String field);

    /*
     * Fetch activity changes by activity id here
     * Returned changes of each
     */
    Future<JsonArray> changes(String activityId);
}
