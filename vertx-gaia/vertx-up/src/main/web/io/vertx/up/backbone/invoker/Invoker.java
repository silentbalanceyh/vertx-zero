package io.vertx.up.backbone.invoker;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.up.commune.Envelop;

import java.lang.reflect.Method;

/**
 * Replier for method invoking
 */
public interface Invoker {
    /**
     * Ensure correct invoking
     *
     * @param returnType Method return type
     * @param paramCls   Method parameters
     */
    void ensure(final Class<?> returnType, final Class<?> paramCls);

    /**
     * Invoke method and replying
     *
     * @param proxy   Proxy object reference
     * @param method  Method reference for reflection
     * @param message Message handler
     */
    void invoke(Object proxy, Method method, Message<Envelop> message);

    /**
     * Invoke method and ( Ipc ) then replying
     *
     * @param proxy   Proxy object reference
     * @param method  Method reference for reflection
     * @param message Message handler
     * @param vertx   Vertx reference
     */
    void next(Object proxy, Method method, Message<Envelop> message, Vertx vertx);

    /**
     * Invoke method normalized, this api may be more useful
     *
     * @param proxy   Proxy object reference
     * @param method  Method reference for reflection
     * @param input   Envelop as input part
     * @param handler Async Handler to handle returned T
     * @param <I>     Input Type
     * @param <O>     Output Type
     */
    <I, O> void handle(Object proxy, Method method, I input, Handler<AsyncResult<O>> handler);
}
