package cn.vertxup.rbac.wall;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Authenticate;
import io.vertx.up.annotations.Authorization;
import io.vertx.up.annotations.Wall;
import io.vertx.up.secure.ZaaS;

import javax.inject.Inject;

@Wall(value = "jwt", path = "/api/*")
public class JwtWall {
    @Inject
    private transient ZaaS aaS;

    @Authenticate
    public Future<Boolean> authenticate(final JsonObject data) {
        return this.aaS.verify(data);
    }

    @Authorization
    public Future<Boolean> authorization(final JsonObject data) {
        return this.aaS.authorize(data);
    }
}
