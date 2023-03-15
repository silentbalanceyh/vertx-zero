package cn.originx.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

/**
 * ## Json处理专用工具
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class OxJson {
    /*
     * 私有构造函数（工具类转换）
     */
    private OxJson() {
    }

    /**
     * 数组压缩，将每个元素中的`null`使用`""`替换。
     *
     * @param input {@link JsonArray} 待处理数组数据
     * @param atom  {@link DataAtom} 模型定义
     *
     * @return {@link JsonArray} 处理后的数据
     */
    static JsonArray elementCompress(final JsonArray input, final DataAtom atom) {
        final JsonArray data = new JsonArray();
        final ConcurrentMap<String, Class<?>> typeMap = atom.type();
        Ut.itJArray(input).forEach(json -> {
            final JsonObject item = json.copy();
            for (final String field : json.fieldNames()) {
                final Class<?> type = typeMap.get(field);
                if (String.class == type && json.getValue(field) instanceof String) {
                    final String value = json.getString(field);
                    if (Ut.isNil(value)) {
                        /*
                         * null 替换 ""
                         */
                        item.putNull(field);
                    }
                }
            }
            data.add(item);
        });
        return data;
    }

    /**
     * 为记录主键赋值，内置调用`UUID.randomUUID().toString()`。
     *
     * @param data {@link JsonObject} 数据信息
     * @param atom {@link DataAtom} 模型定义
     *
     * @return {@link JsonObject} 处理过的数据信息
     */
    static JsonObject elementUuid(final JsonObject data, final DataAtom atom) {
        final String key = Ao.toKey(data, atom);
        if (Objects.isNull(key)) {
            /*
             * 设置主键 key
             */
            Ao.toKey(data, atom, UUID.randomUUID().toString());
        }
        return data;
    }
}
