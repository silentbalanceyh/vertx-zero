package io.vertx.up.commune.element;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.ChangeFlag;
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
 */
public class JSix implements Serializable {

    private final transient JsonObject rawData = new JsonObject();

    private JSix(final JsonObject options) {
        final JsonObject configuration = Ut.sureJObject(options);
        final JsonObject pluginComponent = configuration.getJsonObject("components");
        if (Ut.notNil(pluginComponent)) {
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
        final String field = type.name() + Strings.DOT + batch;
        final JsonObject configData = Ut.sureJObject(this.rawData.getJsonObject(field));
        /*
         * configuration.operation
         */
        configData.put("configuration.operation", type.name());
        return configData;
    }
}
