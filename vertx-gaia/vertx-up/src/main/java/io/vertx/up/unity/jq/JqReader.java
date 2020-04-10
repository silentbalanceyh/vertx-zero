package io.vertx.up.unity.jq;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.atom.query.Pager;
import io.vertx.up.uca.condition.JooqCond;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.jooq.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * Jooq Splitted Reader
 * SELECT
 * COUNT
 * - Search, Check, Find
 */
@SuppressWarnings("all")
public class JqReader {

    private transient final VertxDAO vertxDAO;

    private transient JqAnalyzer analyzer;

    private JqReader(final VertxDAO vertxDAO, final JqAnalyzer analyzer) {
        this.vertxDAO = vertxDAO;
        this.analyzer = analyzer;
    }

    static JqReader create(final VertxDAO vertxDAO, final JqAnalyzer analyzer) {
        return new JqReader(vertxDAO, analyzer);
    }

    JqReader on(final JqAnalyzer analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    // ============ Fetch One Operation =============
    /* Async fetch one operation: SELECT */
    <T> Future<T> fetchOneAsync(final String field, final Object value) {
        return JqTool.future(this.vertxDAO.fetchOneAsync(this.analyzer.column(field), value));
    }

    <T> Future<T> fetchOneAndAsync(final JsonObject filters) {
        final Condition condition = JooqCond.transform(filters, Operator.AND, this.analyzer::column);
        return JqTool.future(this.vertxDAO.fetchOneAsync(condition));
    }

    <T> T fetchOneAnd(final JsonObject filters) {
        final Condition condition = JooqCond.transform(filters, Operator.AND, this.analyzer::column);
        final DSLContext context = JooqInfix.getDSL();
        return this.toResult(context.selectFrom(this.vertxDAO.getTable()).where(condition).fetchOne(this.vertxDAO.mapper()));
    }

    <T> Future<T> findByIdAsync(final Object id) {
        return JqTool.future(this.vertxDAO.findByIdAsync(id));
    }

    <T> Future<List<T>> findAllAsync() {
        return JqTool.future(this.vertxDAO.findAllAsync());
    }

    /* Sync fetch one operation: SELECT */
    <T> T fetchOne(final String field, final Object value) {
        return toResult(this.vertxDAO.fetchOne(this.analyzer.column(field), value));
    }

    <T> T findById(final Object id) {
        return toResult(this.vertxDAO.findById(id));
    }

    <T> List<T> findAll() {
        return this.vertxDAO.findAll();
    }

    // ============ Exist Operation =============
    Future<Boolean> existsByIdAsync(final Object id) {
        return JqTool.future(this.vertxDAO.existsByIdAsync(id));
    }

    Boolean existsById(final Object id) {
        return this.vertxDAO.existsById(id);
    }

    // ============ Fetch List with Condition ===========
    <T> Future<List<T>> fetchAsync(final String field, final Object value) {
        return JqTool.future(this.vertxDAO.fetchAsync(this.analyzer.column(field), Arrays.asList(value)));
    }

    <T> Future<List<T>> fetchAsync(final Condition condition) {
        return JqTool.future(this.vertxDAO.fetchAsync(condition));
    }

    <T> Future<List<T>> fetchInAsync(final String field, final List<Object> values) {
        return JqTool.future(this.vertxDAO.fetchAsync(this.analyzer.column(field), values));
    }

    <T> List<T> fetch(final String field, final Object value) {
        return this.vertxDAO.fetch(this.analyzer.column(field), value);
    }

    <T> List<T> fetchIn(final String field, final List<Object> values) {
        return this.vertxDAO.fetch(this.analyzer.column(field), values.toArray());
    }

    <T> List<T> fetch(final Condition condition) {
        final DSLContext context = JooqInfix.getDSL();
        return context.selectFrom(this.vertxDAO.getTable()).where(condition).fetch(this.vertxDAO.mapper());
    }

    // ============ Result Wrapper ==============
    private <T> T toResult(final Object value) {
        return null == value ? null : (T) value;
    }

    /*
     * Pagination Searching
     */
    Future<JsonObject> searchPaginationAsync(final Inquiry inquiry, final String pojo) {
        final JsonObject response = new JsonObject();
        return this.searchAsync(inquiry)
                .compose(Ux.fnJArray(pojo))
                .compose(array -> {
                    response.put("list", array);
                    return this.countAsync(inquiry);
                })
                .compose(counter -> {
                    response.put("count", counter);
                    return Future.succeededFuture(response);
                });
    }

    <T> JsonObject searchPagination(final Inquiry inquiry, final String pojo) {
        final JsonObject response = new JsonObject();
        final List<T> list = this.search(inquiry);
        response.put("list", Ux.<T>fnJArray(pojo).apply(list));
        final Integer counter = this.count(inquiry);
        response.put("count", counter);
        return response;
    }

    /*
     * Basic Method for low tier search/count pair
     */
    <T> Future<Integer> countAsync(final Inquiry inquiry) {
        return this.countAsync(null == inquiry.getCriteria() ? new JsonObject() : inquiry.getCriteria().toJson());
    }

    <T> Future<Integer> countAsync(final JsonObject filters) {
        final Function<DSLContext, Integer> function
                = dslContext -> null == filters ? dslContext.fetchCount(this.vertxDAO.getTable()) :
                dslContext.fetchCount(this.vertxDAO.getTable(), JooqCond.transform(filters, this.analyzer::column));
        return JqTool.future(this.vertxDAO.executeAsync(function));
    }

    <T> Future<List<T>> searchAsync(final Inquiry inquiry) {
        final Function<DSLContext, List<T>> function = context -> this.searchInternal(context, inquiry);
        return JqTool.future(this.vertxDAO.executeAsync(function));
    }

    <T> Future<List<T>> searchAsync(final JsonObject criteria) {
        final Function<DSLContext, List<T>> function = context -> this.searchInternal(context, criteria);
        return JqTool.future(this.vertxDAO.executeAsync(function));
    }

    <T> List<T> search(final Inquiry inquiry) {
        final DSLContext context = JooqInfix.getDSL();
        return this.searchInternal(context, inquiry);
    }

    <T> List<T> search(final JsonObject criteria) {
        final DSLContext context = JooqInfix.getDSL();
        return this.searchInternal(context, criteria);
    }

    <T> Integer count(final Inquiry inquiry) {
        return this.count(null == inquiry.getCriteria() ? new JsonObject() : inquiry.getCriteria().toJson());
    }

    <T> Integer count(final JsonObject filters) {
        final DSLContext context = JooqInfix.getDSL();
        return null == filters ? context.fetchCount(this.vertxDAO.getTable()) :
                context.fetchCount(this.vertxDAO.getTable(), JooqCond.transform(filters, this.analyzer::column));
    }

    /*
     * Search Internal
     */
    /*
     * Major search code logical inner.
     * Jooq Engine supported
     * 1) sorter
     * 2) pager
     * 3) projection
     * 4) criteria
     */
    private <T> List<T> searchInternal(final DSLContext dslContext, final Inquiry inquiry) {
        // Started steps
        final SelectWhereStep started = dslContext.selectFrom(this.vertxDAO.getTable());
        // Condition set
        SelectConditionStep conditionStep = null;
        if (null != inquiry.getCriteria()) {
            final Condition condition = JooqCond.transform(inquiry.getCriteria().toJson(), this.analyzer::column);
            conditionStep = started.where(condition);
        }
        // Sorted Enabled
        SelectSeekStepN selectStep = null;
        if (null != inquiry.getSorter()) {
            final List<OrderField> orders = JooqCond.orderBy(inquiry.getSorter(), this.analyzer::column, null);
            if (null == conditionStep) {
                selectStep = started.orderBy(orders);
            } else {
                selectStep = conditionStep.orderBy(orders);
            }
        }
        // Pager Enabled
        SelectWithTiesAfterOffsetStep pagerStep = null;
        if (null != inquiry.getPager()) {
            final Pager pager = inquiry.getPager();
            if (null == selectStep && null == conditionStep) {
                pagerStep = started.offset(pager.getStart()).limit(pager.getSize());
            } else if (null == selectStep) {
                pagerStep = conditionStep.offset(pager.getStart()).limit(pager.getSize());
            } else {
                pagerStep = selectStep.offset(pager.getStart()).limit(pager.getSize());
            }
        }
        // Projection
        final Set<String> projectionSet = inquiry.getProjection();
        final JsonArray projection = Objects.isNull(projectionSet) ? new JsonArray() : Ut.toJArray(projectionSet);
        // Returned one by one
        if (null != pagerStep) {
            return JqResult.toResult(pagerStep.fetch(this.vertxDAO.mapper()), projection, this.analyzer.pojo());
        }
        if (null != selectStep) {
            return JqResult.toResult(selectStep.fetch(this.vertxDAO.mapper()), projection, this.analyzer.pojo());
        }
        if (null != conditionStep) {
            return JqResult.toResult(conditionStep.fetch(this.vertxDAO.mapper()), projection, this.analyzer.pojo());
        }
        return JqResult.toResult(started.fetch(this.vertxDAO.mapper()), projection, this.analyzer.pojo());
    }

    private <T> List<T> searchInternal(final DSLContext dslContext, final JsonObject criteria) {
        // Started steps
        final SelectWhereStep started = dslContext.selectFrom(this.vertxDAO.getTable());
        // Condition injection
        SelectConditionStep conditionStep = null;
        if (null != criteria) {
            final Condition condition = JooqCond.transform(criteria, this.analyzer::column);
            conditionStep = started.where(condition);
        }
        // Projection
        return started.fetch(this.vertxDAO.mapper());
    }
}
