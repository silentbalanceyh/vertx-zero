package cn.originx.cv;

/**
 * ## 平台常量
 *
 * ### 1. 基本介绍
 *
 * 该类为<strong>接口</strong>定义，之中的所有成员都是常量成员，默认修饰为`public static final`，即接口常量，主要定义了以下几种：
 *
 * - CMDB关系标识符以及CMDB中常用的关系相关字段名（以模型为主）。
 * - Aspect默认标准插件名信息。
 * - 环境配置数据。
 * - 除开上述几种以外，还包含了内部类`Field, Relation, Ambient`。
 *
 * > CMDB系统属于`Ox`平台的专用系统，也是它的正统App，虽然是数据驱动型，但固定字段也是配合CMDB的模型标准。
 *
 * ### 2. 内部类
 *
 * - {@link Field OxCv.Field类}
 * - {@link Relation OxCv.Relation类}
 * - {@link Ambient OxCv.Ambient类}
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface OxCv {
    /**
     * <value>rl.device.relation</value>，「CMDB」CMDB中关系模型的统一标识符，identifier字段，字段定义参考{@link Relation OxCv.Relation类}。
     */
    String RELATION_IDENTIFIER = "rl.device.relation";
    /**
     * <value>device.relation</value>，「CMDB」ServiceConfig中配置的options专用配置名信息。
     *
     * 它的基本配置如下：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *     "device.relation": "xxx"
     * }
     * // </code></pre>
     * ```
     */
    String RELATION_FIELD = "device.relation";
    /**
     * <value>globalId</value>，「CMDB」CMDB系统中和`UCMDB`对接的模型关联主键，对应`UCMDB`中的uuid用于标识配置项。
     */
    String RELATION_UID = "globalId";
    /**
     * <value>down</value>「CMDB」配置项模型中的<strong>下游</strong>关系字段名，为`JsonArray`数据结构。
     */
    String RELATION_DOWN = "down";
    /**
     * <value>up</value>，「CMDB」配置项模型中的<strong>上游</strong>关系字段名，为`JsonArray`数据结构。
     */
    String RELATION_UP = "up";


    /**
     * <value>plugin.activity</value>，「Plugin」ServiceConfig配置<strong>变更历史</strong>插件，对应`X_ACTIVITY`表。
     *
     * 配置如下：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *     "plugin.activity": "xxxx"
     * }
     * // </code></pre>
     * ```
     */
    String PLUGIN_ACTIVITY = "plugin.activity";
    /**
     * <value>plugin.todo</value>，「Plugin」ServiceConfig配置<strong>待办</strong>插件，对应`X_TODO`表。
     *
     * 配置如下：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *     "plugin.todo": "xxxx"
     * }
     * // </code></pre>
     * ```
     */
    String PLUGIN_TODO = "plugin.todo";
    /**
     * <value>plugin.ticket</value>，「Plugin」ServiceConfig配置<strong>待确认单</strong>插件，对应新表`B_TICKET`，和`X_TODO`做跨数据库Join。
     *
     * 配置如下：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *     "plugin.ticket": "xxxx"
     * }
     * // </code></pre>
     * ```
     */
    String PLUGIN_TICKET = "plugin.ticket";
    /**
     * <value>plugin.component</value>，「Plugin」ServiceConfig配置中<strong>标准</strong>插件，通道可配置的触发主插件。
     *
     * 配置如下：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *     "plugin.component": "cn.originx.scaffold.plugin.AspectRecord"
     * }
     * // </code></pre>
     * ```
     *
     * 该插件用于区分单记录还是多记录，可用插件如：
     *
     * - {@link cn.originx.scaffold.plugin.AspectRecord cn.originx.scaffold.plugin.AspectRecord}， 单记录处理，JsonObject类型
     * - {@link cn.originx.scaffold.plugin.AspectBatch cn.originx.scaffold.plugin.AspectBatch}，多记录处理，JsonArray类型
     */
    String PLUGIN_COMPONENT = "plugin.component";
    /**
     * <value>plugin.component.before</value>，「Plugin」ServiceConfig配置中的AOP<strong>前置</strong>插件链，JsonArray数据结构。
     *
     * 配置如下：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *      "plugin.component.before": [
     *          "cn.originx.uca.plugin.semi.BeforeNumber",
     *          "cn.originx.uca.plugin.semi.BeforeLife"
     *      ],
     * }
     * // </code></pre>
     * ```
     *
     * > 该插件链会对JsonObject和JsonArray执行Monad的链式操作：`Monad1 -> Monad2 -> Monad3`
     */
    String PLUGIN_COMPONENT_BEFORE = "plugin.component.before";
    /**
     * <value>plugin.component.after</value>，「Plugin」ServiceConfig配置中的AOP<strong>后置</strong>插件链，JsonArray数据结构。
     *
     *
     * 配置如下：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *      "plugin.component.after": [
     *          "cn.originx.uca.plugin.semi.AfterEs",
     *          "cn.originx.uca.plugin.semi.AfterNode",
     *          "cn.originx.itsm.plugin.AfterItsm"
     *      ],
     * }
     * // </code></pre>
     * ```
     *
     * > 该插件链会对JsonObject和JsonArray执行Monad的链式操作：`Monad1 -> Monad2 -> Monad3`
     */
    String PLUGIN_COMPONENT_AFTER = "plugin.component.after";
    /**
     * <value>plugin.component.job</value>，「Plugin」ServiceConfig配置中的AOP<strong>后置</strong>异步回调插件，在after之后，JsonArray数组结构。
     *
     * 配置如下：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *      "plugin.component.after": [
     *          "cn.originx.uca.plugin.semi.AfterEs",
     *          "cn.originx.uca.plugin.semi.AfterNode",
     *          "cn.originx.itsm.plugin.AfterItsm"
     *      ],
     * }
     * // </code></pre>
     * ```
     *
     * > 该插件链位于after之后，即在after之后开启异步Job流程，响应信息可先回客户端而实现数据本身的异步任务，以提高性能。
     */
    String PLUGIN_COMPONENT_JOB = "plugin.component.job";
    /**
     * <value>plugin.config</value>，「Plugin」和Before/After相关的组件静态配置，为JsonObject结构。
     *
     * 该组件配置为`name = Json`的核心结构，Json中存储了和组件相关的配置数据。
     *
     * 配置如下：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *      "plugin.config": {
     *          "cn.originx.uca.plugin.semi.BeforeNumber": {
     *              "field": "code"
     *          }
     *      }
     * }
     * // </code></pre>
     * ```
     *
     * 示例中配置了`BeforeNumber`插件所需的基础配置信息，可任意定义。
     */
    String PLUGIN_CONFIG = "plugin.config";
    /**
     * <value>plugin.identifier</value>，「Plugin」ServiceConfig配置中<strong>标识选择</strong>插件，实现接口`io.vertx.tp.optic.environment.Identifier`。
     */
    String PLUGIN_IDENTIFIER = "plugin.identifier";

    /**
     * <value>configuration.operation</value>，ServiceConfig中用于配置操作类型的专用常量。
     *
     * 操作类型目前的版本中有三种：
     *
     * - ADD：添加
     * - DELETE：删除
     * - UPDATE：更新
     */
    String CONFIGURATION_OPERATION = "configuration.operation";
    /**
     * <value>__VERTX_MASTER__</value>，图引擎中主图的代码信息，对应`cn.vertxup.graphic.domain.tables.pojos.GGraphic`中的`code`属性，`CODE`字段。
     *
     * CMDB中的拓扑图分三种：
     *
     * - 主图：master = true的图（一般一个CMDB系统只有一张主图）。
     * - 定义图：根据关系定义构造的拓扑图，`M_MODEL`和`M_RELATION`构造。
     * - 数据图：自由绘图，目前版本中根据`ci.device`和`rl.device.relation`两种模型构造的图。
     */
    String GRAPHIC_MASTER_DEFAULT = "__VERTX_MASTER__";

    /**
     * ## 「内部类」配置项字段常量
     *
     * ### 1. 基本介绍
     *
     * CMDB定义了目前系统中常用的核心字段（OOB模板数据配置型，静态数据规范）。
     *
     * ### 2. 核心点
     *
     * #### 2.1. lifecycle
     *
     * lifecycle为配置项生命周期变量，参考`cn.originx.cv.em.LifeCycle`枚举定义。
     *
     * |值|托管方|说明|
     * |---|---|:---|
     * |INNER|CMDB|CMDB内部流程（默认值）。|
     * |PENDING|第三方|等待上线的生命周期，未上线之前不可编辑和推送，数据可能不完整。|
     * |READY|第三方|可上线的生命周期，可执行第三方上线流程——数据准备完成。|
     * |ONLINE|第三方|已上线的生命周期（不可删除）。|
     * |OFFLINE|第三方|已下线的生命周期（不可编辑），下线过后的配置项不可在CMDB中编辑。|
     * |DELETED|第三方|已删除，在CMDB中已下线会执行物理删除和历史备份，但第三方系统会产生DELETED的标记。|
     *
     * #### 2.2. confirmStatus
     *
     * confirmStatus用来表示配置项是否执行变更中，参考`cn.originx.uca.workflow.Commutator`接口定义。
     *
     * - `unconfirmed`：未确认状态，正在执行变更，不可消费。
     * - `confirmed`：已确认状态，可消费。
     *
     * #### 2.3. 三级分类
     *
     * CMDB中的配置项每个都有三个字段，对应类别字典`X_CATEGORY`：
     *
     * - categoryFirst：一级分类字段名。
     * - categorySecond：二级分类字段名。
     * - categoryThird：三级分类字段名。
     *
     * @author <a href="http://www.origin-x.cn">Lang</a>
     */
    interface Field {

        /**
         * <value>lifecycle</value>，「CMDB」生命周期专用字段名。
         */
        String LIFE_CYCLE = "lifecycle";
        /**
         * <value>confirmStatus</value>，「CMDB」待确认状态专用字段名。
         */
        String CONFIRM_STATUS = "confirmStatus";
        /**
         * <value>categoryFirst</value>，「CMDB」一级分类字段名。
         */
        String CATEGORY_FIRST = "categoryFirst";
        /**
         * <value>categorySecond</value>，「CMDB」二级分类字段名。
         */
        String CATEGORY_SECOND = "categorySecond";
        /**
         * <value>categoryThird</value>，「CMDB」三级分类字段名。
         */
        String CATEGORY_THIRD = "categoryThird";

        String DIM_1 = "dim1";
        String DIM_2 = "dim2";
        String DIM_3 = "dim3";

        String DIFF_KEY = "diff.key";
    }

    /**
     * ## 「内部类」关系字段常量
     *
     * ### 1. 基本介绍
     *
     * CMDB中的关系模型专用字段常量，主要分：
     *
     * - 关系源，上游，`SOURCE_`前缀。
     * - 目标源，下游，`TARGET_`前缀。
     *
     * ### 2. 维度定义
     *
     * 该类中针对上下游字段信息，定义了以下几个维度数据：
     *
     * - GlobalId：第三方集成专用标识字段，对应配置项中的`globalId`字段。
     * - Category：配置项的分类信息（叶级分类），优先使用`X_CATEGORY`中的值，其次直接使用第三方的类型定义值。
     * - Identifier：配置项的统一模型标识符，identifier字段。
     * - Code：配置项编号。
     * - Name：配置项名称。
     *
     * @author <a href="http://www.origin-x.cn">Lang</a>
     */
    interface Relation {
        /**
         * <value>sourceGlobalId</value>，「CMDB」上游配置项主键，通常是第三方主键，本版本中是UCMDB中主键。
         */
        String SOURCE_JOINED = "sourceGlobalId";
        /**
         * <value>sourceCategory</value>，「CMDB」上游配置项类型。
         */
        String SOURCE_CATEGORY = "sourceCategory";
        /**
         * <value>sourceIdentifier</value>，「CMDB」上游配置项模型标识符identifier。
         */
        String SOURCE_IDENTIFIER = "sourceIdentifier";
        /**
         * <value>sourceCode</value>，「CMDB」上游配置项编号。
         */
        String SOURCE_CODE = "sourceCode";
        /**
         * <value>sourceName</value>，「CMDB」上游配置项名称。
         */
        String SOURCE_NAME = "sourceName";
        /**
         * <value>source</value>，用于描述上游五个维度的前缀信息，方便程序执行上游属性提取。
         *
         * 示例使用代码：
         *
         * ```java
         * // <pre><code class="java">
         *     final Object sourceValue = relation.getEnd1CI().getPropertyValue(attrName);
         *     if (Objects.nonNull(sourceValue)) {
         *          data.put(OxCv.Relation.SOURCE_PREFIX + suffix, sourceValue);
         *     }
         * // </code></pre>
         * ```
         */
        String SOURCE_PREFIX = "source";

        /**
         * <value>targetGlobalId</value>，「CMDB」下游配置项主键，通常是第三方主键，本版本中是UCMDB中主键。
         */
        String TARGET_JOINED = "targetGlobalId";
        /**
         * <value>targetCategory</value>，「CMDB」下游配置项类型。
         */
        String TARGET_CATEGORY = "targetCategory";
        /**
         * <value>targetIdentifier</value>，「CMDB」下游配置项模型标识符identifier。
         */
        String TARGET_IDENTIFIER = "targetIdentifier";
        /**
         * <value>targetCode</value>，「CMDB」下游配置项编号。
         */
        String TARGET_CODE = "targetCode";
        /**
         * <value>targetName</value>，「CMDB」下游配置项名称。
         */
        String TARGET_NAME = "targetName";
        /**
         * <value>target</value>，用于描述下游五个维度的前缀信息，方便程序执行下游属性提取。
         *
         * 示例使用代码：
         *
         * ```java
         * // <pre><code class="java">
         *     final Object targetValue = relation.getEnd2CI().getPropertyValue(attrName);
         *     if (Objects.nonNull(targetValue)) {
         *          data.put(OxCv.Relation.TARGET_PREFIX + suffix, targetValue);
         *     }
         * // </code></pre>
         * ```
         */
        String TARGET_PREFIX = "target";
    }

    /**
     * ## 「内部类」环境目录常量
     *
     * ### 1. 基本介绍
     *
     * CMDB中定义的环境基础配置数据：
     *
     * - CMDB配置目录：`external`目录。
     * - CMDB配置文件：`configuration.json`文件。
     * - Shell环境专用配置。
     *
     * > 上述配置根目录从启动项目`ox-driver/ix-atlantic`的`src/main/resources`开始计算（标准Maven）。
     *
     * @author <a href="http://www.origin-x.cn">Lang</a>
     */
    interface Ambient {
        /**
         * <value>runtime/external</value>，「CMDB」专用环境配置（外联）。
         **/
        String CONFIG_EXTERNAL = "runtime/external/";
        /**
         * <value>runtime/configuration.json</value>，「CMDB」运行时环境配置（专用）。
         */
        String CONFIG_FILE = "runtime/configuration.json";
    }
}
