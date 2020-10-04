package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
class ActionCheck extends AbstractAction {

    private transient ActionFetch fetch;

    ActionCheck(final JqAnalyzer analyzer) {
        super(analyzer);
        // Criteria Processing
        this.fetch = new ActionFetch(analyzer);
    }

    <ID> Boolean existById(final ID id) {
        return this.vertxDAO.existsById(id);
    }

    <ID> Future<Boolean> existByIdAsync(final ID id) {
        return this.successed(this.vertxDAO.existsByIdAsync(id));
    }

    <T> Boolean exist(final JsonObject criteria) {
        final List<T> list = this.fetch.fetch(criteria);
        return !list.isEmpty();
    }

    <T> Future<Boolean> existAsync(final JsonObject criteria) {
        return this.fetch.<T>fetchAsync(criteria)
                .compose(list -> Future.succeededFuture(!list.isEmpty()));
    }
}
