package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;
import jakarta.ws.rs.*;

@EndPoint
@Path("/api")
public interface UserAgent {

    /*
     * /api/user/password
     * Request: Update user password here
     */
    @POST
    @Path("user/password")
    @Address(Addr.User.PASSWORD)
    JsonObject password(@BodyParam JsonObject params);

    /*
     * /api/user/profile
     * Request: Update user information
     */
    @POST
    @Path("user/profile")
    @Address(Addr.User.PROFILE)
    JsonObject profile(@BodyParam JsonObject params);

    /*
     * /user/logout
     * 1. Remove token from System
     * 2. Remove pool permission
     */
    @POST
    @Path("user/logout")
    @Address(Addr.Auth.LOGOUT)
    JsonObject logout();

    /**
     * modified by Hongwei at 2019/12/06
     * add get, create, update and delete methods for user domain.
     */
    @GET
    @Path("/user/:key")
    @Address(Addr.User.GET)
    JsonObject getById(@PathParam("key") String key);

    @POST
    @Path("/user")
    @Address(Addr.User.ADD)
    JsonObject create(@BodyParam JsonObject data);

    @PUT
    @Path("/user/:key")
    @Address(Addr.User.UPDATE)
    JsonObject update(@PathParam("key") String key,
                      @BodyParam JsonObject data);

    @DELETE
    @Path("/user/:key")
    @Address(Addr.User.DELETE)
    Boolean delete(@PathParam("key") String key);

    // ---------------- All Usage business api for `user + type` extracting

    /*
     * The user usage in zero extension
     *
     * 1. User Management ( /api/user/search ) RBAC Module
     * 2. 「By Type」Employee Importing
     * 3. 「By Selection」Employee Selecting with usage ( Combine condition )
     */
    @POST
    @Path("/user/search/:identifier")
    @Address(Addr.User.QR_USER_SEARCH)
    JsonObject searchByType(@PathParam(KName.IDENTIFIER) String identifier,
                            @BodyParam JsonObject criteria);

    /*
     * /api/user
     * Request: get user id from token
     */
    @GET
    @Path("user")
    @Address(Addr.User.INFORMATION)
    JsonObject information();
}
