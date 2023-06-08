package io.horizon.uca.aop;

import io.horizon.eon.em.EmAop;
import io.horizon.util.HUt;
import io.modello.eon.configure.VPC;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * 「切面分流器」
 * 根据既定规则对AOP部分的配置执行分流操作，分类规则参考如下配置：
 * <pre><code>
 *     {
 *         "plugin.fork": {
 *             "type": "FIELD",
 *             "robin": "",
 *             "config":{
 *                  "by": "identifier"
 *             },
 *             "<value1>": {
 *                  "plugin.component.before": [],
 *                  "plugin.component.job": [],
 *                  "plugin.component.after": []
 *             }
 *         },
 *         "plugin.config": {
 *
 *         }
 *     }
 * </code></pre>
 * 针对配置执行分流操作，若带有分流器，则分流器分为两种：
 * <pre><code>
 *     1. FIELD: 按字段分流
 *     2. COMPONENT: 按组件分类，实现 {@link io.horizon.specification.uca.HRobin} 接口
 * </code></pre>
 * 此处需要单独说明生命周期问题
 * <pre><code>
 *     1. Before：前置执行
 *     2. After：后置执行
 *     3. Around：环绕执行
 * </code></pre>
 * 不同的函数签名其含义会有所区别，且使用周期也会不同
 * <pre><code>
 *     完整生命周期包含两部分：配置周期和请求周期
 *     - 配置：在配置周期中由于没有包含请求所有内容，所有操作级别都是元数据级别，此时 forkKey 自然是无法获取的
 *     - 请求：在请求周期中会根据请求数据计算 forkKey，然后执行对应的操作
 *     简单说 children 模式只有在请求周期中会生效
 *     函数签名：
 *     - xxx(T): 读取所有配置组件
 *     - xxx(T, config): 配置周期 - 设置组件和配置
 *     - xxx(T, config, key): 请求周期 - 设置组件和配置
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AspectRobin {
    private final AspectComponent component;
    private final ConcurrentMap<String, AspectComponent> children = new ConcurrentHashMap<>();
    private final AspectConfig config = new AspectConfig();

    private final EmAop.Robin type;

    private AspectRobin(final JsonObject components) {
        // 防止 components 传入 null
        final JsonObject configuration = HUt.valueJObject(components);
        final JsonObject configJ = HUt.valueJObject(configuration, VPC.aop.PLUGIN_CONFIG);
        // 先判断是否包含分流器
        if (configuration.containsKey(VPC.aop.PLUGIN_FORK)) {
            // 包含分流器
            final String component = HUt.valueString(configuration, VPC.aop.plugin_fork.ROBIN);
            if (HUt.isNil(component)) {
                // FIELD 默认
                this.type = EmAop.Robin.FIELD;
            } else {
                // COMPONENT
                this.type = EmAop.Robin.COMPONENT;
            }
            final JsonObject iter = HUt.valueJObject(configuration, VPC.aop.PLUGIN_FORK, true);
            iter.remove(VPC.aop.plugin_fork.ROBIN);
            iter.remove(VPC.aop.plugin_fork.TYPE);

            final JsonObject forkJ = HUt.valueJObject(configuration, VPC.aop.PLUGIN_FORK, true);
            this.config.bind(HUt.elementSubset(forkJ,
                VPC.aop.plugin_fork.ROBIN,
                VPC.aop.plugin_fork.TYPE,
                VPC.aop.plugin_fork.CONFIG
            ));

            HUt.itJObject(iter, JsonObject.class).forEach(entry -> {
                final AspectComponent child = new AspectComponent(entry.getValue());
                this.config.bind(child, configJ);
                this.children.put(entry.getKey(), child);
            });
            this.component = null;  // Set Null
        } else {
            // 不包含分流器
            this.type = EmAop.Robin.NONE;
            // 直接构造
            this.component = new AspectComponent(components);
            this.config.bind(this.component, configJ);
        }
    }

    public static AspectRobin create(final JsonObject components) {
        return new AspectRobin(components);
    }

    public static AspectRobin create() {
        return new AspectRobin(new JsonObject());
    }

    public <T> List<Class<?>> beforeQueue(final T input) {
        return this.component(input, AspectComponent::nameBefore);
    }

    public <T> List<Class<?>> afterQueue(final T input) {
        return this.component(input, AspectComponent::nameAfter);
    }

    public <T> List<Class<?>> afterJob(final T input) {
        return this.component(input, AspectComponent::nameJob);
    }

    public JsonObject config(final Class<?> clazz) {
        return this.config.config(clazz);
    }

    public void beforeQueue(final Class<?> clazz, final JsonObject external, final String key) {
        this.add(clazz, external, AspectComponent::nameBefore, key);
    }

    public void beforeQueue(final Class<?> clazz, final JsonObject external) {
        this.add(clazz, external, AspectComponent::nameBefore, null);
    }

    public void afterQueue(final Class<?> clazz, final JsonObject external, final String key) {
        this.add(clazz, external, AspectComponent::nameAfter, key);
    }

    public void afterQueue(final Class<?> clazz, final JsonObject external) {
        this.add(clazz, external, AspectComponent::nameAfter, null);
    }

    public void afterJob(final Class<?> clazz, final JsonObject external, final String key) {
        this.add(clazz, external, AspectComponent::nameJob, key);
    }

    public void afterJob(final Class<?> clazz, final JsonObject external) {
        this.add(clazz, external, AspectComponent::nameJob, null);
    }

    private void add(final Class<?> clazz, final JsonObject external,
                     final Function<AspectComponent, List<Class<?>>> executor, final String forkKey) {
        final AspectComponent component;
        // 算法截断
        if (EmAop.Robin.NONE == this.type) {
            component = this.component;
        } else {
            component = this.seekComponent(forkKey);
        }
        // 追加配置
        final List<Class<?>> classes = executor.apply(component);
        if (!classes.contains(clazz)) {
            classes.add(clazz);
            // External Configuration for current Component
            this.config.config(clazz, external);
        }
    }

    private <T> List<Class<?>> component(final T input, final Function<AspectComponent, List<Class<?>>> executor) {
        // 算法截断
        if (EmAop.Robin.NONE == this.type) {
            return executor.apply(this.component);
        }
        // 深度检索
        final String forkKey = this.config.forkKey(input);
        final AspectComponent component = this.seekComponent(forkKey);
        return executor.apply(component);
    }

    private AspectComponent seekComponent(final String forkKey) {
        AspectComponent component;
        if (Objects.isNull(forkKey)) {
            component = this.component;
        } else {
            component = this.children.get(forkKey);
            if (Objects.isNull(component)) {
                component = this.component;
            }
        }
        return component;
    }
}
