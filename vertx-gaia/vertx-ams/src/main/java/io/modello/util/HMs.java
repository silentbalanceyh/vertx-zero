package io.modello.util;

import io.modello.specification.HRecord;
import io.vertx.core.json.JsonArray;

/**
 * 全称为 High Order Modeler Service，建模专用工具类，替换原来的
 * HUt 的 Modeler 部分的核心调用，该工具从 HUt 继承，属于上层工具原型链
 * HUt 可以直接调用 HMs / HNc 形成统一归口，依旧走 HUt 工具链
 * 三个工具层主管不同的工具，而且形成完整原型链
 * 注：HMs 的所有注释部分全部挪到 HUt 的统一归口中
 *
 * @author lang : 2023-05-08
 */
public class HMs {
    private HMs() {
    }

    public static JsonArray toJArray(final HRecord[] records) {
        return MModeler.toJArray(records);
    }

    public static String nsApp(final String appName) {
        return MNs.namespace(appName, null);
    }

    public static String nsAtom(final String appName, final String identifier) {
        return MNs.namespace(appName, identifier);
    }
}
