package io.mature.extension.scaffold.plugin;

import io.horizon.spi.plugin.AfterPlugin;
import io.horizon.spi.plugin.BeforePlugin;
import io.horizon.spi.plugin.DataPlugin;
import io.mature.extension.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.up.atom.exchange.DFabric;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.function.Function;

/*
 * 插件管理器
 * - plugin.component.before
 * []
 * - plugin.component.after
 * []
 */
@SuppressWarnings("unchecked")
public class PluginQueue {
    private final transient DataAtom atom;
    private transient DFabric fabric;

    PluginQueue(final DataAtom atom) {
        this.atom = atom;
    }

    void bind(final DFabric fabric) {
        this.fabric = fabric;
    }

    List<Function<JsonObject, Future<JsonObject>>> beforePlugin(final JsonObject options) {
        return this.before(options);
    }

    List<Function<JsonArray, Future<JsonArray>>> beforePlugins(final JsonObject options) {
        return this.before(options);
    }

    List<Function<JsonObject, Future<JsonObject>>> afterPlugin(final JsonObject options) {
        return this.after(Ox.pluginAfter(options), options);
    }

    List<Function<JsonArray, Future<JsonArray>>> afterPlugins(final JsonObject options) {
        return this.after(Ox.pluginAfter(options), options);
    }

    Set<Function<JsonObject, Future<JsonObject>>> jobPlugin(final JsonObject options) {
        return new HashSet<>(this.after(Ox.pluginJob(options), options));
    }

    Set<Function<JsonArray, Future<JsonArray>>> jobPlugins(final JsonObject options) {
        return new HashSet<>(this.after(Ox.pluginJob(options), options));
    }

    // ========================= Infusion Initialize ============================
    private <T extends DataPlugin<T>> T mapBind(final Class<?> pluginCls) {
        final T plugin = Ut.instance(pluginCls);
        /*
         * 绑定模型
         */
        if (Objects.nonNull(plugin)) {
            plugin.bind(this.atom);
        }
        /*
         * 「字典翻译器的BUG」插件队列在传入字典翻译器给某个插件时，也需要创建拷贝
         */
        if (Objects.nonNull(this.fabric)) {
            plugin.bind(this.fabric.copy());
        }
        return (T) plugin;
    }

    private <P extends DataPlugin<P>, T> Future<T> runPlugin(final P plugin, final T input, final JsonObject options) {
        final JsonObject optionData = Ox.pluginOptions(plugin.getClass(), options);
        if (plugin instanceof BeforePlugin) {
            // Run Before
            final BeforePlugin runner = (BeforePlugin) plugin;
            if (input instanceof JsonObject) {
                return runner.beforeAsync((JsonObject) input, optionData)
                    .compose(json -> Ux.future((T) json));
            } else {
                return runner.beforeAsync((JsonArray) input, optionData)
                    .compose(json -> Ux.future((T) json));
            }
        } else {
            // Run After
            final AfterPlugin runner = (AfterPlugin) plugin;
            if (input instanceof JsonObject) {
                return runner.afterAsync((JsonObject) input, optionData)
                    .compose(json -> Ux.future((T) json));
            } else {
                return runner.afterAsync((JsonArray) input, optionData)
                    .compose(json -> Ux.future((T) json));
            }
        }
    }

    // ========================= Infusion After/Before ============================
    private <T> List<Function<T, Future<T>>> before(final JsonObject options) {
        final List<Class<?>> pluginCls = Ox.pluginBefore(options);
        final List<Function<T, Future<T>>> executors = new ArrayList<>();
        pluginCls.stream()
            .map(this::mapBind).map(plugin -> (BeforePlugin) plugin)
            .forEach(plugin -> executors.add(previous -> this.runPlugin(plugin, previous, options)));
        return executors;
    }

    private <T> List<Function<T, Future<T>>> after(final List<Class<?>> pluginCls, final JsonObject options) {
        final List<Function<T, Future<T>>> executors = new ArrayList<>();
        pluginCls.stream()
            .map(this::mapBind).map(plugin -> (AfterPlugin) plugin)
            .forEach(plugin -> executors.add(previous -> this.runPlugin(plugin, previous, options)));
        return executors;
    }
}
