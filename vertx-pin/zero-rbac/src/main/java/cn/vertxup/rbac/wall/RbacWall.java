package cn.vertxup.rbac.wall;

import cn.vertxup.rbac.service.accredit.AccreditStub;
import cn.vertxup.rbac.service.jwt.JwtStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.annotations.Authenticate;
import io.vertx.up.annotations.Authorized;
import io.vertx.up.annotations.AuthorizedResource;
import io.vertx.up.annotations.Wall;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;

import javax.inject.Inject;

/**
 * Interface defined for component
 */
@Wall(value = "extension", path = "/api/*")
public class RbacWall {
    private static final Annal LOGGER = Annal.get(RbacWall.class);
    @Inject
    private transient JwtStub jwtStub;
    @Inject
    private transient AccreditStub accredit;

    @Authenticate
    public Future<Boolean> authenticate(final JsonObject data) {
        final String token = data.getString(KName.ACCESS_TOKEN);
        final String user = data.getString(KName.USER);
        // No Cache
        Sc.infoAuth(LOGGER, AuthMsg.TOKEN_INPUT, token, user);
        return this.jwtStub.verify(user, token);
    }

    @Authorized
    public Future<JsonObject> authorize(final User user) {
        return this.accredit.profile(user);
    }

    @AuthorizedResource
    public Future<JsonObject> resource(final JsonObject params) {
        return this.accredit.resource(params);
    }
}
