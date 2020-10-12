package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.condition.JooqCond;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.atom.query.Pager;
import io.vertx.up.uca.jooq.util.JqOut;
import io.vertx.up.util.Ut;
import org.jooq.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
public class ActionQr extends AbstractAction {
    public ActionQr(final JqAnalyzer analyzer) {
        super(analyzer);
    }

    <T> Future<List<T>> searchAsync(final Inquiry inquiry) {
        final Function<DSLContext, List<T>> executor = context -> this.searchInternal(context, inquiry);
        // Here above statement must be required and splitted
        // You could not write `this.vertxDAO.executeAsync(context -> this.searchInternal(context, inquiry))`
        // Above statement in comments will occurs compile error
        return this.successed(this.vertxDAO.executeAsync(executor));
    }

    /*
     * Common usage for search engine calling.
     * These methos could help to do query based on Zero Query Engine.
     * Here are two situations:
     * 1): Full query:  pager, sorter, projection, criteria
     * 2): Simple query: criteria only
     */
    <T> Future<List<T>> searchAsync(final JsonObject criteria) {
        final Function<DSLContext, List<T>> executor = context -> this.searchInternal(context, criteria);
        return this.successed(this.vertxDAO.executeAsync(executor));
    }

    <T> List<T> search(final Inquiry inquiry) {
        return this.searchInternal(this.context(), inquiry);
    }

    <T> List<T> search(final JsonObject criteria) {
        return this.searchInternal(this.context(), criteria);
    }

    /*
     * The interface to call `public` only
     * The Aop Cache should call this method to get original data from database
     */
    public <T> List<Object> searchPrimary(final JsonObject criteria) {
        final List<T> entities = this.search(criteria);
        /*
         * Process entity ids here
         */
        return this.analyzer.primaryValue(entities);
    }

    /*
     * 「Sync method」
     * Simple query
     */
    private <T> List<T> searchInternal(final DSLContext context, final JsonObject criteria) {
        // Started steps
        final SelectWhereStep started = context.selectFrom(this.vertxDAO.getTable());
        // Condition injection
        SelectConditionStep conditionStep = null;
        if (null != criteria) {
            final Condition condition = JooqCond.transform(criteria, this.analyzer::column);
            conditionStep = started.where(condition);
        }
        // Projection
        return started.fetch(this.vertxDAO.mapper());
    }

    /*
     * 「Sync method」
     * Major search code logical inner.
     * Jooq Engine supported
     * 1) sorter
     * 2) pager
     * 3) projection
     * 4) criteria
     */
    private <T> List<T> searchInternal(final DSLContext context, final Inquiry inquiry) {
        // Started steps
        final SelectWhereStep started = context.selectFrom(this.vertxDAO.getTable());
        // Condition set
        SelectConditionStep conditionStep = null;
        if (null != inquiry.getCriteria()) {
            final JsonObject criteria = inquiry.getCriteria().toJson();
            final Condition condition = JooqCond.transform(criteria, this.analyzer::column);
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
}
