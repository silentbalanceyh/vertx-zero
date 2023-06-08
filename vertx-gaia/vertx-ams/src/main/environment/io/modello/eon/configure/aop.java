package io.modello.eon.configure;

import io.horizon.eon.VName;
import io.horizon.uca.aop.AspectRobin;
import io.vertx.core.json.JsonArray;

interface _HPCAopComponent {
    /**
     * <value>plugin.component.before</value>，AOP配置中的前置插件，数据类型为 {@link io.vertx.core.json.JsonArray}
     * 配置如下：
     * <pre><code>
     *     {
     *         "plugin.component.before": [
     *             "xxx.xxx.xxx.BeforeComponent1",
     *             "xxx.xxx.xxx.BeforeComponent2"
     *         ]
     *     }
     * </code></pre>
     * 插件执行流程会对 {@link io.vertx.core.json.JsonObject} 和 {@link io.vertx.core.json.JsonArray} 两种类型执行链式操作，串行执行
     * <pre><code>
     *     Monad1 -> Monad2 -> Monad3
     *     配置点有两部分：
     *     1. 动态建模 {@see VDBC.I_SERVICE.SERVICE_CONFIG} 中配置
     *     2. 全局处理 {@link AspectRobin} 中处理
     * </code></pre>
     */
    String PLUGIN_COMPONENT_BEFORE = "plugin.component.before";
    /**
     * <value>plugin.component.after</value>，AOP配置中的后置插件，数据类型为 {@link io.vertx.core.json.JsonArray}
     * 配置如下：
     * <pre><code>
     *     {
     *         "plugin.component.after": [
     *             "xxx.xxx.xxx.AfterComponent1",
     *             "xxx.xxx.xxx.AfterComponent2"
     *         ]
     *     }
     * </code></pre>
     * 插件执行流程会对 {@link io.vertx.core.json.JsonObject} 和 {@link io.vertx.core.json.JsonArray} 两种类型执行链式操作,
     * <pre><code>
     *      Monad1 -> Monad2 -> Monad3
     *      配置点有两部分：
     *      1. 动态建模 {@see VDBC.I_SERVICE.SERVICE_CONFIG} 中配置
     *      2. 全局处理 {@link AspectRobin} 中处理
     * </code></pre>
     */
    String PLUGIN_COMPONENT_AFTER = "plugin.component.after";
    /**
     * <value>plugin.component.job</value>, AOP配置中的后置异步回调插件，在 after 之后，{@link io.vertx.core.json.JsonArray}
     * 配置如下：
     * <pre><code>
     *     {
     *         "plugin.component.job": [
     *             "xxx.xxx.xxx.JobComponent1",
     *             "xxx.xxx.xxx.JobComponent2"
     *         ]
     *     }
     * </code></pre>
     */
    String PLUGIN_COMPONENT_JOB = "plugin.component.job";
}

/**
 * @author lang : 2023-06-03
 */
interface HPCAop extends _HPCAopComponent {
    /**
     * <value>plugin.fork</value>，新追加分流器配置，针对请求数据执行分流规则，让抽象表可支持多分流模式的AOP基本规则，AOP实现模式：
     * <pre><code>
     *     1. 不带分流器规则
     *     2. 带分流器规则：
     *        1）简单规则：按照单字段数据执行分流
     *        2）复杂规则：提供
     * </code></pre>
     */
    String PLUGIN_FORK = "plugin.fork";
    /**
     * <value>plugin.config</value>, 和 Before/After 相关的组件静态配置，数据类型为 {@link io.vertx.core.json.JsonObject}
     *
     * 该组件配置为`name = Json`的核心结构，Json中存储了和组件相关的配置数据，配置如下：
     *
     * <pre><code>
     *     {
     *         "plugin.config": {
     *             "component1": {
     *
     *             },
     *             "component2": {
     *
     *             }
     *         }
     *     }
     * </code></pre>
     *
     * 上述片段中 component1 和 component2 是组件名称（Java类全名）
     */
    String PLUGIN_CONFIG = "plugin.config";

    interface plugin_fork {
        /**
         * 分流器类型，对应 {@link io.horizon.eon.em.EmAop.Robin}
         */
        String TYPE = VName.TYPE;

        String ROBIN = VName.ROBIN;

        String CONFIG = VName.CONFIG;

        /**
         * 分流之后会形成类似如下结构（不同的值使用的组件不一样）
         * <pre><code>
         *      "value1" = {
         *          "plugin.component.before": [],
         *          "plugin.component.job": [],
         *          "plugin.component.after": []
         *      }
         * </code></pre>
         * 分流限制：
         * <pre><code>
         *     1. 每种组件的配置一致
         *     2. 最好书写不同的组件做分流，而在内部逻辑中去处理共享部分
         *     3. 分流依赖输入数据，现阶段不做批量分流，批量分流之后的结果依然是一个 String 的唯一 key，
         *        简单讲就是调用 {@link io.horizon.util.HUt#valueSetString} 方法从 {@link JsonArray} 中提取唯一键值
         * </code></pre>
         */
        interface __ extends _HPCAopComponent {
        }

        interface config {
            String BY = VName.BY;
        }
    }
}
