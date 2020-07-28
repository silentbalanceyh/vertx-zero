package cn.vertxup.api;

import io.vertx.core.json.JsonArray;
import io.vertx.tp.atom.cv.Addr;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.ID;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@EndPoint
@Path("/api")
public interface ModelAgent {
    @Path("/model/:identifier/fields")
    @GET
    @Address(Addr.Model.MODEL_FIELDS)
    JsonArray modelAttributes(@PathParam(KeField.IDENTIFIER) String identifier);

    @Path("/model")
    @GET
    @Address(Addr.Model.MODELS)
    JsonArray models(@HeaderParam(ID.Header.X_SIGMA) String appId);
}
