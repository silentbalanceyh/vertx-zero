package io.modello.eon;

import io.modello.eon.configure.VPC;

/**
 * @author lang : 2023-06-03
 */
interface I_SERVICE {
    /**
     * I_SERVICE / SERVICE_CONFIG 字段
     */
    interface SERVICE_CONFIG extends VPC.aop {
        /**
         * 「动态建模专用」
         * <value>configuration.operation</value>, ServiceConfig中用于配置操作类型的专用常量
         * 操作类型对应到 {@link io.horizon.eon.em.typed.ChangeFlag}
         * <pre><code>
         *     - ADD: 添加
         *     - UPDATE: 更新
         *     - DELETE：删除
         * </code></pre>
         */
        String CONFIGURATION_OPERATION = "configuration.operation";

        /**
         * 「动态建模专用」
         * <value>plugin.plugin</value>，ServiceConfig中配置的标识选择插件，实现接口
         * {@link io.horizon.spi.modeler.Identifier}
         */
        String PLUGIN_IDENTIFIER = "plugin.identifier";
        /**
         * 「动态建模专用」
         * <value>plugin.component</value>, ServiceConfig中标准插件，通道可配置的触发主插件
         * 配置如下
         * <pre><code>
         *     {
         *         "plugin.component": "io.extension.plugin.extension.AspectRecord"
         *     }
         * </code></pre>
         * 插件本身可区分单记录和多记录
         * <pre><code>
         *     1. {@see io.extension.extension.plugin.AspectRecord} 单记录处理器
         *     2. {@see io.extension.plugin.extension.AspectBatch} 多记录处理器
         * </code></pre>
         */
        String PLUGIN_COMPONENT = "plugin.component";
        /**
         * 「动态建模专用」
         * <value>plugin.activity</value>, ServiceConfig中配置变更历史插件，对应 X_ACTIVITY 表
         * 配置如下
         * <pre><code>
         *     {
         *         "plugin.activity": ""
         *     }
         * </code></pre>
         */
        String PLUGIN_ACTIVITY = "plugin.activity";
        /**
         * 「动态建模专用」
         * <value>*</value>，ServiceConfig中配置待办插件，对应 `X_TODO` 表，由于TODO是标签所以此处不写todo部分
         * 配置如下
         * <pre><code>
         *     {
         *         "plugin.todo": ""
         *     }
         * </code></pre>
         */
        String PLUGIN_TODO = "plugin.todo";
        /**
         * 「动态建模专用」
         * <value>plugin.ticket</value>，ServiceConfig中配置待确认单插件，对应新表 `B_TICKET` 和 `X_TODO` 实现跨数据库Join
         * 以及二次更新流程
         * <pre><code>
         *     {
         *         "plugin.ticket": ""
         *     }
         * </code></pre>
         */
        String PLUGIN_TICKET = "plugin.ticket";
    }
}
