package io.horizon.util;

import io.horizon.annotations.HighOrder;
import io.horizon.eon.em.EmApp;
import io.macrocosm.specification.program.HArk;
import io.modello.specification.HRecord;
import io.modello.specification.atom.HAtom;
import io.modello.util.HMs;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author lang : 2023/4/28
 */
class _Modeler extends _It {
    protected _Modeler() {
    }

    /**
     * 建模专用转换，将记录转换成JsonObject
     *
     * @param records 记录
     *
     * @return JsonObject
     */
    @HighOrder(HMs.class)
    public static JsonArray toJArray(final HRecord[] records) {
        return HMs.toJArray(records);
    }

    /**
     * 根据应用程序名称获取应用程序名空间，注：默认的名空间是基于 Origin X Engine，这是 Zero 扩展框架内部的动态建模
     * 核心框架，且业务部分是闭源的，它负责直接将 DDL 部分的元数据存储到数据库中，您可以根据接口提供自己的实现，Zero中
     * 的实现部分位于 zero-atom 扩展项目。
     *
     * @param appName 应用程序名称
     *
     * @return 应用程序名空间
     */
    @HighOrder(HMs.class)
    public static String nsApp(final String appName) {
        return HMs.nsApp(appName);
    }

    /**
     * 根据应用程序名称和标识符获取模型所在名空间，此方法需特殊说明，Zero中的建模整体进程如下：
     *
     * <pre><code>
     * 1. （Done）基于Jooq的静态建模创造了Pojo类的 - 静态模型。
     * 2. （Done）基于Atom的动态建模创造了zero-atom中的 - 动态模型。
     * 3. （Done）基于工作流引擎 Camunda 创造了 hybrid 创建了静态可扩展模型，扩展了 - 工单模型。
     * 4. （Progress）基于EMF做一次初探，实现跨框架的 - 统一模型。
     * 5. （Pending）基于数据分析理论和报表设计工具做 - 采集模型。
     * 6. （Pending）基于工具设计平台做低代码专用的 - 调试模型。
     * 7. （Pending）基于行业应用和数字化转型做 - 工业模型 / 业务模型（垂直领域）。
     * </code></pre>
     *
     * @param appName    应用程序名称
     * @param identifier 标识符
     *
     * @return 模型所在名空间
     */
    @HighOrder(HMs.class)
    public static String nsAtom(final String appName, final String identifier) {
        return HMs.nsAtom(appName, identifier);
    }

    /**
     * 返回模型专用的缓存键，该缓存键的构造基于两个核心维度：
     * <pre><code>
     *     1. 模型标识符 identifier
     *     2. 配置项，可变的 JsonObject 详细信息
     * </code></pre>
     *
     * @param atom    模型原子
     * @param options 配置项
     *
     * @return 缓存键
     */
    @HighOrder(HMs.class)
    public static String keyAtom(final HAtom atom, final JsonObject options) {
        return HMs.keyAtom(atom, options);
    }

    /**
     * 返回应用专用的缓存键，该缓存键构造基于应用程序名称（唯一）
     *
     * @param name 应用程序名称
     *
     * @return 缓存键
     */
    @HighOrder(HMs.class)
    public static String keyApp(final String name) {
        return HMs.keyApp(name, null);
    }

    /**
     * 返回应用专用的缓存键，该缓存键构造基于应用程序名称（唯一）
     *
     * @param name  应用程序名称
     * @param owner 租户ID
     *
     * @return 缓存键
     */
    @HighOrder(HMs.class)
    public static String keyApp(final String name, final String owner) {
        return HMs.keyApp(name, owner);
    }

    /**
     * 构造应用专用的缓存键，
     *
     * @param ark 应用配置容器 {@link HArk}
     *
     * @return 缓存键
     */
    @HighOrder(HMs.class)
    public static String keyApp(final HArk ark) {
        return HMs.keyApp(ark);
    }

    /**
     * 返回租户专用的ID值，检索优先级
     * <pre><code>
     *     1. Z_TENANT 环境变量
     *     2. 传入的 id
     *     3. 两个都为 null 则 DEFAULT
     * </code></pre>
     *
     * @param id 租户ID
     *
     * @return 租户ID
     */
    @HighOrder(HMs.class)
    public static String keyOwner(final String id) {
        return HMs.keyOwner(id);
    }

    /**
     * 单租户环境下返回以 appId / appKey 为核心的查询条件，条件格式：
     * <pre><code>
     *     {
     *         "language": "xxx",
     *         "sigma": "xxx",
     *         "appId": "xxx",
     *         "appKey": "xxx"
     *     }
     * </code></pre>
     *
     * @param ark 应用配置容器 {@link HArk}
     *
     * @return 缓存键
     */
    @HighOrder(HMs.class)
    public static JsonObject qrApp(final HArk ark) {
        return HMs.qrApp(ark, null);
    }

    /**
     * 多租户环境下返回以 appId / appKey / tenant 为核心的查询条件，条件格式：
     * <pre><code>
     *     {
     *         "language": "xxx",
     *         "sigma": "xxx",
     *         "appId": "xxx",
     *         "appKey": "xxx",
     *         "tenantId": "xxx"
     *     }
     * </code></pre>
     *
     * @param ark  应用配置容器 {@link HArk}
     * @param mode 模式
     *
     * @return 缓存键
     */
    @HighOrder(HMs.class)
    public static JsonObject qrApp(final HArk ark, final EmApp.Mode mode) {
        return HMs.qrApp(ark, mode);
    }


    /**
     * 单租户环境下返回以 namespace / name  为核心的查询条件，条件格式：
     * <pre><code>
     *     {
     *         "language": "xxx",
     *         "sigma": "xxx",
     *         "name": "xxx",
     *         "namespace": "xxx",
     *     }
     * </code></pre>
     *
     * @param ark 应用配置容器 {@link HArk}
     *
     * @return 缓存键
     */
    @HighOrder(HMs.class)
    public static JsonObject qrService(final HArk ark) {
        return HMs.qrService(ark, null);
    }

    /**
     * 单租户环境下返回以 namespace / name / tenantId 为核心的查询条件，条件格式：
     * <pre><code>
     *     {
     *         "language": "xxx",
     *         "sigma": "xxx",
     *         "name": "xxx",
     *         "namespace": "xxx",
     *         "tenantId": "xxx"
     *     }
     * </code></pre>
     *
     * @param ark  应用配置容器 {@link HArk}
     * @param mode 模式
     *
     * @return 缓存键
     */
    @HighOrder(HMs.class)
    public static JsonObject qrService(final HArk ark, final EmApp.Mode mode) {
        return HMs.qrService(ark, mode);
    }
}
