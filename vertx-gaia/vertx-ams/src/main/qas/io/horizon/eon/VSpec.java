package io.horizon.eon;

import io.horizon.specification.boot.HInstall;
import io.horizon.util.HUt;
import io.macrocosm.specification.nc.HWrapper;


public interface VSpec {
    /**
     * 计算 Bundle 名称，目录模式，如果是目录模式，则内容遵循
     * {@link VSpecBundle} 内置目录规范
     *
     * @param group    组
     * @param artifact ID
     * @param version  版本
     *
     * @return Bundle 名称
     */
    static String bundle(final String group, final String artifact, final String version) {
        return HUt.fromMessage("{}.{}_{}", group, artifact, version);
    }

    /**
     * 计算 Bundle 名称，文件模式，如果是文件模式，jar 文件内部资源
     * 路径遵循 {@link VSpecBundle} 内置目录规范
     *
     * @param group    组
     * @param artifact ID
     * @param version  版本
     *
     * @return Bundle 名称.jar
     */
    static String bundle_jar(final String group, final String artifact, final String version) {
        return HUt.fromMessage("{}.{}_{}.jar", group, artifact, version);
    }

    /**
     * -----------------------------------------------------------------------------
     * <p>
     * 4. Bundle接入目录规范<br/>
     *
     * Bundle模块基础规范，参考如下：<br/>
     *
     * Eclipse常用插件基础
     * <pre><code>
     * - /META-INF/MANIFEST.MF              模块描述文件
     * - plugin.xml                         插件配置文件（XML）
     * - plugin.properties                  插件配置文件（PROP）
     * - lib/                               依赖库
     * - schema/                            XSD专用文件存储目录
     * - xxx.jar                            JAR文件，主 Bundle
     * </code></pre>
     *
     * 自定义插件基础
     * <pre><code>
     * - lib/
     *   - extension/                       （扩展）第三方开发的扩展JAR文件
     * - modeler/                           （后端）模型库
     *   - emf/                                  - EMF模型库
     *   - atom/                                 - zero-atom 专用模型库
     *     - meta/                                  - 定义
     *     - reference/                             - 引用
     *     - rule/                                  - 规则
     * - init/                              （后端）注册初始化目录，元数据出厂设置
     *   - modeler/                              - 模型注册器
     *   - store/                                - 存储注册器
     *     - ddl/                                   - SQL存储中的基础文件
     *   - cloud/                                - 云端连接器
     *   - development/                          - 开发层编辑器
     *   - oob/                                  - 初始化元数据
     *     - resource/                              - 专用资源文件
     * - backend/                           （后端）后端 Web 主目录
     *   - scripts/                         （后端）后端 Web 专用主脚本
     *     - {type}/                             - 不同类型的脚本，后期扩展脚本引擎
     *   - endpoint/                        （后端）后端 Web 端地址
     *     - api/                                - Web服务接口，RESTful
     *     - web-socket/                         - Web Socket服务
     *     - service-bus/                        - Service Bus服务总线
     *   - webapp/                          （后端）JSP等类型内容
     *     - WEB-INF/                            - 传统资源文件，包括 web.xml / Servlet
     *   - components/                      （后端）组件配置目录
     *     - task/                               - 任务
     *     - handler/                            - 处理器
     *     - event/                              - 事件
     *     - validator/                          - 验证器
     * - frontend/                          （前端）前端配置主目录
     *   - assembly/                        （前端）Native前端处理
     *   - cab/                             （前端）资源包
     *     - {language}/                         - 语言目录（多语言环境）
     *   - scripts/                         （前端）前端脚本（Ts、Js）
     *   - skin/                            （前端）前端皮肤处理专用
     *   - images/                          （前端）图片相关资源
     *     - icon/                               - 图标资源
     *   - components/                      （前端）自定义组件（扩展）
     * </code></pre>
     * </p>
     *
     * @author lang : 2023-05-28
     */
    interface Bundle extends VSpecBundle {
    }

    /**
     * -----------------------------------------------------------------------------
     * <p>
     * 3. Zero模块化专用目录规范<br/>
     * Zero模块化专用目录规范，主要用于 Zero Extension 的模块化处理，Zero Extension可以作为两种模式存在：
     * <ol>
     *     <li>Zero Extension直接使用静态方式作为依赖接入应用中（通常是单机版）</li>
     *     <li>Zero Extension使用 `动态方式` 接入应用（基于OSGI规范），参考下边章节的 Bundle 专用目录规范</li>
     * </ol>
     *
     * 在上述Zero Extension第一种静态接入模式中：由于模块本身会作为依赖的应用，所以需实现全路径唯一，且不可重复，因此针对横向的管理会很差，
     * 于是才有OSGI模式下的标准出现，本章节为第一种静态方式的依赖。直接参考：<a href="https://www.vertx-cloud.cn/document/doc-web/index.html#_%E8%B5%84%E6%BA%90%E7%BB%93%E6%9E%84">4.3.2.资源结构</a>。
     * </p>
     *
     * Zero模块内的核心目录如（假设模块名称 `mod`）
     * <pre><code>
     *     - /action/{code}              （横向）安全，资源管理，code唯一
     *     - /authority/{code}           （横向）安全，权限管理面板，code唯一
     *     - /cab/cn/{uri}               （横向）前端配置，非微前端下的前端统一目录
     *     - /modulat/mod                （横向）模块配置，code唯一为模块对应的名称
     *     - /plugin/mod                 （模块内）模块基础配置
     *       /plugin/sql/mod             （模块内）模块SQL配置
     *     - /pojo/{identifier}          （横向）POJO映射配置，identifier为静态模型文件名
     *     - /workflow/{code}            （横向）工作流配置，code微工作流定义的键值
     * </code></pre>
     *
     * 书写过程给中，小写是目录，大写是值，所有路径本身依靠小写来区分，基本命名规范
     * <pre><code>
     *     1. 目录本名直接使用小写命名
     *     2. 目录节点值则直接使用大写命名
     *     3. 当前目录的全路径（绝对路径）采用 V 大写命名
     * </code></pre>
     * </p>
     *
     * @author lang
     */
    interface Extension extends VSpecExtension {
    }

    /**
     * Bundle基础规范，当前版本为草稿，考虑如下几点：
     * -----------------------------------------------------------------------------
     * <p>
     * 1. 核心目录<br/>
     * 描述了标准目录空间下的基础内容，如：
     * <pre><code>
     *     全容器：
     *     - configuration/             系统配置目录
     *     - init/oob/                  出厂设置（初始化目录）
     *     - runtime/                   运行时目录
     *     - plugin/                    Zero Extension扩展目录
     *     容器和模块对接（OSGI）：
     *     - plugins/
     *     - features/
     *     - extensions/
     * </code></pre>
     * -----------------------------------------------------------------------------
     * <p>
     * 2. 全容器目录
     * <pre><code>
     *   - /configuration/library/      「库信息」
     *       - /system/                      系统库基础目录
     *       - /internal/                    内置应用共享库
     *       - /external/                    扩展库基础目录
     *   - /configuration/editor/       「编辑器」
     *       - /internal/                    默认带的产品内置编辑器
     *          - /{editor_1}/                  编辑器1
     *          - /{editor_2}/                  编辑器2
     *          - ...
     *       - /external/                    扩展编辑器目录
     *          - /{editor_1}/                  编辑器1
     *          - /{editor_2}/                  编辑器2
     *          - ...
     *   - /init/oob/                   「数据初始化」OOB数据初始化（元数据导入）
     *       - /secure/                      安全相关配置数据
     *       - /environment/                 环境相关配置数据
     *       - /navigation/                  服务于前端，由后端提供前端的导航基础（菜单部分的整体运行）
     *   - /runtime/                    「运行时」
     *       - /cache/                       运行时缓存基础
     *       - /log/                         运行时日志信息
     *
     *   - /plugin/                     「旧版插件」区别于OSGI插件，此目录现阶段主要用于 Zero Extension 的遗留系统，且此规范主要应用于
     *                                       单点系统部分，当您的应用是一个单机应用时，则需遵循此种规范，由于和 OSGI 插件规范近似，所以
     *                                       此处需区分 `plugins` 和 `plugin` 两个目录，不同目录代表的含义有所区别，新版中的 OSGI模块
     *                                       化处理完成后，此处的 `plugin` 目录将会被移除，统一使用 OSGI 插件规范。
     *       - /{extension_1}/               扩展1
     *       - /{extension_2}/               扩展2
     *       - ...
     *
     *   {OSGI}                          「OSGI容器」 Bundle外层（接入，遵循OSGI基础规范）
     *   - /features/                        功能
     *   - /plugins/                         插件
     *   - /extension/                   「Bundle扩展」（元模型级）
     *       - /{bundle_1}/                  此处扩展为系统级扩展，在注册流程中，如果有需要直接将Bundle针对系统的基础扩展放置到此处，
     *       - /{bundle_2}/                  并且根据Bundle本身的特性（identifier）构造对应的扩展目录，扩展出来的目录遵循基本的定义
     *                                       配置，您可以为新的Bundle提前执行环境预处理，包括：
     *                                       - 环境基础检查（准入规则）
     *                                       - 环境运行检查（环境扫描）
     *                                       - 运行状态检查（是否可激活、激活完成之后是否状态正常）
     * </code></pre>
     * </p>
     */
    interface Web extends VSpecWeb {
    }

    /**
     * 启动器核心目录设计
     * <p>
     * 内置配置对象 {@link HWrapper}，统一配置接口
     * <pre><code>
     * boot:                # 启动器主配置
     *     launcher:        # 「路径」主容器配置 {@link io.horizon.specification.boot.HLauncher}
     *                           # 主容器1：OSGI容器 -> Jetty Bundle
     *                           # 主容器2：OSGI容器 -> Rx Vertx
     *                           # 主容器3：VertxApplication（单机版Zero标准）
     *                           # 主容器4：AeonApplication（云端启动器）
     *     component:       # {@link io.horizon.spi.BootIo} 中提供并执行初始化
     *        on:           # 配置内置于 {@link io.macrocosm.specification.boot.HOn}
     *        off:          # 配置内置于 {@link io.macrocosm.specification.boot.HOff}
     *        run:          # 配置内置于 {@link io.macrocosm.specification.boot.HRun}
     *     config:          # 实现层所需启动的基础配置，提供路径格式
     *                           # - OSGI容器配置路径
     *                           # - Web容器配置外置路径
     *     rad:             # 配置内置于 {@link io.horizon.specification.under.HRAD}
     *     connect:         # 空间连接专用配置
     *        frontier:     # 配置内置于 {@link io.macrocosm.specification.secure.HFrontier}
     *        galaxy:       # 配置内置于 {@link io.macrocosm.specification.secure.HGalaxy}
     *        space:        # 配置内置于 {@link io.macrocosm.specification.secure.HSpace}
     * </code></pre>
     * 所有组件对应的配置使用 {@link io.horizon.spi.BootIo} 的 SPI提供，此 SPI 会用于构造所有启动节点的核心
     * 区域组件，核心接口改造流程处理：
     * <pre><code>
     * 1. {@link io.horizon.specification.boot.HLauncher} 启动容器
     *    1.1. 内置初始化 {@link HWrapper} 组装所有配置数据
     *         提取上述节点所有配置信息，内置调用 {@link io.horizon.spi.BootIo} 提取不同的配置组件
     *         开启启动组件表，而配置数据则位于不同节点
     *    1.2. 抽取配置信息以初始化配置对象（配置对象只有一套）
     *    1.3. 根据最终结果，构造核心启动组件，然后触发 {@link io.horizon.specification.boot.HLauncher}
     * 2. 组件启动
     *    -- {@link io.horizon.specification.boot.HLauncher}              start
     *       -- {@link io.macrocosm.specification.boot.HOn}               configure 启动配置初始化
     *       -- {@link HInstall}             start Bundle启动，有多少启动多少
     *    -- {@link io.horizon.specification.under.HRAD}                  开发中心启动（可选）
     *    -- connect                                                      安全连接器启动
     * 3. 组件职责
     *    - launcher：主启动器
     *    - on：核心启动器，内置可调用多个 on，实现配置检查
     *      - 环境变量连接器
     *      - 目录检查器
     *      - 配置验证器（启动保证）
     *      - 可选组件扫描器
     *      - 启动Bundle /
     *          - 提取所有 Bundle 走内置流程，Bundle 流程依旧可使用生命周期组件 on / off / run（递归调用）
     *    - off：用于关闭流程
     *    - run：用于更新流程
     *    - rad：用于开发中心
     *    - connect：根据不同的方式获取 {@link io.macrocosm.specification.secure.HAccount}，连接安全中心实现
     *               不同空间模式的整体连接流程
     * </code></pre>
     *
     * </p>
     */
    interface Boot extends VSpecBoot {

    }
}
