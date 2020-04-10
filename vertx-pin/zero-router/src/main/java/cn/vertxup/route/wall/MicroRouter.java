package cn.vertxup.route.wall;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.tp.route.init.RtPin;
import io.vertx.up.annotations.Authenticate;
import io.vertx.up.annotations.Wall;
import io.vertx.up.secure.Security;
import io.vertx.up.secure.handler.JwtOstium;
import io.vertx.up.secure.provider.JwtAuth;

@Wall(value = "jwt", path = "/api/*")
public class MicroRouter implements Security {

    private final static String NAME = RtPin.ipcAuth();

    @Authenticate
    public AuthHandler authenticate(final Vertx vertx,
                                    final JsonObject config) {
        return JwtOstium.create(JwtAuth.create(vertx, new JWTAuthOptions(config))
                .bind(() -> this));
    }

    @Override
    public Future<Boolean> verify(final JsonObject data) {
        return null; // Ux.applyRpc(NAME, KeIpc.Sc.IPC_TOKEN_VERIFY, data).compose(Ke.Result::boolAsync);
    }

    @Override
    public Future<Boolean> access(final JsonObject data) {
        return null; // Ux.applyRpc(NAME, KeIpc.Sc.IPC_TOKEN_ACCESS, data).compose(Ke.Result::boolAsync);
    }
}
