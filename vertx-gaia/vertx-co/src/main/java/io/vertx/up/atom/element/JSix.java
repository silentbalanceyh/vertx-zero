package io.vertx.up.atom.element;

import io.horizon.eon.VString;
import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/*
 * 解析
 * {
 *     "components":
 * }
 * 六种：
 * 1）维度一：ChangeFlag
 * - ADD
 * - UPDATE
 * - DELETE
 *
 * 2）维度二：batch
 * - true
 * - false
 *
 * 最终生成六种配置
 *
 * 配置格式：
 *
 * {
 *     "components": {
 *          "ADD.true": {},
 *          "UPDATE.true": {},
 *          "DELETE.true": {},
 *          "ADD.false": {},
 *          "UPDATE.false": {},
 *          "DELETE.false": {}
 *     }
 * }
 *
 * 每一个节点格式：
 * {
 *     "configuration.operation": "TYPE" ( JSix 省略 )
 *     "plugin.component.before": [],
 *     "plugin.component.job": [],
 *     "plugin.component.after": [],
 *     "plugin.config": {
 *     }
 * }
 * -- before: 前置插件（队列）
 * -- job：后置插件（并行）
 * -- after：后置插件（队列）
 * 其他节点为配置
 */
public class JSix implements Serializable {

    private final JsonObject rawData = new JsonObject();

    private JSix(final JsonObject options) {
        final JsonObject configuration = Ut.valueJObject(options);
        final JsonObject pluginComponent = configuration.getJsonObject("components");
        if (Ut.isNotNil(pluginComponent)) {
            this.rawData.mergeIn(pluginComponent, true);
        }
    }

    public static JSix create(final JsonObject options) {
        return new JSix(options);
    }

    public JsonObject batch(final ChangeFlag flag) {
        return this.data(flag, true);
    }

    public JsonObject single(final ChangeFlag flag) {
        return this.data(flag, false);
    }

    /*
     * 读取核心配置
     */
    public JsonObject data(final ChangeFlag type, final boolean batch) {
        /*
         * 构造 key
         */
        final String field = type.name() + VString.DOT + batch;
        final JsonObject configData = Ut.valueJObject(this.rawData.getJsonObject(field));
        /*
         * configuration.operation
         */
        configData.put("configuration.operation", type.name());
        return configData;
    }
}
