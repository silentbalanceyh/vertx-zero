package io.vertx.up.uca.jooq;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.eon.Values;
import io.vertx.up.uca.jooq.util.JqFlow;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ActionSearch extends AbstractAction {
    private transient final ActionQr qr;
    private transient final AggregatorCount counter;

    ActionSearch(final JqAnalyzer analyzer) {
        super(analyzer);
        // Qr
        this.qr = new ActionQr(analyzer);
        this.counter = new AggregatorCount(analyzer);
    }

    <T> Future<JsonObject> searchAsync(final JsonObject query, final JqFlow workflow) {
        return workflow.inputQrAsync(query).compose(inquiry -> {
            // Search Result
            final Future<JsonArray> dataFuture = this.qr.searchAsync(inquiry)   // execute
                .compose(workflow::outputAsync);                            // after : pojo processing
            // Count Result
            final JsonObject criteria = Objects.nonNull(inquiry.getCriteria()) ?
                inquiry.getCriteria().toJson() : new JsonObject();
            final Future<Long> countFuture = this.counter.countAsync(criteria);  // execute

            return CompositeFuture.join(dataFuture, countFuture).compose(result -> {
                // Processing result
                final JsonArray list = result.resultAt(Values.IDX);
                final Long count = result.resultAt(Values.ONE);
                // Result here
                return Future.succeededFuture(Ux.pageData(list, count));
            });
        }).otherwise(Ux.otherwise(new JsonObject()));
    }

    <T> JsonObject search(final JsonObject query, final JqFlow workflow) {
        // Data Processing
        final Qr qr = workflow.inputQr(query);
        final JsonArray list = workflow.output(this.qr.search(qr));
        // Count Processing
        final Long count = this.counter.count(qr.getCriteria().toJson());
        return Ux.pageData(list, count);
    }
}
