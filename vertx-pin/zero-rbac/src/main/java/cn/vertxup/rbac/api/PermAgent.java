package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import jakarta.ws.rs.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface PermAgent {
    /*
     * permission set fetching
     */
    @Path("/permission/groups/by/sigma")
    @GET
    @Address(Addr.Authority.PERMISSION_GROUP)
    JsonArray calculate();

    @Path("/permission/definition/saving")
    @PUT
    @Address(Addr.Authority.PERMISSION_DEFINITION_SAVE)
    JsonObject saveDefinition(@BodyParam JsonObject body);

    @Path("/permission/role/:roleId")
    @GET
    @Address(Addr.Authority.PERMISSION_BY_ROLE)
    JsonArray fetchAsync(@PathParam("roleId") String roleId);

    @Path("/permission/role/:roleId")
    @PUT
    @Address(Addr.Authority.PERMISSION_SAVE)
    JsonArray savePerm(@PathParam("roleId") String roleId,
                       @BodyParam JsonArray permissions);

    // ======================= CRUD Replace =============================
    /*
     * Single Api for the permission management of un-related
     * - 1) The permissions are not related to any S_PERM_SET
     * - 2) The actions are also not related to any S_PERMISSION
     * The query data format is the same as standard `search engine` api here
     * {
     *      "criteria": {},
     *      "projection": [],
     *      "pager": {
     *          "page": xx,
     *          "size": yy
     *      },
     *      "sorter": []
     * }
     */
    @Path("/permission/by/freedom")
    @POST
    @Address(Addr.Perm.PERMISSION_UN_READY)
    JsonArray searchUnReady(@BodyParam JsonObject query);

    @Path("/permission/:key")
    @GET
    @Address(Addr.Perm.BY_ID)
    JsonObject fetchById(@PathParam("key") String key);

    @Path("/permission")
    @POST
    @Address(Addr.Perm.ADD)
    JsonObject add(@BodyParam JsonObject param);

    @Path("/permission/:key")
    @PUT
    @Address(Addr.Perm.EDIT)
    JsonObject update(@PathParam("key") String key, @BodyParam JsonObject params);

    @Path("/permission/:key")
    @DELETE
    @Address(Addr.Perm.DELETE)
    JsonObject remove(@PathParam("key") String key);
}
