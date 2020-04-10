package cn.vertxup.rbac.ipc;

import cn.vertxup.rbac.wall.JwtWall;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeIpc;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.commune.Envelop;
import io.vertx.up.secure.Security;
import io.vertx.up.util.Ut;

/*
 * Ipc Server for current
 */
public class IpcServer {
    /*
     * This is specific because of @Inject may generate duplicated
     * implementation class in zero system.
     */
    private final transient Security security = Ut.singleton(JwtWall.class);

    @Ipc(KeIpc.Sc.IPC_TOKEN_VERIFY)
    public Future<JsonObject> verify(final Envelop envelop) {
        return this.security.verify(envelop.data())
                /* Token verified successfully */
                .compose(Ke.Result::boolAsync);
    }

    @Ipc(KeIpc.Sc.IPC_TOKEN_ACCESS)
    public Future<JsonObject> access(final Envelop envelop) {
        return this.security.access(envelop.data())
                /* Token access */
                .compose(Ke.Result::boolAsync);
    }
}
