package io.vertx.up.uca.jooq;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.tp.plugin.jooq.condition.JooqCond;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.atom.query.Pager;
import io.vertx.up.uca.jooq.util.JqFlow;
import io.vertx.up.uca.jooq.util.JqOut;
import io.vertx.up.uca.jooq.util.JqTool;
import io.vertx.up.util.Ut;
import org.jooq.*;

import java.util.*;
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

    private transient final JqAnalyzer analyzer;
    private transient JqAggregator aggregator;

    private transient ActionSearch search;
    private transient ActionFetch fetch;

    private JqReader(final JqAnalyzer analyzer) {
        this.analyzer = analyzer;
        this.vertxDAO = analyzer.vertxDAO();
        this.aggregator = JqAggregator.create(analyzer);

        /*
         * New Structure for more details
         */
        this.search = new ActionSearch(analyzer);
        this.fetch = new ActionFetch(analyzer);
    }

    static JqReader create(final JqAnalyzer analyzer) {
        return new JqReader(analyzer);
    }

    // ============ Search Processing =============
    <T> Future<JsonObject> searchAsync(final JsonObject params, final JqFlow workflow) {
        return this.search.searchAsync(params, workflow);
    }

    <T> JsonObject search(final JsonObject params, final JqFlow workflow) {
        return this.search.search(params, workflow);
    }

    // ============ Fetch Processing ================
    <T> Future<List<T>> fetchAllAsync() {
        return this.fetch.fetchAllAsync();
    }

    <T> List<T> fetchAll() {
        return this.fetch.fetchAll();
    }

    <T> Future<List<T>> fetchAsync(final String field, final Object value) {
        return this.fetch.fetchAsync(field, value);
    }

    <T> List<T> fetch(final String field, final Object value) {
        return this.fetch.fetch(field, value);
    }

    <T> Future<List<T>> fetchAsync(final JsonObject criteria) {
        return this.fetch.fetchAsync(criteria);
    }

    <T> List<T> fetch(final JsonObject criteria) {
        return this.fetch.fetch(criteria);
    }

    <T> Future<T> fetchByIdAsync(final Object id) {
        return this.fetch.fetchByIdAsync(id);
    }

    <T> T fetchById(final Object id) {
        return this.fetch.fetchById(id);
    }

    // ============ Fetch One Operation =============
    /* Async fetch one operation: SELECT */
    <T> Future<T> fetchOneAsync(final String field, final Object value) {
        return this.fetch.fetchOneAsync(field, value);
    }

    <T> T fetchOne(final String field, final Object value) {
        return this.fetch.fetchOne(field, value);
    }

    <T> Future<T> fetchOneAsync(final JsonObject criteria) {
        return this.fetch.fetchOneAsync(criteria);
    }

    <T> T fetchOne(final JsonObject criteria) {
        return this.fetch.fetchOne(criteria);
    }

    // ============ Exist Operation =============
    Future<Boolean> existsByIdAsync(final Object id) {
        return JqTool.future(this.vertxDAO.existsByIdAsync(id));
    }

    Boolean existsById(final Object id) {
        return this.vertxDAO.existsById(id);
    }

    private List<Object> parameters(final Object value) {
        if (value instanceof Collection) {
            return (List<Object>) value;
        } else {
            return Arrays.asList(value);
        }
    }

    // ============ Result Wrapper ==============
    private <T> T toResult(final Object value) {
        return null == value ? null : (T) value;
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
            return JqOut.toResult(pagerStep.fetch(this.vertxDAO.mapper()), projection, this.analyzer);
        }
        if (null != selectStep) {
            return JqOut.toResult(selectStep.fetch(this.vertxDAO.mapper()), projection, this.analyzer);
        }
        if (null != conditionStep) {
            return JqOut.toResult(conditionStep.fetch(this.vertxDAO.mapper()), projection, this.analyzer);
        }
        return JqOut.toResult(started.fetch(this.vertxDAO.mapper()), projection, this.analyzer);
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
