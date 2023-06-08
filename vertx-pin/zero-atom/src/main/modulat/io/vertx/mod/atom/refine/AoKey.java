package io.vertx.mod.atom.refine;

import cn.vertxup.atom.domain.tables.pojos.MJoin;
import io.modello.specification.HRecord;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.mod.atom.modeling.data.DataRecord;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class AoKey {

    /*
     * 返回单一的Key
     * 根据 Joins 读取第一个
     */
    static String joinKey(final Model model) {
        return model.dbJoins().stream()
            .map(MJoin::getEntityKey)
            .filter(Objects::nonNull)
            .findFirst().orElse(null);
    }

    /*
     * 从 DataKey 拷贝数据到 Record，
     * 读取主键数据信息
     */
    static ConcurrentMap<String, Object> joinKeys(final Model model,
                                                  final HRecord record) {
        // 1. 读取 join 部分的信息
        final Set<MJoin> joins = model.dbJoins();
        // 2. 返回 join 中的主键部分的值
        final ConcurrentMap<String, Object> keyMap = new ConcurrentHashMap<>();
        joins.stream().map(MJoin::getEntityKey)
            .filter(Objects::nonNull)
            .filter(attribute -> record.fieldUse().contains(attribute))
            // Null Pointer Exception
            .filter(attribute -> Objects.nonNull(record.get(attribute)))
            .forEach(attribute -> keyMap.put(attribute, record.get(attribute)));
        return keyMap;
    }

    static <ID> Object toKey(final ID id) {
        // 先处理类型
        if (null == id) {
            return null;
        }
        // UUID专用
        if (id instanceof UUID) {
            return id.toString();
        }
        return id;
    }

    /*
     * 根据数据转换成 ID
     */
    static <ID> ID toKey(final JsonObject data, final DataAtom atom) {
        final HRecord record = toData(data, atom);
        return record.key();
    }

    static <ID> void toKey(final JsonObject data, final DataAtom atom, final ID defaultKey) {
        if (Objects.nonNull(defaultKey)) {
            final HRecord record = toData(data, atom);
            record.key(defaultKey);
            data.mergeIn(record.toJson());
        }
    }

    private static HRecord toData(final JsonObject data, final DataAtom atom) {
        final HRecord record = new DataRecord();
        Ut.contract(record, DataAtom.class, atom);
        record.set(data);
        return record;
    }
}
