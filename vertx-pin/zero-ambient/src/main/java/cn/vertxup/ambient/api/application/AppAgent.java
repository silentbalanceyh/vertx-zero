package cn.vertxup.ambient.api.application;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KWeb;
import jakarta.ws.rs.*;

@EndPoint
public interface AppAgent {
    /*
     * Desc: Get EmApp Data by Name
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
    JsonObject fetchMenus(@HeaderParam(KWeb.HEADER.X_APP_ID) String appId);

    /*
     * Get EmApp Information by Id ( Logged required )
     */
    @Path("/api/app")
    @GET
    @Address(Addr.App.BY_ID)
    JsonObject byId(@HeaderParam(KWeb.HEADER.X_APP_ID) String appId);

    /*
     * RESTful Api to Update Basic X_APP information
     */
    @Path("/api/app")
    @PUT
    @Address(Addr.App.UP_BY_ID)
    JsonObject updateBy(@HeaderParam(KWeb.HEADER.X_APP_ID) String appId,
                        @BodyParam JsonObject data);

    /*
     * Fetch all datasource
     * {
     *      "database": "",
     *      "history": "",
     *      "workflow": "",
     *      "atom": ""
     * }
     */
    @GET
    @Path("/api/database")
    @Address(Addr.Init.SOURCE)
    JsonObject database(@HeaderParam(KWeb.HEADER.X_APP_ID) String appId);

    /*
     * 1. Step 1: Update the Notice by `expiredAt` first
     * 2. Step 2: Query the valid `notice` records from the system
     */
    @POST
    @Path("/api/notice-dashboard")
    @Address(Addr.Init.NOTICE)
    JsonArray notice(@HeaderParam(KWeb.HEADER.X_APP_ID) String appId,
                     @BodyParam JsonObject condition);
}
