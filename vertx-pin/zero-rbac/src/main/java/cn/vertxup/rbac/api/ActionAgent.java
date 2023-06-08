package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KWeb;
import jakarta.ws.rs.BodyParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface ActionAgent {
    /*
     * Step 2
     * RESTful Api stored in SEC_ACTION for all authorized, instead of
     * get api information from zero engine, here we provide another interface
     * to seek action for `/api/x-action/seek` here.
     *
     * Here are some spec situation for this interface, this interface could be used to seek
     * RESTful Apis
     * 1) All the static Uris could be authorized by `UriAeon` class ( Routing Manager )
     * 2) All the dynamic Uris could be authorized by `I_API` in `zero-jet` project
     *
     * Critical: All the records came from `SEC_ACTION` table instead of above two points.
     */
    @POST
    @Path("action/seek")
    @Address(Addr.Authority.ACTION_SEEK)
    JsonArray searchAuthorized(@HeaderParam(KWeb.HEADER.X_SIGMA) String sigma,
                               @BodyParam JsonObject params);

    /*
     * Step 1
     * Another interface for security resource definition here.
     * This RESTful apis will be used in `Resource Definition` module, this method is combined by
     * `UriAeon` and `I_API` both
     *
     * 1) `UriAeon` could provide all the static Uris
     * 2) `I_API` must be called via channel to get raw APIs that have not been authorized here.
     *
     * Critical:
     * 1) The APIs could be authorized only once ( 1:1 in API & Action, 1:1 in Action & Resource )
     * 2) All the APIs that have be authorized will be show the status here
     */
    @POST
    @Path("action/ready")
    @Address(Addr.Authority.ACTION_READY)
    JsonArray searchAll(@HeaderParam(KWeb.HEADER.X_SIGMA) String sigma,
                        @BodyParam JsonObject params);
}
