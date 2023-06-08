package io.horizon.uca.aop;

import io.horizon.exception.web._501NotSupportException;
import io.horizon.specification.uca.HRobin;
import io.horizon.util.HUt;
import io.modello.eon.configure.VPC;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang : 2023-06-03
 */
class AspectConfig {
    private final JsonObject forkJ = new JsonObject();
    private final ConcurrentMap<Class<?>, JsonObject> configMap = new ConcurrentHashMap<>();

    JsonObject config(final Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return this.configMap.getOrDefault(clazz, new JsonObject());
    }

    void config(final Class<?> clazz, final JsonObject config) {
        Objects.requireNonNull(clazz);
        this.configMap.put(clazz, HUt.isNil(config) ? new JsonObject() : config);
    }

    void bind(final AspectComponent component, final JsonObject configJ) {
        this.bind(component.nameBefore(), configJ);
        this.bind(component.nameAfter(), configJ);
        this.bind(component.nameJob(), configJ);
    }

    void bind(final JsonObject forkJ) {
        this.forkJ.mergeIn(forkJ, true);
    }

    /**
     * 值节点选择器，主要针对
     * <pre><code>
     *     {
     *         "type": "FIELD",
     *         "robin": "",
     *         "config": {
     *             "by": "identifier"
     *         }
     *     }
     * </code></pre>
     *
     * @param input 输入参数（JsonObject、JsonArray、T）
     * @param <T>   输入类型
     *
     * @return 选择计算的值
     */
    <T> String forkKey(final T input) {
        final Class<?> clazz = HUt.valueC(this.forkJ, VPC.aop.plugin_fork.ROBIN);
        final JsonObject config = HUt.valueJObject(this.forkJ, VPC.aop.plugin_fork.CONFIG);
        if (Objects.nonNull(clazz)) {
            // 配置了 robin
            final HRobin<T> robin = HUt.singleton(clazz);
            return robin.execute(input, config);
        } else {
            // 未配置组件，暂时只考虑 type = FIELD 的情况
            final String by = HUt.valueString(config, VPC.aop.plugin_fork.config.BY);
            Objects.requireNonNull(by);
            if (input instanceof JsonObject) {
                final JsonObject inputJ = (JsonObject) input;
                return HUt.valueString(inputJ, by);
            } else if (input instanceof JsonArray) {
                final JsonArray inputA = (JsonArray) input;
                return HUt.valueString(inputA, by);
            } else {
                throw new _501NotSupportException(this.getClass());
            }
        }
    }

    private void bind(final Collection<Class<?>> clazzSet, final JsonObject configJ) {
        clazzSet.forEach(clazz -> {
            if (configJ.containsKey(clazz.getName())) {
                final JsonObject componentConfig = HUt.valueJObject(configJ, clazz.getName());
                this.config(clazz, componentConfig);
            }
        });
    }
}
