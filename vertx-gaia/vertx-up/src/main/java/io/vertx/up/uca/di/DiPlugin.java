package io.vertx.up.uca.di;

import com.google.inject.Injector;
import io.reactivex.Observable;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.eon.Info;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.plugin.Infix;
import io.vertx.up.runtime.ZeroAmbient;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("all")
public class DiPlugin {

    private static final Cc<Class<?>, DiPlugin> CC_DI = Cc.open();
    private transient final Class<?> clazz;
    private transient final Annal logger;

    private DiPlugin(final Class<?> clazz) {
        this.clazz = clazz;
        logger = Annal.get(clazz);
    }

    public static DiPlugin create(final Class<?> clazz) {
        return CC_DI.pick(() -> new DiPlugin(clazz), clazz);
        // return Fn.po?l(Pool.PLUGINS, clazz, () -> new DiPlugin(clazz));
    }

    public <T> T createComponent(final Class<?> clazz) {
        final Injector di = ZeroAnno.getDi();
        return Ut.singleton(clazz, () -> (T) di.getInstance(clazz));
    }

    public void createInjection(final Object proxy) {
        final ConcurrentMap<Class<?>, Class<?>> binds = getBind();
        final Class<?> type = proxy.getClass();
        Observable.fromArray(type.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Plugin.class))
            .subscribe(field -> {
                final Class<?> fieldType = field.getType();
                final Class<?> infixCls = binds.get(fieldType);
                if (null != infixCls) {
                    if (Ut.isImplement(infixCls, Infix.class)) {
                        final Infix reference = Ut.singleton(infixCls);
                        final Object tpRef = Ut.invoke(reference, "get");
                        final String fieldName = field.getName();
                        Ut.field(proxy, fieldName, tpRef);
                    } else {
                        logger.warn(Info.INFIX_IMPL, infixCls.getName(), Infix.class.getName());
                    }
                } else {
                    logger.warn(Info.INFIX_NULL, field.getType().getName(), field.getName(), type.getName());
                }
            })
            .dispose();
    }

    private ConcurrentMap<Class<?>, Class<?>> getBind() {
        // Extract all infixes
        final Set<Class<?>> infixes = new HashSet<>(ZeroAmbient.getInjections().values());
        final ConcurrentMap<Class<?>, Class<?>> binds = new ConcurrentHashMap<>();
        Observable.fromIterable(infixes)
            .filter(Infix.class::isAssignableFrom)
            .subscribe(item -> {
                final Method method = Fn.getJvm(() -> item.getDeclaredMethod("get"), item);
                final Class<?> type = method.getReturnType();
                binds.put(type, item);
            })
            .dispose();
        return binds;
    }
}
