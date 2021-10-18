package io.vertx.up.uca.jooq;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ActionGroup extends AbstractAggregator {
    private transient final ActionFetch fetch;

    ActionGroup(final JqAnalyzer analyzer) {
        super(analyzer);
        this.fetch = new ActionFetch(analyzer);
    }

    <T> ConcurrentMap<String, List<T>> group(final String field) {
        final List<T> dataList = this.fetch.fetchAll();
        return Ut.elementGroup(dataList, t -> Ut.field(t, field), v -> v);
    }

    <T> ConcurrentMap<String, List<T>> group(final JsonObject criteria, final String field) {
        final List<T> dataList = this.fetch.fetch(criteria);
        return Ut.elementGroup(dataList, t -> Ut.field(t, field), v -> v);
    }
}
