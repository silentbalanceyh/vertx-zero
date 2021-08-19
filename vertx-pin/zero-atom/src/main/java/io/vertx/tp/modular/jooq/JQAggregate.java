package io.vertx.tp.modular.jooq;

import io.vertx.core.Future;
import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.modular.jooq.internal.Jq;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectWhereStep;

@SuppressWarnings("all")
class JQAggregate extends AbstractJQQr {
    private final transient JQTerm term;

    JQAggregate(final DSLContext context) {
        super(context);
        this.term = new JQTerm(context);
    }

    DataEvent count(final DataEvent event) {
        return this.aggr(event, (tables, ingest) -> {
            final SelectWhereStep query = this.term.getSelectSample(event, tables, ingest);
            return (long) query.fetch().size();
        });
    }

    Future<DataEvent> countAsync(final DataEvent event) {
        return this.aggrAsync(event, (tables, ingest) -> {
            final SelectWhereStep<Record> query = this.term.getSelectSample(event, tables, ingest);
            return query.fetchAsync().<Long>thenApplyAsync(Jq::toCount);
        });
    }
}
