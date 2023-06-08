package io.modello.specification.atom;

import io.horizon.specification.typed.TJson;

import java.io.Serializable;

/**
 * 「模型所属应用」
 * <hr/>
 * 针对模型的应用级专用接口，负责管理模型生命周期，比较核心的几点直接参考本接口方法中的定义。
 * 三个核心接口如下：
 * <pre><code>
 *     1. identifier()：模型唯一标识符
 *     2. namespace()：名空间相关信息，直接从 {@link io.macrocosm.specification.app.HNamespace} 中衍生
 *     3. resource()：
 *        - 资源关联可直接对接单个目录信息，实现资源文件部分的初始化
 *        - 可直接标识 {@link io.horizon.specification.under.HResource} 用来关联到底层存储专用
 * </code></pre>
 * 现阶段此接口只对应：
 * <pre><code>
 *     1. {@link HModel}
 *     2. {@link HEntity}
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HLife extends Serializable, TJson {

    /**
     * 统一模型标识符，按正常计算来说，模型本身具备如下唯一条件区分：
     * <pre><code>
     *     1. namespace：模型所在的名空间，现阶段名空间和应用名称是 1:1 的关系，所以可以通过应用名称来区分
     *     2. identifier：模型的统一标识符，同一个名空间之下模型的标识符不可以重复，通常使用格式为类似 ci.device
     *        - 上述格式中 ci 可以代表模型的类型
     *        - device 则代表模型本身具有实际意义的部分，如果出现子模型则继续使用 .xxx 的方式扩展
     * </code></pre>
     * 如上述示例中构建多级模型树：
     * <pre><code>
     *     ci.device:                           设备类型
     *     | - ci.network：                        网络设备
     *         | - ci.network.router：             路由器
     * </code></pre>
     *
     * @return 模型标识符
     */
    String identifier();

    /**
     * 统一名空间，若使用了不同的名空间则表示当前模型隶属于单个应用，现阶段结构中，名空间可以跨越 租户和应用 而存在，实现全局唯一，在
     * 多租户、多应用的环境中，您必须提供名空间来区分模型的所属应用，这种情况下静态模型可以和 Meta 所属的数据库 ( Database ) 存储在
     * 一起，而动态模型则使用独立的数据库实现模型操作，是否分库取决于最终需求。
     * 最终部署形态可以按此规则分为如下几种：
     * <pre><code>
     *     项目类型：        | tenantId       | appId/appKey     | sigma
     *     -------------------------------------------------------------
     *     单体项目             1                1                  1
     *     单体容器项目         1                1                  1
     *     多应用项目           1                N                  N
     *     多租户单应用项目      N                1                  N
     *     多租户多应用项目      N                N                  sigma = tenantId
     *
     * </code></pre>
     *
     * 关于 sigma 值的基本原则如下：
     * <ul>
     *     <li>Tenant租户标识是基于 sigma 而存在的，在单租户多应用项目中例外</li>
     *     <li>应用表示分两部分：appId 负责应用所属，appKey 负责私有、敏感、安全数据的应用标识（随机字符串）</li>
     *     <li>如果是多租户项目，那么一个 sigma 中可能包含多个 appId，如：App1, App2, App3</li>
     * </ul>
     *
     * 此处的 namespace 名空间就是和应用直接绑定的，它控制了如下几种资源
     * <pre><code>
     *                             Zero Extension Module 中的表对应关系                标识字段
     *     1. 菜单资源                 X_MENU                                          appId
     *     2. 字典资源                 X_TABULAR / X_CATEGORY                          appId / sigma
     *     3. 活动规则                 X_ACTIVITY_RULE                                 namespace
     *     4. 模型定义                 M_MODEL                                         namespace
     *     5. 接口/任务/服务组件        I_API / I_JOB / I_SERVICE                       namespace
     * </code></pre>
     *
     * 简单说：所有业务层相关的内容，包括项目、工作空间、版本库都以 namespace 为主
     *
     * @return 统一名空间
     */
    String namespace();

    /**
     * 应用所在的服务端初始化空间，通常是一个工作目录，也是应用的根目录，现阶段版本的基础规范如下：
     * 参考 {@link io.horizon.eon.em.Environment}，三种不同的目录根目录如下（Gradle的目录结构略有不同）
     * <pre><code>
     *     Development:                 src/main/resources/             开发环境
     *     Production:                  target/                         生产环境
     *     Mockito:                     src/test/resources/             测试环境
     * </code></pre>
     *
     * 每个目录中的结构基础规范如下：
     * <pre><code>
     * /atom/                           Modeler, 建模专用目录（后期的标准化配置目录）
     * /atom/<app>/meta                 元模型目录
     *                                  - json:         JSON格式元模型
     *                                  - xml:          XML格式元模型
     *                                  - ecore:        EMF格式元模型
     *                                  - xlsx:         Excel格式元模型
     * /atom/<app>/reference            JSON 链接文件专用目录
     * /atom/<app>/rule                 JSON 链接文件专用目录
     * /hybrid/                         @Deprecated,    Modeler, 静态建模专用目录（保留配置）
     * /cab/                            Web, 前端配置专用目录
     * /runtime/                        Web, 运行时目录
     * /init                            Init, 初始化目录
     * /init/oob                        Init, 初始化OOB目录，包含配置数据（Demo数据）
     * /init/project                    Init, 项目的出厂设置目录
     * /modulat/                        Module, 模块化专用规范
     * /plugin/                         Module, 插件目录，不同插件使用
     * /workflow/                       Workflow, 工作流引擎配置目录
     * </code></pre>
     *
     * 项目运行时会创建三个核心的文件目录，用于放置项目本身的工程文件
     * <ul>
     *     <li>项目出厂设置：/init/workspace/，用于恢复所有项目的出厂设置专用目录。</li>
     *     <li>（内存中）环境变量：Z_WORKSPACE_RUN，项目运行目录，持久化和非持久化交叉的一个目录，若没有设置环境变量则使用 /temp 临时文件夹</li>
     *     <li>环境变量：Z_WORKSPACE_STORE，项目持久化目录，每次保存之后刷新该目录保证保存的内容会处理掉（和Git版本管理打通）</li>
     * </ul>
     * 如果对接了配置管理器，则直接将配置管理器（ZooKeeper这种）按应用程序名空间做项目的目录规划。
     *
     * @return 应用的工作目录
     */
    String resource();
}
