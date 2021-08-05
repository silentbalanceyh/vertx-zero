package io.vertx.up.uca.container;

import io.vertx.up.fn.Fn;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class VInstance {
    private static final ConcurrentMap<Class<?>, VInstance> V_POOL =
            new ConcurrentHashMap<>();
    private transient final Class<?> interfaceCls;

    private VInstance(final Class<?> interfaceCls) {
        this.interfaceCls = interfaceCls;
    }

    public static VInstance create(final Class<?> interfaceCls) {
        return Fn.pool(V_POOL, interfaceCls, () -> new VInstance(interfaceCls));
    }

    @SuppressWarnings("unchecked")
    public <T> T proxy() {
        final Class<?>[] interfaces = new Class<?>[]{this.interfaceCls};
        return (T) Proxy.newProxyInstance(this.interfaceCls.getClassLoader(), interfaces, new VInvoker());
    }
}
