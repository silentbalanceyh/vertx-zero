package io.vertx.up.uca.micro.ipc.tower;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.web._500RpcMethodInvokeException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;

/**
 * The last point for method
 */
@SuppressWarnings("all")
public class FinalTransit implements Transit {

    private static final Annal LOGGER = Annal.get(FinalTransit.class);
    private transient Method method;
    private transient Vertx vertx;

    @Override
    public Future<Envelop> async(final Envelop data) {
        // 1. Extract type
        final Object proxy = Ut.singleton(this.method.getDeclaringClass());
        // 2. Async type
        final Future<Envelop> returnValue = Fn.getJvm(
                () -> ReturnTransit.build(() -> this.method.invoke(proxy, data),
                        this.getClass(), this.method),
                this.method
        );
        Fn.outWeb(null == returnValue, LOGGER,
                _500RpcMethodInvokeException.class, this.getClass(), returnValue);
        return returnValue;
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
