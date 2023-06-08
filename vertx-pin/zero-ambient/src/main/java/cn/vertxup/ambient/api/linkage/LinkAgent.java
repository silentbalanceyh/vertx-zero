package cn.vertxup.ambient.api.linkage;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;
import jakarta.ws.rs.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface LinkAgent {
    /*
     * Fetch all linkages by
     * {
     *      "sigma": "identify application",
     *      "type": "the type means the category of linkage in current system, Fixed"
     * }
     */
    @GET
    @Path("/linkage/type/:type")
    @Address(Addr.Linkage.FETCH_BY_TYPE)
    JsonArray fetchByType(@PathParam(KName.TYPE) String type);

    /*
     * Fetch all by
     * - sourceKey
     */
    @GET
    @Path("/linkage/v/source/:key")
    @Address(Addr.Linkage.FETCH_TARGET)
    JsonArray fetchTarget(@PathParam(KName.KEY) String key);

    /*
     * Fetch all by
     * - targetKey
     */
    @GET
    @Path("/linkage/v/target/:key")
    @Address(Addr.Linkage.FETCH_SOURCE)
    JsonArray fetchSource(@PathParam(KName.KEY) String key);

    /*
     * Fetch all by
     * - key sourceKey or targetKey
     */
    @GET
    @Path("/linkage/b/:key")
    @Address(Addr.Linkage.FETCH_ST)
    JsonArray fetchSt(@PathParam(KName.KEY) String key);

    @POST
    @Path("/linkage/b/batch/save")
    @Address(Addr.Linkage.SAVE_BATCH_B)
    JsonArray batchSaveB(@BodyParam JsonArray data);

    @POST
    @Path("/linkage/v/batch/save")
    @Address(Addr.Linkage.SAVE_BATCH_V)
    JsonArray batchSaveV(@BodyParam JsonArray data);

    /*
     * Three part of following:
     * {
     *     removed: []
     *     data: []
     * }
     * - removed: key set of removed linkage, the key is X_LINKAGE primary key
     * - data: JsonArray data of linkage that should be stored.
     */
    @POST
    @Path("/linkage/sync/b")
    @Address(Addr.Linkage.SYNC_B)
    JsonArray syncB(@BodyParam JsonObject request);

    // ----------------- Spec for CRUD
    @POST
    @Path("/linkage/b/:type")
    @Address(Addr.Linkage.ADD_NEW_B)
    JsonObject createB(@PathParam(KName.TYPE) String type,
                       @BodyParam JsonObject body);

    @POST
    @Path("/linkage/v/:type")
    @Address(Addr.Linkage.ADD_NEW_V)
    JsonObject createV(@PathParam(KName.TYPE) String type,
                       @BodyParam JsonObject body);

    @GET
    @Path("/linkage/:key")
    @Address(Addr.Linkage.FETCH_BY_KEY)
    JsonObject fetch(@PathParam(KName.KEY) String key);

    @DELETE
    @Path("/linkage/:key")
    @Address(Addr.Linkage.REMOVE_BY_REGION)
    JsonObject remove(@PathParam(KName.KEY) String key);
}
