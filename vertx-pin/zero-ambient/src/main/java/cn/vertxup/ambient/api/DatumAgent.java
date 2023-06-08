package cn.vertxup.ambient.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KWeb;
import jakarta.ws.rs.*;

@EndPoint
@Path("/api")
public interface DatumAgent {

    @Path("/type/categories/{type}")
    @GET
    @Address(Addr.Datum.CATEGORY_TYPE)
    JsonArray categoryByType(@HeaderParam(KWeb.HEADER.X_APP_ID) String appId,
                             @PathParam("type") String type,
                             @QueryParam("leaf") @DefaultValue("true") Boolean includeLeaf);

    @Path("/types/categories")
    @POST
    @Address(Addr.Datum.CATEGORY_TYPES)
    JsonObject fetchCategories(@HeaderParam(KWeb.HEADER.X_APP_ID) String appId,
                               @BodyParam JsonArray types);

    @Path("/{type}/category/{code}")
    @GET
    @Address(Addr.Datum.CATEGORY_CODE)
    JsonArray fetchCategory(@HeaderParam(KWeb.HEADER.X_APP_ID) String appId,
                            @PathParam("type") String type,
                            @PathParam("code") String code);

    @Path("/type/tabulars/{type}")
    @GET
    @Address(Addr.Datum.TABULAR_TYPE)
    JsonArray tabularByType(@HeaderParam(KWeb.HEADER.X_APP_ID) String appId,
                            @PathParam("type") String type);

    @Path("/types/tabulars")
    @POST
    @Address(Addr.Datum.TABULAR_TYPES)
    JsonObject fetchTabulars(@HeaderParam(KWeb.HEADER.X_APP_ID) String appId,
                             @BodyParam JsonArray types);

    @Path("/{type}/tabular/{code}")
    @GET
    @Address(Addr.Datum.TABULAR_CODE)
    JsonArray fetchTabular(@HeaderParam(KWeb.HEADER.X_APP_ID) String appId,
                           @PathParam("type") String type,
                           @PathParam("code") String code);

}
