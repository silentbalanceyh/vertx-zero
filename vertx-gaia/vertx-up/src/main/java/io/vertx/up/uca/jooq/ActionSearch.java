package io.vertx.up.uca.jooq;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.eon.Values;
import io.vertx.up.uca.jooq.util.JqFlow;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class ActionSearch extends AbstractAction {
    private static final String FIELD_LIST = "list";
    private static final String FIELD_COUNT = "count";
    private transient final ActionQr qr;
    private transient final AggregatorCount counter;

    ActionSearch(final JqAnalyzer analyzer) {
        super(analyzer);
        // Qr
        this.qr = new ActionQr(analyzer);
        this.counter = new AggregatorCount(analyzer);
    }

    <T> Future<JsonObject> searchAsync(final JsonObject query, final JqFlow workflow) {
        final JsonObject response = new JsonObject();

        // Search Result
        final Future<JsonArray> dataFuture = workflow.inputQrAsync(query)  // before : pojo processing
                .compose(this.qr::searchAsync)                              // execute
                .compose(workflow::outputAsync);                            // after : pojo processing
        // Count Result
        final Future<Long> countFuture = this.counter.countAsync(query.getJsonObject(Inquiry.KEY_CRITERIA));

        return CompositeFuture.join(dataFuture, countFuture).compose(result -> {
            // Processing result
            final JsonArray list = result.resultAt(Values.IDX);
            final Long count = result.resultAt(Values.ONE);
            // Result here
            response.put(FIELD_COUNT, count).put(FIELD_LIST, list);
            return Future.succeededFuture(response);
        });
    }

    <T> JsonObject search(final JsonObject query, final JqFlow workflow) {
        final JsonObject response = new JsonObject();
        // Data Processing
        final JsonArray list = workflow.output(this.qr.search(workflow.inputQr(query)));
        // Count Processing
        final Long count = this.counter.count(query.getJsonObject(Inquiry.KEY_CRITERIA));
        response.put(FIELD_COUNT, count).put(FIELD_LIST, list);
        return response;
    }
}
