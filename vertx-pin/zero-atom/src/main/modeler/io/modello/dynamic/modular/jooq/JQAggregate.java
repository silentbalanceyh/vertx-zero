package io.modello.dynamic.modular.jooq;

import io.vertx.mod.atom.modeling.data.DataEvent;
import org.jooq.DSLContext;
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
}
