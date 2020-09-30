package io.vertx.up.uca.jooq;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.tp.plugin.jooq.condition.JooqCond;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Operator;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Two mode:
 *
 * 1) Dim1: Sync / Async
 * 2) Dim2: Pojo / Bind-Pojo / Non-Pojo
 * 3) Dim3: T, List<T>, JsonObject, JsonArray
 *
 * This class is for basic operation abstraction such as:
 *
 * INSERT, UPDATE, DELETE, SELECT etc.
 *
 * The scope is default ( Package Only )
 */
@SuppressWarnings("all")
abstract class AbstractAction {
    protected transient final VertxDAO vertxDAO;
    protected transient final JqAnalyzer analyzer;

    protected AbstractAction(final JqAnalyzer analyzer) {
        this.analyzer = analyzer;
        this.vertxDAO = analyzer.vertxDAO();
    }

    /**
     * The logger could be used by sub-class only
     */
    protected Annal logger() {
        return Annal.get(getClass());
    }

    /*
     * Future processed for output, this api could convert common java Future to vertx Future
     * Here are two modes:
     * 1): T -> T
     * 2): T -> Void -> T ( DELETE OPERATION )
     * If there are some failure occurs, call `failure` to print stack exception message
     */
    protected <T> Future<T> successed(final CompletableFuture<T> future) {
        final Promise<T> promise = Promise.promise();
        future.thenAcceptAsync(promise::complete).exceptionally((ex) -> failure(ex, promise));
        return promise.future();
    }

    protected <T> Future<T> successed(final CompletableFuture<Void> future, final T input) {
        final Promise<T> promise = Promise.promise();
        future.thenAcceptAsync(nil -> promise.complete(input)).exceptionally((ex) -> failure(ex, promise));
        return promise.future();
    }

    private Void failure(final Throwable ex, final Promise promise) {
        logger().jvm(ex);
        promise.fail(ex);
        return null;
    }

    /*
     * Parameter processing here
     * Here are two situations:
     * 1): No collection -> List<Object> -> [Element]
     * 2): List type -> Direct for list -> [Element, ...]
     */
    protected Collection<Object> parameters(final Object value) {
        if (value instanceof Collection) {
            return (Collection<Object>) value;
        } else {
            /*
             * List as the first collection type selected
             */
            return Arrays.asList(value);
        }
    }

    protected DSLContext context() {
        return JooqInfix.getDSL();
    }

    protected Condition condition(final JsonObject criteria) {
        return Ut.isNil(criteria) ? null : JooqCond.transform(criteria, this.analyzer::column);
    }

    protected Condition conditionAnd(final JsonObject criteria) {
        return JooqCond.transform(criteria, Operator.AND, this.analyzer::column);
    }
}
