package cn.vertxup.ambient.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import jakarta.ws.rs.*;

/**
 * # Zero History Api Definition
 *
 * This is interface to query history of each records from zero framework.
 * It's for `X_ACTIVITY` and `X_ACTIVITY_CHANGE` table that enabled following features.
 *
 * 1. Trash to store all the records that have been deleted from our system.
 * 2. Query all records' histories that have been removed.
 */
@EndPoint
@Path("/api")
public interface HistoryAgent {

    @Path("/history/:identifier/:key")
    @GET
    @Address(Addr.History.HISTORIES)
    Future<JsonArray> fetch(@PathParam("identifier") String identifier,
                            @PathParam("key") String key);

    @Path("/history/:identifier/:key/:field")
    @GET
    @Address(Addr.History.HISTORY_BY_FIELDS)
    Future<JsonArray> fetch(@PathParam("identifier") String identifier,
                            @PathParam("key") String key,
                            @PathParam("field") String field);

    @Path("/history/:key")
    @GET
    @Address(Addr.History.HISTORY_ITEMS)
    Future<JsonArray> fetchItems(@PathParam("key") String key);

    /*
     * The activity change histories page needed
     * Only support two read method:
     *
     * 1) Search by query engine
     * 2) Get activity by id value here
     */
    @Path("/x-activity/search")
    @POST
    @Address(Addr.History.ACTIVITY_SEARCH)
    Future<JsonObject> searchActivities(@BodyParam JsonObject body);

    @Path("/x-activity/:key")
    @GET
    @Address(Addr.History.ACTIVITY_GET)
    Future<JsonObject> fetchActivity(@PathParam("key") String key);
}
