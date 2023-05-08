package io.horizon.util;

import io.horizon.annotations.HighOrder;
import io.modello.specification.HRecord;
import io.modello.util.HMs;
import io.vertx.core.json.JsonArray;

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
}
