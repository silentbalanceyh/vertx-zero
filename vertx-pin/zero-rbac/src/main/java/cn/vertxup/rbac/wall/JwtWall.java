package cn.vertxup.rbac.wall;

import cn.vertxup.rbac.service.accredit.AccreditStub;
import cn.vertxup.rbac.service.jwt.JwtStub;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.up.annotations.Authenticate;
import io.vertx.up.annotations.Wall;
import io.vertx.up.log.Annal;

import javax.inject.Inject;

@Wall(value = "jwt", path = "/api/*")
public class JwtWall {
    private static final Annal LOGGER = Annal.get(JwtWall.class);
    @Inject
    private transient JwtStub jwtStub;
    @Inject
    private transient AccreditStub accredit;

    @Authenticate
    public AuthenticationHandler authenticate(final Vertx vertx,
                                              final JsonObject config) {
        return null;//JwtOstium.create(JwtAuth.create(vertx, new JWTAuthOptions(config).bind(() -> this));
    }
}
