package io.vertx.up.extension.dot;

import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/*
 * Default package scope tool for extension.
 */
class Plugin {
    private static final JsonObject PLUGIN_CONFIG = new JsonObject();
    private static final Annal LOGGER = Annal.get(Plugin.class);

    /*
     * I/O read for config loading.
     */
    static {
        if (ZeroStore.is(YmlCore.extension.__KEY)) {
            final JsonObject pluginConfig = ZeroStore.option(YmlCore.extension.__KEY);
            if (Objects.nonNull(pluginConfig)) {
                PLUGIN_CONFIG.mergeIn(pluginConfig);
            }
        }
    }

    @SuppressWarnings("all")
    static void mountPlugin(final String key, final BiConsumer<Class<?>, JsonObject> consumer) {
        mountPlugin(key, consumer);
    }

    static <T> T mountPlugin(final String key, final BiFunction<Class<?>, JsonObject, T> function,
                             final Supplier<T> supplier) {
        if (PLUGIN_CONFIG.containsKey(key)) {
            final JsonObject metadata = PLUGIN_CONFIG.getJsonObject(key);
            final Class<?> pluginCls = Ut.clazz(metadata.getString(YmlCore.extension.COMPONENT));
            if (Objects.nonNull(pluginCls)) {
                final JsonObject config = metadata.getJsonObject(YmlCore.extension.CONFIG);
                try {
                    return function.apply(pluginCls, config);
                } catch (final Throwable ex) {
                    ex.printStackTrace();
                    LOGGER.warn("Infusion Extension Failure: {0}, class = {1}", ex.getMessage(), pluginCls);
                    return supplier.get();
                }
            }
        }
        return supplier.get();
    }

    static Future<Envelop> mountPlugin(
        final String key,
        /* No plugin, returned original Future<Envelop> */
        final Envelop envelop,
        /* Internal function for generation of Envelop */
        final BiFunction<Class<?>, JsonObject, Future<Envelop>> function) {
        return mountPlugin(key, function, () -> Ux.future(envelop));
    }
}
