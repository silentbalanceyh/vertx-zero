package io.vertx.up.uca.container;

import java.lang.reflect.Proxy;

public class VInstance {

    private static VInstance INSTANCE = null;
    private transient final Class<?> interfaceCls;

    private VInstance(final Class<?> interfaceCls) {
        this.interfaceCls = interfaceCls;
    }

    public static VInstance create(final Class<?> interfaceCls) {
        if (null == INSTANCE) {
            synchronized (VInstance.class) {
                if (null == INSTANCE) {
                    INSTANCE = new VInstance(interfaceCls);
                }
            }
        }
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public <T> T proxy() {
        final Class<?>[] interfaces = new Class<?>[]{this.interfaceCls};
        return (T) Proxy.newProxyInstance(this.interfaceCls.getClassLoader(), interfaces, new VInvoker());
    }
}
