package cn.vertxup.rbac.wall;

import cn.vertxup.rbac.wall.zaas.CommonZaaS;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.up.annotations.Authenticate;
import io.vertx.up.annotations.Wall;

@Wall(value = "jwt", path = "/api/*", executor = CommonZaaS.class)
public class JwtWall {

    @Authenticate
    public AuthenticationHandler authenticate(final Vertx vertx,
                                              final JsonObject config) {
        System.out.println(config.encodePrettily());
        return null;//JwtOstium.create(JwtAuth.create(vertx, new JWTAuthOptions(config).bind(() -> this));
    }
}
