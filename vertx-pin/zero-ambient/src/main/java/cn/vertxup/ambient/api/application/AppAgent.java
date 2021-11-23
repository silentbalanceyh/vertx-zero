package cn.vertxup.ambient.api.application;

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
     * Desc: Get Application Data by Name
     * Request: GET /app/name/xxx?name=xxx
     * Input:
     {
        "name": "xxx"
     }
     * Output:
     {
        "key": "78fce5a2-17f3-4dac-a75c-7e751595015c",
        "name": "vie.app.zui",
        "code": "zui",
        "title": "Zero框架脚手架",
        "domain": "localhost",
        "appPort": 5100,
        "urlEntry": "/login/index",
        "urlMain": "/main/index",
        "path": "/zui",
        "route": "/zui",
        "active": true,
        "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
        "language": "cn"
     }
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
