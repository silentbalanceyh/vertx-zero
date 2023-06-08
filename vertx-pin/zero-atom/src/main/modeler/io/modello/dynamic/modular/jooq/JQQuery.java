package io.modello.dynamic.modular.jooq;

import io.modello.dynamic.modular.metadata.AoSentence;
import io.vertx.mod.atom.modeling.data.DataEvent;
import org.jooq.DSLContext;
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


    DataEvent fetchOne(final DataEvent event) {
        return this.qr(event, (tables, ingest) -> {
            /* 查询条件一致 */
            final SelectWhereStep query = this.term.getSelectSample(event, tables, ingest);
            /* 处理 */
            return query.fetchOne();
        });
    }

    DataEvent fetchAll(final DataEvent event) {
        return this.qrBatch(event, (tables, ingest) -> {
            /* 查询条件一致 */
            final SelectWhereStep query = this.term.getSelectAll(event, tables, ingest);
            return query.fetchArray();
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

}
