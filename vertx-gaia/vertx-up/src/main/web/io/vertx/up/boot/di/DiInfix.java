package io.vertx.up.boot.di;

import io.horizon.eon.VMessage;
import io.horizon.uca.log.Annal;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.up.annotations.Infusion;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.Infix;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang : 2023-06-02
 */
class DiInfix {

    private static final Set<Class<?>> TPS = ZeroAnno.getTps();
    private static final ConcurrentMap<Class<?>, Class<?>> INFUSION = infusionMap();
    private transient final Annal logger;

    DiInfix(final Class<?> clazz) {
        this.logger = Annal.get(clazz);
    }

    private static ConcurrentMap<Class<?>, Class<?>> infusionMap() {
        // Extract all infixes
        final Set<Class<?>> infixes = new HashSet<>(ZeroStore.injection().values());
        final ConcurrentMap<Class<?>, Class<?>> binds = new ConcurrentHashMap<>();
        Observable.fromIterable(infixes)
            .filter(Infix.class::isAssignableFrom)
            .subscribe(item -> {
                final Method method = Fn.failOr(() -> item.getDeclaredMethod("get"), item);
                final Class<?> type = method.getReturnType();
                binds.put(type, item);
            })
            .dispose();
        return binds;
    }

    Object wrapInfix(final Object proxy) {
        if (Objects.isNull(proxy)) {
            return null;
        }
        final Class<?> typeOf = proxy.getClass();
        if (!TPS.contains(typeOf)) {
            return proxy;
        }
        final Class<?> type = proxy.getClass();
        Observable.fromArray(type.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Infusion.class))
            .subscribe(field -> {
                final Class<?> fieldType = field.getType();
                final Class<?> infixCls = INFUSION.get(fieldType);
                if (null != infixCls) {
                    if (Ut.isImplement(infixCls, Infix.class)) {
                        final Infix reference = Ut.singleton(infixCls);
                        final Object tpRef = Ut.invoke(reference, "get");
                        final String fieldName = field.getName();
                        Ut.field(proxy, fieldName, tpRef);
                    } else {
                        this.logger.warn(VMessage.DiPlugin.IMPL_WRONG, infixCls.getName(), io.vertx.up.plugin.Infix.class.getName());
                    }
                } else {
                    this.logger.warn(VMessage.DiPlugin.IMPL_NULL, field.getType().getName(), field.getName(), type.getName());
                }
            })
            .dispose();
        return proxy;
    }
}
