package io.vertx.up.runtime.env;

import io.horizon.atom.common.Attr;
import io.horizon.atom.common.AttrSet;
import io.macrocosm.specification.boot.HMature;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

/**
 * 成熟度环境模型，负责各种环境变量的读取和执行
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class MatureEnv implements HMature {
    /*
     * vector 数据结构：
     *   field1 = ENV_NAME1
     *   field2 = ENV_NAME2
     */
    @Override
    public JsonObject configure(final JsonObject configJ, final AttrSet set) {
        final Set<String> names = set.names();
        names.stream().filter(field -> {
            // 过滤 envName 为空的情况
            final Attr attribute = set.attribute(field);
            final String envName = attribute.alias();
            return Ut.isNotNil(envName);
        }).forEach(field -> {
            /*
             * 核心解析流程
             * name = alias, alias此处就是 ENV_NAME
             * 1）先从环境变量中提取 ENV_NAME 的值
             * 2）值不存在考虑默认值
             */
            final Attr attribute = set.attribute(field);
            this.normalizeValue(configJ, attribute, field);
        });
        return configJ;
    }

    private void normalizeValue(final JsonObject configJ,
                                final Attr attribute, final String field) {
        final String envName = attribute.alias();

        // Default String.class
        final Class<?> envType = attribute.type();

        /*
         * 「P1」valueE：环境变量值
         * 「P2」valueC: 配置值
         * 「P3」valueD：默认值（编程）
         */
        final Object valueC = configJ.getValue(field);
        final Object valueE = Ut.env(envName, envType);

        if (Objects.nonNull(valueE)) {
            // P1
            configJ.put(field, valueE);
        } else {
            // P2 is null, set P3
            if (Objects.isNull(valueC)) {
                configJ.put(field, attribute.value());
            }
        }
    }
}
