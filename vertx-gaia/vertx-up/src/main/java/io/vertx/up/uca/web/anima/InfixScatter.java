package io.vertx.up.uca.web.anima;

import io.reactivex.Observable;
import io.vertx.core.Vertx;
import io.vertx.tp.error.PluginSpecificationException;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.eon.KPlugin;
import io.vertx.up.eon.bridge.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.Runner;
import io.vertx.up.runtime.ZeroAmbient;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.uca.di.DiPlugin;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Infix initialization
 */
public class InfixScatter implements Scatter<Vertx> {

    private static final Annal LOGGER = Annal.get(InfixScatter.class);

    private static final Set<Class<?>> PLUGINS = ZeroAnno.getTps();

    private static final DiPlugin PLUGIN = DiPlugin.create(InfixScatter.class);

    @Override
    @SuppressWarnings("all")
    public void connect(final Vertx vertx) {
        final ConcurrentMap<String, Class<?>> wholeInjections = ZeroAmbient.getInjections();
        /* Enabled **/
        // final ConcurrentMap<String, Class<?>> enabled = Ut.reduce(LIME.read().keySet(), wholeInjections);
        /* Scan all Infix **/
        final ConcurrentMap<Class<? extends Annotation>, Class<?>> injections =
            Ut.reduce(KPlugin.INFIX_MAP, wholeInjections);
        injections.values().forEach(item -> {
            if (null != item && item.isAnnotationPresent(Plugin.class)) {
                final Method method = findInit(item);
                Fn.outUp(null == method, LOGGER,
                    PluginSpecificationException.class,
                    getClass(), item.getName());
                Fn.safeJvm(() -> method.invoke(null, vertx), LOGGER);
            }
        });
        /* Scan all extension Infix **/
        Observable.fromIterable(wholeInjections.keySet())
            .filter(key -> !KPlugin.INJECT_STANDARD.contains(key))
            .map(wholeInjections::get)
            .filter(Objects::nonNull)
            .filter(item -> item.isAnnotationPresent(Plugin.class))
            .subscribe(item -> {
                final Method method = findInit(item);
                Fn.outUp(null == method, LOGGER,
                    PluginSpecificationException.class,
                    getClass(), item.getName());
                Fn.safeJvm(() -> method.invoke(null, vertx), LOGGER);
            })
            .dispose();
        /* After infix inject plugins **/
        Ut.itSet(PLUGINS, (clazz, index) -> Runner.run(() -> {
            /* Instance reference **/
            final Object reference = Ut.singleton(clazz);
            /* Injects scanner **/
            PLUGIN.createInjection(reference);
        }, "injects-plugin-scannner"));
    }


    /**
     * Check whether clazz has the method of name
     */
    private Method findInit(final Class<?> clazz) {
        return Fn.orNull(() -> {
            final Method[] methods = clazz.getDeclaredMethods();
            final List<Method> found = Arrays.stream(methods)
                .filter(item -> "init".equals(item.getName()) && this.validMethod(item))
                .collect(Collectors.toList());
            return Values.ONE == found.size() ? found.get(Values.IDX) : null;
        }, clazz);
    }

    private boolean validMethod(final Method method) {
        return (void.class == method.getReturnType() || Void.class == method.getReturnType())
            && Modifier.isStatic(method.getModifiers())
            && Modifier.isPublic(method.getModifiers());
    }
}
