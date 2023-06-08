package io.modello.dynamic.modular.query;

import cn.vertxup.atom.domain.tables.pojos.MField;
import cn.vertxup.atom.domain.tables.pojos.MJoin;
import io.horizon.uca.log.Annal;
import io.horizon.uca.qr.Criteria;
import io.horizon.uca.qr.Sorter;
import io.horizon.uca.qr.syntax.QTree;
import io.modello.dynamic.modular.jooq.internal.Jq;
import io.modello.dynamic.modular.metadata.AoSentence;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.Schema;
import io.vertx.mod.atom.modeling.element.DataMatrix;
import io.vertx.mod.atom.modeling.element.DataTpl;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.vertx.mod.atom.refine.Ao.LOG;

class JoinIngest implements Ingest {

    private static final Annal LOGGER = Annal.get(JoinIngest.class);
    private transient AoSentence sentence;

    @Override
    public Condition onCondition(final DataTpl tpl,
                                 final Criteria criteria,
                                 final ConcurrentMap<String, String> aliasMap) {
        /* 构造查询树 */
        final QTree tree = QTree.create(criteria);
        LOG.SQL.info(tree.hasValue(), LOGGER, "（Join模式）查询分析树：\n{0}", tree.toString());
        /* 抽取Tpl中的查询条件，Join模式考虑多表 */
        final ConcurrentMap<String, DataMatrix> matrixs = tpl.matrixData();
        final ConcurrentMap<String, String> prefixMap = this.calculatePrefix(matrixs, aliasMap);

        return QVisitor.analyze(tree, new HashSet<>(matrixs.values()), prefixMap);
    }

    private ConcurrentMap<String, String> calculatePrefix(
        final ConcurrentMap<String, DataMatrix> matrixs,
        final ConcurrentMap<String, String> aliasMap) {
        /* 1. 计算成 field -> Prefix */
        final ConcurrentMap<String, String> fieldInfo = new ConcurrentHashMap<>();
        matrixs.keySet().forEach(table -> {
            /* 2. 前缀 */
            final String prefix = aliasMap.get(table);
            /* 3. 列信息 */
            final DataMatrix matrix = matrixs.get(table);
            matrix.getAttributes().forEach(attribute -> {
                /* 4. 计算列 */
                final String column = matrix.getColumn(attribute);
                /* 5. 填充 */
                fieldInfo.put(column, prefix);
            });
        });
        return fieldInfo;
    }

    @Override
    public Table<Record> onTable(final DataTpl tpl, final Set<String> tables,
                                 final ConcurrentMap<String, String> aliasMap) {
        /*
         * 多表 Join 查询的时候，使用 LEFT 模式（默认），即主表一定会有数据
         */
        final MJoin leader = tpl.joinLeader();
        if (Objects.isNull(leader)) {
            /*
             * 直接使用自然连接
             * join 中的 priority 全部为 null 的情况，直接使用自然连接
             */
            LOG.SQL.info(LOGGER, "连接模式: Nature（自然连接）");
            return Jq.joinNature(aliasMap);
        } else {
            /*
             * 否则使用左连接，使用 leader 当主表
             */
            final Schema schema = tpl.schema(leader.getEntity());
            final String primary = schema.getTable();
            final ConcurrentMap<String, String> joinedCols = new ConcurrentHashMap<>();
            {
                /*
                 * 计算第二参，被连接的表的对应映射关系
                 */
                final ConcurrentMap<String, String> joined = tpl.joinVoters();
                joined.forEach((table, field) -> {
                    final Schema tableObj = tpl.schema(table);
                    final MField fieldObj = tableObj.getField(field);
                    final String column = fieldObj.getColumnName();
                    /*
                     * 是否封装
                     */
                    final String wrapperCol;
                    if (Objects.nonNull(this.sentence)) {
                        /*
                         * 执行符号的包装
                         */
                        wrapperCol = this.sentence.columnDdl(column);
                    } else {
                        wrapperCol = column;
                    }
                    joinedCols.put(tableObj.getTable(), wrapperCol);
                });
            }
            /*
             * 列专用处理
             */
            LOG.SQL.info(LOGGER, "连接模式: Left（左连接）");
            return Jq.joinLeft(primary, joinedCols, aliasMap);
        }
    }

    @Override
    @SuppressWarnings("all")
    public List<OrderField> onOrder(final DataTpl tpl,
                                    final Sorter sorter,
                                    final ConcurrentMap<String, String> aliasMap) {
        final List<OrderField> orders = new ArrayList<>();
        final JsonObject data = sorter.toJson();
        for (final String field : data.fieldNames()) {
            final String columnName = tpl.column(field);
            if (Objects.nonNull(columnName)) {
                final boolean isAsc = data.getBoolean(field);
                final Field column = DSL.field(columnName);
                orders.add(isAsc ? column.asc() : column.desc());
            }
        }
        LOG.SQL.info(0 < orders.size(), LOGGER, "（Join模式）排序条件：{0}, size = {1}", data.encode(), orders.size());
        return orders;
    }

    @Override
    public Ingest bind(final AoSentence sentence) {
        this.sentence = sentence;
        return this;
    }
}
