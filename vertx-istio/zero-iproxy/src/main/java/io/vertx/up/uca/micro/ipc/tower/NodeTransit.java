package io.vertx.up.uca.micro.ipc.tower;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.web._500RpcMethodInvokeException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.micro.ipc.client.TunnelClient;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;

/**
 * The middle point for method
 */
public class NodeTransit implements Transit {

    private static final Annal LOGGER = Annal.get(NodeTransit.class);
    private transient Method method;
    private transient Vertx vertx;

    @Override
    @SuppressWarnings("all")
    public Future<Envelop> async(final Envelop envelop) {
        // 1. Extract type
        final Object proxy = Ut.singleton(this.method.getDeclaringClass());
        // 2. Return data
        final Future<Envelop> returnValue = Fn.getJvm(
                () -> ReturnTransit.build(() -> this.method.invoke(proxy, envelop),
                        this.getClass(), this.method),
                this.method
        );
        Fn.outWeb(null == returnValue, LOGGER,
                _500RpcMethodInvokeException.class, this.getClass(), returnValue);
        // 3. Here process the next
        return returnValue.compose(item -> TunnelClient.create(this.getClass())
                .bind(this.vertx)
                .bind(this.method)
                .connect(Ux.fromEnvelop(item)));
    }

    @Override
    public Transit connect(final Method method) {
        this.method = method;
        return this;
    }


    @Override
    public Transit connect(final Vertx vertx) {
        this.vertx = vertx;
        return this;
    }
}
