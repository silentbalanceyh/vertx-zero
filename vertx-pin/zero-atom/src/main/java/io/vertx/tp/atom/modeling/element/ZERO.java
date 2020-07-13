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
            Arrays.asList(record.fields()).forEach(field -> {
                // 抽取表格列
                final String column = field.getName();
                // 抽取字段名
                final String attribute = matrix.getField(column);
                // 抽取字段名为空
                if (Ut.notNil(attribute)) {
                    /*
                     * 这里的 projections 的语义和 Jooq 以及数据域中的语义是一致的
                     *
                     * 1. 如果不传入任何 projection，= []，这种情况下不做任何过滤，直接返回所有的 projection 中的数据
                     * 2. 如果传入了 projection，那么在最终结果中只返回 projection 中包含的字段信息
                     *
                     * 在安全中的视图保存的语义和这里的语义是一致的，S_VIEW 中的保存语义：
                     *
                     * 不仅仅如此，如果出现了下边的情况下，这里提供相关的运算：
                     *
                     * 1. S_VIEW 中存储了当前角色或者用户的列信息
                     * 2. 请求中出现了 projection 的参数信息
                     *
                     * 以上两种情况会使用合并的方式来处理列的计算，也就是说此时 两个 projection 中的数据会进行集合添加，添加的
                     * 最终结果是最终的返回列信息。
                     */
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
