package io.vertx.tp.modular.jooq;

import io.vertx.core.Future;
import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.modular.jooq.internal.Jq;
import io.vertx.tp.modular.metadata.AoSentence;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectWhereStep;

@SuppressWarnings("all")
class JQQuery extends AbstractJQQr {
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
        return this.qrBatch(events, (tables, ingest) -> {
            /* 查询条件一致 */
            final SelectWhereStep query = this.term.getSelectSample(events, tables, ingest);
            return query.fetchArray();
        }, null);
    }

    Future<DataEvent> queryAsync(final DataEvent events) {
        return this.qrBatchAsync(events, (tables, ingest) -> {
            /* 查询条件一致 */
            final SelectWhereStep<Record> query = this.term.getSelectSample(events, tables, ingest);
            return query.fetchAsync().<Record[]>thenApplyAsync(Jq::toRecords);
        }, null);
    }


    DataEvent fetchOne(final DataEvent event) {
        return this.qr(event, (tables, ingest) -> {
            /* 查询条件一致 */
            final SelectWhereStep query = this.term.getSelectSample(event, tables, ingest);
            /* 处理 */
            return query.fetchOne();
        });
    }

    Future<DataEvent> fetchOneAsync(final DataEvent event) {
        return this.qrAsync(event, (tables, ingest) -> {
            final SelectWhereStep<Record> query = this.term.getSelectSample(event, tables, ingest);
            return query.fetchAsync().<Record>thenApplyAsync(Jq::toRecord);
        });
    }

    DataEvent fetchAll(final DataEvent event) {
        return this.qrBatch(event, (tables, ingest) -> {
            /* 查询条件一致 */
            final SelectWhereStep query = this.term.getSelectAll(event, tables, ingest);
            return query.fetchArray();
        }, null);
    }


    Future<DataEvent> fetchAllAsync(final DataEvent event) {
        return this.qrBatchAsync(event, (tables, ingest) -> {
            final SelectWhereStep<Record> query = this.term.getSelectAll(event, tables, ingest);
            return query.fetchAsync().<Record[]>thenApplyAsync(Jq::toRecords);
        }, null);
    }

    DataEvent search(final DataEvent event) {
        return this.qrBatch(event,
                (tables, ingest) -> {
                    /* 条件 */
                    final SelectWhereStep query = this.term.getSelectComplex(event, tables, ingest);
                    /* 排序 */
                    return query.fetchArray();
                },
                (tables, ingest) -> {
                    final SelectWhereStep query = this.term.getSelectSample(event, tables, ingest);
                    /* 查询条件一致 */
                    return (long) query.fetch().size();
                }
        );
    }

    Future<DataEvent> searchAsync(final DataEvent event) {
        return this.qrBatchAsync(event,
                (tables, ingest) -> {
                    final SelectWhereStep<Record> query = this.term.getSelectComplex(event, tables, ingest);
                    return query.fetchAsync().<Record[]>thenApplyAsync(Jq::toRecords);
                },
                (tables, ingest) -> {
                    final SelectWhereStep<Record> query = this.term.getSelectSample(event, tables, ingest);
                    return query.fetchAsync().<Long>thenApplyAsync(result -> Long.valueOf(result.size()));
                });
    }

}
