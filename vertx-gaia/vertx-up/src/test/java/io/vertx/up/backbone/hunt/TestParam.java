package io.vertx.up.backbone.hunt;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.EndPoint;
import jakarta.ws.rs.*;

import java.math.BigDecimal;
import java.util.Date;

@EndPoint
public class TestParam {

    @Path("/query/{name}")
    @GET
    public void test(
        final @DefaultValue("Str0") @QueryParam("str0") String str1,
        final @DefaultValue("12") @FormParam("int0") Integer int0,
        final @DefaultValue("33") @PathParam("name") int int1,
        final @DefaultValue("false") @HeaderParam("present") Boolean present,
        final @DefaultValue("33.3") @CookieParam("cookie") BigDecimal decimal,
        final @DefaultValue("33.5") @QueryParam("amount") float amount,
        final @DefaultValue("33.3") @CookieParam("double") double wrapper,
        final @DefaultValue("2012-11-11") @QueryParam("date") Date created,
        final @DefaultValue("{\"name\":\"Lang\"}") @QueryParam("test") JsonObject data
    ) {
        System.out.println(int0);
    }
}
