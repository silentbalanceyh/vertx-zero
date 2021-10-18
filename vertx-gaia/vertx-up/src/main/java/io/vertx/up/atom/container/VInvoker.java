package io.vertx.up.atom.container;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class VInvoker implements InvocationHandler {
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        return method.invoke(proxy, args);
    }
}
