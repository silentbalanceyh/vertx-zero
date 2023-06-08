package io.vertx.up.boot.anima;

import io.horizon.eon.VValue;
import io.horizon.uca.log.Annal;
import io.macrocosm.specification.config.HConfig;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Infusion;
import io.vertx.up.eon.KPlugin;
import io.vertx.up.exception.boot.PluginSpecificationException;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * Infusion initialization
 */
public class InfixScatter implements Scatter<Vertx> {

    private static final Annal LOGGER = Annal.get(InfixScatter.class);

    /**
     * 扫描所有 Infusion，Infix 架构专用的接入类信息，Infix接入类基本规范
     * <pre><code>
     *     1. 此类必须是带有 {@link Infusion} 注解的类型
     *     2. 此类必须包含初始化静态方法（同步方法）
     * </code></pre>
     *
     * @param vertx  Vertx实例信息
     * @param config 基础配置信息
     */
    @Override
    @SuppressWarnings("all")
    public void connect(final Vertx vertx, final HConfig config) {
        final ConcurrentMap<String, Class<?>> wholeInjections = ZeroStore.injection();

        final ConcurrentMap<Class<? extends Annotation>, Class<?>> injections =
            Ut.elementZip(KPlugin.INFIX_MAP, wholeInjections);
        injections.values().forEach(item -> {
            if (null != item && item.isAnnotationPresent(Infusion.class)) {
                final Method method = findInit(item);
                Fn.outBoot(null == method, LOGGER,
                    PluginSpecificationException.class,
                    getClass(), item.getName());
                Fn.failAt(() -> method.invoke(null, vertx), LOGGER);
            }
        });
        /* Scan all extension Infusion **/
        Observable.fromIterable(wholeInjections.keySet())
            .filter(key -> !KPlugin.INJECT_STANDARD.contains(key))
            .map(wholeInjections::get)
            .filter(Objects::nonNull)
            .filter(item -> item.isAnnotationPresent(Infusion.class))
            .subscribe(item -> {
                final Method method = findInit(item);
                Fn.outBoot(null == method, LOGGER,
                    PluginSpecificationException.class,
                    getClass(), item.getName());
                Fn.failAt(() -> method.invoke(null, vertx), LOGGER);
            })
            .dispose();
        /* After infix inject plugins **/
        //        Ut.itSet(PLUGINS, (clazz, index) -> Runner.run(() -> {
        //            /* Instance reference **/
        //            final Object reference = Ut.singleton(clazz);
        //            /* Injects scanner **/
        //            PLUGIN.createInjection(reference);
        //        }, "injects-plugin-scannner"));
    }


    /**
     * Check whether clazz has the method of name
     */
    private Method findInit(final Class<?> clazz) {
        return Fn.runOr(() -> {
            final Method[] methods = clazz.getDeclaredMethods();
            final List<Method> found = Arrays.stream(methods).filter(item -> {
                    if (!"init".equals(item.getName())) {
                        // 直接 KO
                        return false;
                    }
                    if (!(void.class == item.getReturnType() || Void.class == item.getReturnType())) {
                        // 直接 KO
                        return false;
                    }
                    final int modifier = item.getModifiers();
                    return Modifier.isStatic(modifier) && Modifier.isPublic(modifier);
                })
                .toList();
            return VValue.ONE == found.size() ? found.get(VValue.IDX) : null;
        }, clazz);
    }
}
