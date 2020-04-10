package cn.vertxup.ambient.api;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.ID;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@EndPoint
public interface AppAgent {
    /*
     * Get Application Data by Name
     */
    @Path("/app/name/{name}")
    @GET
    @Address(Addr.App.BY_NAME)
    JsonObject byName(@PathParam("name") String name);

    @Path("/api/menus")
    @GET
    @Address(Addr.Menu.BY_APP_ID)
    JsonObject fetchMenus(@HeaderParam(ID.Header.X_APP_ID) String appId);

    /*
     * Get Application Information by Id ( Logged required )
     */
    @Path("/api/app")
    @GET
    @Address(Addr.App.BY_ID)
    JsonObject byId(@HeaderParam(ID.Header.X_APP_ID) String appId);
}
