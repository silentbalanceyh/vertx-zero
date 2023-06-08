package io.vertx.mod.atom.refine;

import io.modello.dynamic.modular.jooq.convert.JsonArraySider;
import io.modello.dynamic.modular.jooq.convert.JsonObjectSider;
import io.modello.specification.HRecord;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.mod.atom.modeling.data.DataRecord;
import io.vertx.mod.atom.modeling.element.DataMatrix;
import io.vertx.up.atom.element.JBag;
import io.vertx.up.plugin.excel.atom.ExRecord;
import io.vertx.up.plugin.excel.atom.ExTable;
import io.vertx.up.util.Ut;
import org.jooq.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class AoData {
    /* 转换器专用 */
    @SuppressWarnings("all")
    private static ConcurrentMap<Class<?>, Converter> CONVERT_MAP = new ConcurrentHashMap<Class<?>, Converter>() {
        {
            put(JsonArray.class, Ut.singleton(JsonArraySider.class));
            put(JsonObject.class, Ut.singleton(JsonObjectSider.class));
        }
    };

    /*
     * 主键设置
     * Record -> 内部 keyMatrix
     * 1. 从 Record 中提取主键信息
     * 2. 同步数据到 keyMatrix
     */
    static void connect(final HRecord record,
                        final ConcurrentMap<String, DataMatrix> keyMatrix,
                        final ConcurrentMap<String, DataMatrix> dataMatrix,
                        final Set<String> joins) {
        /* 1. 提取主键，PK: 第二次调用主键 */
        final Object id = record.key();

        /* 2. 内部主键设置，keyMatrix */
        keyMatrix.values().forEach(matrix ->
            matrix.getAttributes().forEach(attribute ->
                matrix.set(attribute, id)));

        /* 3. 数据主键设置，dataMatrix */
        dataMatrix.values().forEach(matrix -> matrix.getKeys().stream()
            .filter(attribute -> Objects.nonNull(record.get(attribute)))
            .forEach(attribute -> matrix.set(attribute, record.get(attribute))));

        /* 4. Join中的非主键设置，关联键同样需要设值 */
        dataMatrix.values().forEach(matrix -> {
            final Set<String> attributes = matrix.getAttributes();
            final Set<String> keys = matrix.getKeys();
            joins.stream().filter(attributes::contains)
                /* 非主键 */
                .filter(attribute -> !keys.contains(attribute))
                /* 特殊的设置，非定义主键 */
                .forEach(joinButNoPrimaryKey -> matrix.set(joinButNoPrimaryKey, id));
        });
    }


    static HRecord record(final DataAtom atom) {
        final HRecord record = new DataRecord();
        Ut.contract(record, DataAtom.class, atom);
        return record;
    }

    static List<HRecord> records(final DataAtom atom, final ExTable table) {
        /*
         * 构造 记录集
         */
        final List<ExRecord> records = table.get();
        final List<HRecord> results = new ArrayList<>();
        records.forEach(each -> {
            /*
             * 构造记录集
             */
            final HRecord record = new DataRecord();
            Ut.contract(record, DataAtom.class, atom);
            /*
             * 记录数据
             */
            final JsonObject data = each.toJson();
            record.set(data);
            results.add(record);
        });
        return results;
    }


    static HRecord[] records(final JsonArray data, final DataAtom atom) {
        final List<HRecord> recordList = new ArrayList<>();
        Ut.itJArray(data).map(each -> record(each, atom))
            .forEach(recordList::add);
        return recordList.toArray(new HRecord[]{});
    }

    static HRecord record(final JsonObject data, final DataAtom atom) {
        final HRecord record = new DataRecord();
        Ut.contract(record, DataAtom.class, atom);
        record.fromJson(data);
        return record;
    }

    @SuppressWarnings("all")
    static Converter converter(final Class<?> type) {
        return CONVERT_MAP.getOrDefault(type, null);
    }

    static List<JBag> bagSplit(final JBag pbData, final Integer size) {
        final List<JsonArray> dataList = Ut.elementGroup(pbData.getData(), size);
        final List<JBag> dataPbList = new ArrayList<>();
        dataList.forEach(each -> {
            final JBag data = new JBag();
            data.setIdentifier(pbData.getIdentifier());
            data.setData(each);
            data.setSize(each.size());
            dataPbList.add(data);
        });
        return dataPbList;
    }
}
