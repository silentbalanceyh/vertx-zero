package cn.vertxup.route.wall;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.tp.route.init.RtPin;
import io.vertx.up.annotations.Authenticate;
import io.vertx.up.annotations.Wall;

@Wall(value = "jwt", path = "/api/*")
public class MicroRouter {

    private final static String NAME = RtPin.ipcAuth();

    @Authenticate
    public AuthenticationHandler authenticate(final Vertx vertx,
                                              final JsonObject config) {
        return null; // JwtOstium.create(JwtAuth.create(vertx, new JWTAuthOptions(config)).bind(() -> this));
    }

    public Future<Boolean> verify(final JsonObject data) {
        return null; // Ux.applyRpc(NAME, KeIpc.Sc.IPC_TOKEN_VERIFY, data).compose(Ke.Result::boolAsync);
    }

    public Future<Boolean> authorize(final JsonObject data) {
        return null; // Ux.applyRpc(NAME, KeIpc.Sc.IPC_TOKEN_ACCESS, data).compose(Ke.Result::boolAsync);
    }
}
