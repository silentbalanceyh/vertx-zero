package io.vertx.up.boot.di;

import com.google.inject.Injector;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.up.atom.container.VInstance;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.util.Ut;
import jakarta.inject.Named;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

@SuppressWarnings("all")
public class DiPlugin {

    private static final Cc<Class<?>, DiPlugin> CC_DI = Cc.open();
    private transient final Class<?> clazz;
    private transient final DiInfix infix;
    private transient final Annal logger;

    private DiPlugin(final Class<?> clazz) {
        this.clazz = clazz;
        this.logger = Annal.get(clazz);
        this.infix = new DiInfix(clazz);
    }

    public static DiPlugin create(final Class<?> clazz) {
        return CC_DI.pick(() -> new DiPlugin(clazz), clazz);
        // return Fn.po?l(Pool.PLUGINS, clazz, () -> new DiPlugin(clazz));
    }

    // 直接创建一个新类
    public <T> T createInstance(final Class<?> clazz) {
        return (T) createProxy(clazz, null);
    }

    // 直接创建一个单例
    public <T> T createSingleton(final Class<?> clazz) {
        final Injector di = ZeroAnno.getDi();
        /*
         * Add @Named Support
         */
        String extensionKey = this.named(clazz);
        return Ut.singleton(clazz,
            () -> (T) this.infix.wrapInfix(di.getInstance(clazz)),
            extensionKey);
    }

    public String named(final Class<?> clazz) {
        String name;
        if (clazz.isAnnotationPresent(Named.class)) {
            final Annotation annotation = clazz.getAnnotation(Named.class);
            name = Ut.invoke(annotation, "value");
        } else {
            name = null;
        }
        return name;
    }

    // 创建一个新的
    public Object createProxy(final Class<?> clazz, final Method action) {
        final Injector di = ZeroAnno.getDi();
        final Object instance;
        if (clazz.isInterface()) {
            final Class<?> implClass = Ut.child(clazz);
            if (null != implClass) {
                // Interface + Impl
                instance = di.getInstance(clazz); // Ut.singleton(implClass);
            } else {
                /*
                 * SPEC5: Interface only, direct api, in this situation,
                 * The proxy is null and the agent do nothing. The request will
                 * send to event bus direct. It's not needed to set
                 * implementation class.
                 */
                instance = VInstance.create(clazz);
            }
        } else {
            if (Objects.isNull(action)) {
                instance = di.getInstance(clazz);
            } else {
                instance = di.getInstance(action.getDeclaringClass());
            }
        }
        return this.infix.wrapInfix(instance);
    }
}
