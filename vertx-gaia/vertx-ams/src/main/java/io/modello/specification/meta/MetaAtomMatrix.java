package io.modello.specification.meta;

import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.util.HUt;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 逆向分析专用工具类，提供基于数据的逆向数据分析功能，和 MetaAtom 形成
 * 平行四边形的实现结构
 *
 * @author lang : 2023-05-09
 */
class MetaAtomMatrix {
    private final List<Class<?>> types = new ArrayList<>();

    List<Class<?>> type() {
        return this.types;
    }

    Class<?> type(final int index) {
        return index < this.types.size() ? this.types.get(index) : null;
    }

    void compileComplex(final JsonArray data, final HMetaAtom atom) {
        this.types.clear();

        // index = 2
        final JsonArray fields = data.getJsonArray(VValue.TWO);
        // index = 3
        final JsonArray secondary = data.getJsonArray(VValue.THREE);

        if (HUt.isNotNil(fields) && HUt.isNotNil(secondary)) {
            // parent processing
            final ConcurrentMap<Integer, String> parentMap = new ConcurrentHashMap<>();

            /*
             * Iterator on first
             * Calculate region for `null`
             * Should not be `null` value to avoid: java.lang.NullPointerException
             */
            String found = VString.EMPTY;
            final int length = fields.size();
            for (int idx = 0; idx < length; idx++) {
                final Object item = fields.getValue(idx);
                final String field = this.fieldCompile(item);
                if (HUt.isNotNil(field)) {
                    final Class<?> type = atom.type(field);
                    if (JsonArray.class != type) {
                        this.types.add(type);
                    } else {
                        // Start index
                        found = field;
                        parentMap.put(idx, found);
                    }
                } else {
                    // End index
                    parentMap.put(idx, found);
                }
            }
            /*
             * Secondary must be children to extract
             */
            final int lengthChild = secondary.size();
            for (int idx = 0; idx < lengthChild; idx++) {
                final Object item = secondary.getValue(idx);
                final String field = this.fieldCompile(item);
                final String parent = parentMap.getOrDefault(idx, null);
                if (HUt.isNotNil(field) && HUt.isNotNil(parent)) {
                    /*
                     * Find type based on parent/child both
                     */
                    this.types.add(atom.type(parent, field));
                }
            }
        }
    }

    void compileSimple(final JsonArray data, final HMetaAtom atom) {
        this.types.clear();
        // index = 2
        final JsonArray fields = data.getJsonArray(VValue.ONE);
        if (HUt.isNotNil(fields)) {
            fields.forEach(item -> {
                final String field = this.fieldCompile(item);
                this.types.add(atom.type(field));
            });
        }
    }

    private String fieldCompile(final Object input) {
        // 如果 input = null，则返回 null
        if (Objects.isNull(input)) {
            return null;
        }
        /*
         * 如果JsonObject，则直接提取 value 中的信息
         * 如果是非 JsonObject 类型，则将 input 转换成 String 处理
         */
        if (input instanceof JsonObject) {
            return ((JsonObject) input).getString("value");
        } else {
            return (String) input;
        }
    }
}
