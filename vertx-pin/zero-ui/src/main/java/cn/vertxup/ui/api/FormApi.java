package cn.vertxup.ui.api;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.ui.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;
import jakarta.ws.rs.*;

@EndPoint
@Path("/api")
public interface FormApi {
    /*
     * update form and related fields and options
     */
    @Path("/ui-form/cascade/:key")
    @PUT
    @Address(Addr.Control.PUT_FORM_CASCADE)
    JsonObject putFormCascade(@PathParam(KName.KEY) String key,
                              @BodyParam JsonObject body);

    /*
     * delete form and related fields and options
     */
    @Path("/ui-form/:key")
    @DELETE
    @Address(Addr.Control.DELETE_FORM)
    Boolean deleteForm(@PathParam(KName.KEY) String key);

}
