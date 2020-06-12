package cn.vertxup.ui.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ui.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.ID;

import javax.ws.rs.*;

@EndPoint
@Path("/api")
public interface FormApi {
    /*
     * update form and related fields and options
     */
    @Path("/ui-form/cascade/:key")
    @PUT
    @Address(Addr.Control.PUT_FORM_CASCADE)
    JsonArray putFormCascade(@HeaderParam(ID.Header.X_SIGMA) String sigma,
                         @PathParam(KeField.KEY) String key,
                         @BodyParam JsonObject body);

    /*
     * delete form and related fields and options
     */
    @Path("/ui-form/:key")
    @DELETE
    @Address(Addr.Control.DELETE_FORM)
    Boolean deleteForm(@HeaderParam(ID.Header.X_SIGMA) String sigma,
                         @PathParam(KeField.KEY) String key);

}
