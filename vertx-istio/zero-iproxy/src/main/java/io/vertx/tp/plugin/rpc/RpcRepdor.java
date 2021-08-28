package io.vertx.tp.plugin.rpc;

import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ipc.eon.IpcResponse;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.web._500UnexpectedRpcException;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.micro.ipc.DataEncap;

public class RpcRepdor {

    private static final Annal LOGGER = Annal.get(RpcRepdor.class);

    private transient final Class<?> clazz;

    private RpcRepdor(final Class<?> clazz) {
        this.clazz = clazz;
    }

    public static RpcRepdor create(final Class<?> clazz) {
        return new RpcRepdor(clazz);
    }

    public void replyJson(
        final Promise<JsonObject> handler,
        final AsyncResult<IpcResponse> response) {
        if (response.succeeded()) {
            final Envelop json = DataEncap.out(response.result());
            final JsonObject data = json.data();
            LOGGER.info(Info.CLIENT_RESPONSE, data);
            handler.complete(data);
        } else {
            final Throwable ex = response.cause();
            if (null != ex) {
                final Envelop envelop =
                    Envelop.failure(new _500UnexpectedRpcException(this.clazz, ex));
                handler.complete(envelop.outJson());
                // TODO: Debug Now, Remove In Future
                ex.printStackTrace();
            }
        }
    }

    public void reply(
        final Promise<Envelop> handler,
        final AsyncResult<IpcResponse> response
    ) {
        if (response.succeeded()) {
            handler.complete(DataEncap.out(response.result()));
        } else {
            final Throwable ex = response.cause();
            if (null != ex) {
                final Envelop envelop =
                    Envelop.failure(new _500UnexpectedRpcException(this.clazz, ex));
                handler.complete(envelop);
                // TODO: Debug Now, Remove In Future
                ex.printStackTrace();
            }
        }
    }
}
