package cn.vertxup.rbac.ipc;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ke.cv.KeIpc;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.commune.Envelop;

/*
 * Ipc Server for current
 */
public class IpcServer {
    /*
     * This is specific because of @Inject may generate duplicated
     * implementation class in zero system.
     */

    @Ipc(KeIpc.Sc.IPC_TOKEN_VERIFY)
    public Future<JsonObject> verify(final Envelop envelop) {
        return null;
    }

    @Ipc(KeIpc.Sc.IPC_TOKEN_ACCESS)
    public Future<JsonObject> access(final Envelop envelop) {
        return null;
    }
}
