package io.vertx.tp.modular.jooq;

import io.vertx.core.Future;
import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.modular.jooq.internal.Jq;
import io.vertx.tp.modular.metadata.AoSentence;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectWhereStep;

@SuppressWarnings("all")
class JQQuery extends AbstractJQCrud {
    private final transient JQTerm term;

    JQQuery(final DSLContext context) {
        super(context);
        this.term = new JQTerm(context);
    }

    JQQuery bind(final AoSentence sentence) {
        this.term.bind(sentence);
        return this;
    }

    DataEvent query(final DataEvent events) {
        /* 1. 读取当前 DataMatrix 中的数据 */
        return this.context.transactionResult(configuration -> Jq.doAll(this.getClass(), events, (tables, ingest) -> {
            /* 查询条件一致 */
            final SelectWhereStep query = this.term.getSelectSample(events, tables, ingest);
            return query.fetchArray();
        }));
    }

    Future<DataEvent> queryAsync(final DataEvent events) {
        return null;
    }

    /*
     * 单条件处理，这里读取的最终数据只有一条，所以直接操作即可
     * 数据结构如：
     * Record -> Table1 -> DataMatrix1
     *        -> Table2 -> DataMatrix2
     */
    DataEvent fetchById(final DataEvent event) {
        /* 1. 读取当前DataMatrix 中的数据 */
        return this.read(event, (table, matrix) -> {
            /* 2. 执行条件处理 */
            final SelectWhereStep query = this.context.selectFrom(Jq.toTable(table));

            final Condition condition = Jq.inWhere(matrix);
            query.where(condition);

            /* 3. 执行结果 */
            return query.fetchOne();
        });
    }

    Future<DataEvent> fetchByIdAsync(final DataEvent event) {
        return null;
    }

    DataEvent fetchByIds(final DataEvent events) {
        /* 1. 读取当前 DataMatrix 中的数据 */
        return this.readBatch(events, (table, matrix) -> {
            /* 2. 执行条件处理 */
            final SelectWhereStep query = this.context.selectFrom(Jq.toTable(table));
            final Condition condition = Jq.inWhere(matrix);
            query.where(condition);

            /* 3. 执行结果 */
            return query.fetchArray();
        });
    }

    Future<DataEvent> fetchByIdsAsync(final DataEvent event) {
        return null;
    }

    DataEvent fetchOne(final DataEvent event) {
        return this.context.transactionResult(configuration -> Jq.doQuery(this.getClass(), event, (tables, ingest) -> {
            /* 查询条件一致 */
            final SelectWhereStep query = this.term.getSelectSample(event, tables, ingest);
            /* 处理 */
            return query.fetchOne();
        }));
    }

    Future<DataEvent> fetchOneAsync(final DataEvent event) {
        return null;
    }

    DataEvent fetchAll(final DataEvent event) {
        /* 1. 读取当前DataMatrix 中的数据 */
        return this.context.transactionResult(configuration -> Jq.doAll(this.getClass(), event, (tables, ingest) -> {
            /* 查询条件一致 */
            final SelectWhereStep query = this.term.getSelectAll(event, tables, ingest);
            return query.fetchArray();
        }));
    }


    Future<DataEvent> fetchAllAsync(final DataEvent event) {
        return null;
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

    Future<DataEvent> searchAsync(final DataEvent event) {
        return null;
    }

}
