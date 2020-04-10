package io.vertx.tp.modular.jooq;

import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.modular.jooq.internal.Jq;
import io.vertx.tp.modular.metadata.AoSentence;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectWhereStep;

@SuppressWarnings("all")
class JQQuery {
    private final transient DSLContext context;
    private final transient JQTerm term;

    JQQuery(final DSLContext context) {
        this.context = context;
        this.term = new JQTerm(context);
    }

    JQQuery bind(final AoSentence sentence) {
        this.term.bind(sentence);
        return this;
    }

    DataEvent query(final DataEvent events) {
        /* 1. 读取当前 DataMatrix 中的数据 */
        return this.context.transactionResult(configuration -> Jq.doQueryAll(this.getClass(), events, (tables, ingest) -> {
            /* 查询条件一致 */
            final SelectWhereStep query = this.term.getSelectSample(events, tables, ingest);

            return query.fetchArray();
        }));
    }

    /*
     * 单条件处理，这里读取的最终数据只有一条，所以直接操作即可
     * 数据结构如：
     * Record -> Table1 -> DataMatrix1
     *        -> Table2 -> DataMatrix2
     */
    DataEvent fetchById(final DataEvent event) {
        /* 1. 读取当前DataMatrix 中的数据 */
        return this.context.transactionResult(configuration -> Jq.doRead(this.getClass(), event, (table, matrix) -> {

            /* 2. 执行条件处理 */
            final SelectWhereStep query = this.context.selectFrom(Jq.toTable(table));

            final Condition condition = Jq.onKey(matrix);
            query.where(condition);

            /* 3. 执行结果 */
            return query.fetchOne();
        }));
    }

    DataEvent fetchByIds(final DataEvent events) {
        /* 1. 读取当前 DataMatrix 中的数据 */
        return this.context.transactionResult(configuration -> Jq.doReads(this.getClass(), events, (table, matrixList) -> {

            /* 2. 执行条件处理 */
            final SelectWhereStep query = this.context.selectFrom(Jq.toTable(table));
            final Condition condition = Jq.onKeys(matrixList);
            query.where(condition);

            /* 3. 执行结果 */
            return query.fetchArray();
        }));
    }

    DataEvent fetchOne(final DataEvent event) {
        return this.context.transactionResult(configuration -> Jq.doQuery(this.getClass(), event, (tables, ingest) -> {
            /* 查询条件一致 */
            final SelectWhereStep query = this.term.getSelectSample(event, tables, ingest);
            /* 处理 */
            return query.fetchOne();
        }));
    }

    DataEvent fetchAll(final DataEvent event) {
        /* 1. 读取当前DataMatrix 中的数据 */
        return this.context.transactionResult(configuration -> Jq.doQueryAll(this.getClass(), event, (tables, ingest) -> {
            /* 查询条件一致 */
            final SelectWhereStep query = this.term.getSelectAll(event, tables, ingest);
            return query.fetchArray();
        }));
    }

    DataEvent search(final DataEvent event) {
        return this.context.transactionResult(configuration -> Jq.doQuery(this.getClass(), event, (tables, ingest) -> {
            /* 条件 */
            final SelectWhereStep query = this.term.getSelectComplex(event, tables, ingest);
            /* 排序 */
            return query.fetchArray();
        }, (tables, ingest) -> {
            final SelectWhereStep query = this.term.getSelectSample(event, tables, ingest);
            /* 查询条件一致 */
            return (long) query.fetch().size();
        }));
    }
}
