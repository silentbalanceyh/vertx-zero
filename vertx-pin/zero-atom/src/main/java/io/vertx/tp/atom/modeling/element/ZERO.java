package io.vertx.tp.atom.modeling.element;

import io.vertx.up.commune.Record;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    /* 主键专用管理 */
    ConcurrentMap<String, DataKey> KEYS =
            new ConcurrentHashMap<>();
}

class Sync {

    static void doData(final DataMatrix matrix,
                       final Record recordReference,
                       final org.jooq.Record record,
                       final Set<String> projection) {
        if (null != record) {
            /* 有数据 */
            Arrays.asList(record.fields())
                    .forEach(field -> {
                        // 抽取表格列
                        final String column = field.getName();
                        // 抽取字段名
                        final String attribute = matrix.getField(column);
                        // 抽取字段名为空
                        if (Ut.notNil(attribute)) {
                            // projection 为空表示不过滤
                            if (0 == projection.size() || projection.contains(attribute)) {
                                // 抽取字段类型
                                final Class<?> type = matrix.getType(attribute);
                                // 读值
                                final Object value = record.getValue(field, type);
                                // 填充值处理
                                matrix.set(attribute, value);
                                // 填充 Record 数据，必须是 JsonObject 可适配类型
                                recordReference.set(attribute, Ut.aiJValue(value, type));
                            }
                        }
            });
        }
    }
}
