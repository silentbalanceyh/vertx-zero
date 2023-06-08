package io.vertx.up.atom.container;

import io.horizon.uca.cache.Cc;

import java.lang.reflect.Proxy;

public class VInstance {

    private static final Cc<Class<?>, VInstance> CC_V_INSTANCE = Cc.open();
    private transient final Class<?> interfaceCls;

    private VInstance(final Class<?> interfaceCls) {
        this.interfaceCls = interfaceCls;
    }

    public static VInstance create(final Class<?> interfaceCls) {
        return CC_V_INSTANCE.pick(() -> new VInstance(interfaceCls), interfaceCls);
        // return Fn.po?l(V_POOL, interfaceCls, () -> new VInstance(interfaceCls));
    }

    @SuppressWarnings("unchecked")
    public <T> T proxy() {
        final Class<?>[] interfaces = new Class<?>[]{this.interfaceCls};
        return (T) Proxy.newProxyInstance(this.interfaceCls.getClassLoader(), interfaces, new VInvoker());
    }
}
