package cn.vertxup.ambient.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.em.ActivityStatus;

public interface ActivityStub {

    String ACTIVITY_ID = "activityId";

    /*
     * Activity & Activity Change
     */
    Future<JsonObject> fetchActivity(String id);

    /*
     * Activities fetching from the system by
     * 1) model identifier
     * 2) model key: primary key for record
     */
    Future<JsonArray> fetchActivities(String identifier, String key);

    /*
     * Activity Update ( Confirm / Reject )
     * Here are some points that
     * 1) For `ActivityChange` record, update `PENDING` to `CONFIRMED`
     * 2) For `ActivityChange` record, keep `SYSTEM` status
     */
    Future<JsonArray> saveChanges(String id, ActivityStatus status);

    /*
     * Fetch activity changes by activity id here
     * Returned changes of each
     */
    Future<JsonArray> fetchChanges(String activityId);
}
