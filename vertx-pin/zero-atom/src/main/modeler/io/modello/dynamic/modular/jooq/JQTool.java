package io.modello.dynamic.modular.jooq;

import io.horizon.eon.VValue;
import io.horizon.uca.log.Annal;
import io.horizon.uca.qr.Pager;
import io.horizon.uca.qr.Sorter;
import io.modello.dynamic.modular.metadata.AoSentence;
import io.modello.dynamic.modular.query.Ingest;
import io.vertx.mod.atom.modeling.data.DataEvent;
import io.vertx.mod.atom.modeling.element.DataTpl;
import org.jooq.Record;
import org.jooq.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import static io.vertx.mod.atom.refine.Ao.LOG;

@SuppressWarnings("all")
class JQTerm {
    private final transient DSLContext context;
    private transient AoSentence sentence;

    JQTerm(final DSLContext context) {
        this.context = context;
    }

    JQTerm bind(final AoSentence sentence) {
        this.sentence = sentence;
        return this;
    }

    /*
     * Complex 内部调用了 Sample，所以直接绑定
     */
    SelectWhereStep getSelectSample(final DataEvent event,
                                    final Set<String> tables,
                                    final Ingest ingest) {
        /*
         * 构造 TableMap
         * 1）单表为空
         * 2）多表有值（并且带前缀）
         */
        final ConcurrentMap<String, String> tableMap = JQToolkit.getMap(tables);
        return this.getSelectSample(event, tables, ingest, tableMap);
    }

    SelectWhereStep getSelectSample(final DataEvent event,
                                    final Set<String> tables,
                                    final Ingest ingest,
                                    final ConcurrentMap<String, String> tableMap) {
        final SelectWhereStep step = this.getSelectAll(event, tables, ingest, tableMap, this.context::selectFrom);
        /*
         * 构造条件
         */
        final Condition condition = JQToolkit.getCondition(event, ingest, tableMap);
        step.where(condition);
        return step;
    }

/*    DeleteWhereStep getDeleteSample(final DataEvent event,
                                    final Set<String> tables,
                                    final Ingest ingest) {
        final ConcurrentMap<String, String> tableMap = JQToolkit.getMap(tables);
        final DeleteWhereStep step = this.getSelectAll(event, tables, ingest, tableMap, this.context::deleteFrom);
        final Condition condition = JQToolkit.getCondition(event, ingest, tableMap);
        step.where(condition);
        return step;
    }*/

    SelectWhereStep getSelectAll(final DataEvent event,
                                 final Set<String> tables,
                                 final Ingest ingest) {
        return this.getSelectAll(event, tables, ingest, null, this.context::selectFrom);
    }
/*
    DeleteWhereStep getDeleteAll(final DataEvent event,
                                 final Set<String> tables,
                                 final Ingest ingest) {
        return this.getSelectAll(event, tables, ingest, null, this.context::deleteFrom);
    }*/


    private <T> T getSelectAll(final DataEvent event,
                               final Set<String> tables,
                               final Ingest ingest,
                               final ConcurrentMap<String, String> inputMap,
                               final Function<Table<Record>, T> executor) {
        if (Objects.nonNull(this.sentence)) {
            /*
             * 重要方法做列处理
             */
            ingest.bind(this.sentence);
        }
        /*
         * 构造 TableMap
         * 1）单表为空
         * 2）多表有值（并且带前缀）
         */
        final ConcurrentMap<String, String> tableMap = Objects.isNull(inputMap) ? JQToolkit.getMap(tables) : inputMap;
        /*
         * 构造初始化条件
         * 1）根据 tables 尺寸
         */
        final Table<Record> table = 1 == tables.size()
            ? ingest.onTable(event.getTpl(), tables)
            : ingest.onTable(event.getTpl(), tables, tableMap);
        return executor.apply(table);
    }

    @SuppressWarnings("unchecked")
    SelectWhereStep getSelectComplex(final DataEvent event,
                                     final Set<String> tables,
                                     final Ingest ingest) {
        /*
         * 构造 TableMap
         * 1）单表为空
         * 2）多表有值（并且带前缀）
         */
        final ConcurrentMap<String, String> tableMap = JQToolkit.getMap(tables);
        /*
         * 构造初始化条件
         */
        final SelectWhereStep step = this.getSelectSample(event, tables, ingest, tableMap);
        /*
         * 构造排序
         */
        final List<OrderField> sorters = JQToolkit.getSorter(event, ingest, tableMap);
        if (!sorters.isEmpty()) {
            step.orderBy(sorters);
        }
        /*
         * 构造分页
         */
        final Pager pager = event.getPager();
        if (Objects.nonNull(pager)) {
            step.offset(pager.getStart()).limit(pager.getSize());
        }
        return step;
    }
}

@SuppressWarnings("all")
class JQToolkit {
    private static final Annal LOGGER = Annal.get(JQToolkit.class);

    static Condition getCondition(final DataEvent event,
                                  final Ingest ingest,
                                  final ConcurrentMap<String, String> map) {
        final Condition condition;
        final DataTpl tpl = event.getTpl();
        if (map.isEmpty()) {
            condition = ingest.onCondition(tpl, JQPre.prepare(tpl.atom(), event.getCriteria()));
            if (Objects.nonNull(condition)) {
                LOG.SQL.info(LOGGER, "单表, 最终条件：{0}", condition);
            }
        } else {
            condition = ingest.onCondition(tpl, JQPre.prepare(tpl.atom(), event.getCriteria()), map);
            if (Objects.nonNull(condition)) {
                LOG.SQL.info(LOGGER, "多表, 最终条件：{0}", condition);
            }
        }
        return condition;
    }

    static ConcurrentMap<String, String> getMap(final Set<String> tables) {
        final ConcurrentMap<String, String> tableMap = new ConcurrentHashMap<>();
        if (VValue.ONE < tables.size()) {
            final String prefix = "T0";
            int index = 1;
            /*
             * Iterator
             */
            final Iterator<String> it = tables.iterator();
            while (it.hasNext()) {
                final String table = it.next();
                /*
                 * 生成固定表前缀
                 */
                final String alias = prefix + index;
                index++;
                tableMap.put(table, alias);
            }
        }
        return tableMap;
    }

    static List<OrderField> getSorter(final DataEvent event,
                                      final Ingest ingest,
                                      final ConcurrentMap<String, String> map) {
        final List<OrderField> orders = new ArrayList<>();
        final Sorter sorter = event.getSorter();
        if (Objects.nonNull(sorter)) {
            if (map.isEmpty()) {
                orders.addAll(ingest.onOrder(event.getTpl(), sorter));
            } else {
                orders.addAll(ingest.onOrder(event.getTpl(), sorter, map));
            }
        }
        return orders;
    }
}
