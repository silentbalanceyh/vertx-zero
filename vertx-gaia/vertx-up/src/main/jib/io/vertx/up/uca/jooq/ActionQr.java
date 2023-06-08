package io.vertx.up.uca.jooq;

import io.horizon.uca.qr.Pager;
import io.horizon.uca.qr.Sorter;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.jooq.condition.JooqCond;
import io.vertx.up.uca.jooq.util.JqOut;
import io.vertx.up.util.Ut;
import org.jooq.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class ActionQr extends AbstractAction {
    public ActionQr(final JqAnalyzer analyzer) {
        super(analyzer);
    }

    <T> Future<List<T>> searchAsync(final Ir qr) {
        final Function<DSLContext, List<T>> executor = context -> this.searchInternal(context, qr);
        // Here above statement must be required and splitted
        // You could not write `this.vertxDAO.executeAsync(context -> this.searchInternal(context, inquiry))`
        // Above statement in comments will occurs compile error
        return Future.succeededFuture(executor.apply(this.context()));
    }

    /*
     * Common usage for search engine calling.
     * These methos could help to do query based on Zero Query Engine.
     * Here are two situations:
     * 1): Full query:  pager, sorter, projection, criteria
     * 2): Simple query: criteria only
     */
    <T> Future<List<T>> searchAsync(final JsonObject criteria, final Sorter sorter) {
        final Function<DSLContext, List<T>> executor = context -> this.searchInternal(context, criteria, sorter);
        return Future.succeededFuture(executor.apply(this.context()));
    }

    <T> List<T> search(final Ir qr) {
        return this.searchInternal(this.context(), qr);
    }

    <T> List<T> search(final JsonObject criteria, final Sorter sorter) {
        return this.searchInternal(this.context(), criteria, sorter);
    }

    /*
     * The interface to call `public` only
     * The AOP Cache should call this method to get original data from database
     */
    public <T> List<Object> searchPrimary(final JsonObject criteria, final Sorter sorter) {
        final List<T> entities = this.search(criteria, sorter);
        /*
         * Process entity ids here
         */
        return this.analyzer.primaryValue(entities);
    }

    /*
     * 「Sync method」
     * Simple query
     */
    private <T> List<T> searchInternal(final DSLContext context, final JsonObject criteria, final Sorter sorter) {
        // Started steps
        final SelectWhereStep started = context.selectFrom(this.analyzer.table());
        // Condition injection
        SelectConditionStep conditionStep = null;
        if (null != criteria) {
            final Condition condition = JooqCond.transform(criteria, this.analyzer::column);
            conditionStep = started.where(condition);
        }

        if (Objects.isNull(sorter)) {
            // Projection
            return started.fetchInto(this.analyzer.type());
        } else {
            // Sorted Enabled
            SelectSeekStepN selectStep = null;
            final List<OrderField> orders = JooqCond.orderBy(sorter, this.analyzer::column, null);
            if (null == conditionStep) {
                selectStep = started.orderBy(orders);
            } else {
                selectStep = conditionStep.orderBy(orders);
            }
            return selectStep.fetchInto(this.analyzer.type());
        }
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
    private <T> List<T> searchInternal(final DSLContext context, final Ir qr) {
        // Started steps
        final SelectWhereStep started = context.selectFrom(this.analyzer.table());
        // Condition set
        SelectConditionStep conditionStep = null;
        if (null != qr.getCriteria()) {
            final JsonObject criteria = qr.getCriteria().toJson();
            final Condition condition = JooqCond.transform(criteria, this.analyzer::column);
            conditionStep = started.where(condition);
        }
        // Sorted Enabled
        SelectSeekStepN selectStep = null;
        if (null != qr.getSorter()) {
            final List<OrderField> orders = JooqCond.orderBy(qr.getSorter(), this.analyzer::column, null);
            if (null == conditionStep) {
                selectStep = started.orderBy(orders);
            } else {
                selectStep = conditionStep.orderBy(orders);
            }
        }
        // Pager Enabled
        SelectWithTiesAfterOffsetStep pagerStep = null;
        if (null != qr.getPager()) {
            final Pager pager = qr.getPager();
            if (null == selectStep && null == conditionStep) {
                pagerStep = started.offset(pager.getStart()).limit(pager.getSize());
            } else if (null == selectStep) {
                pagerStep = conditionStep.offset(pager.getStart()).limit(pager.getSize());
            } else {
                pagerStep = selectStep.offset(pager.getStart()).limit(pager.getSize());
            }
        }
        // Projection
        final Set<String> projectionSet = qr.getProjection();
        final JsonArray projection = Objects.isNull(projectionSet) ? new JsonArray() : Ut.toJArray(projectionSet);
        // Returned one by one
        if (null != pagerStep) {
            return JqOut.toResult(pagerStep.fetchInto(this.analyzer.type()), projection, this.analyzer);
        }
        if (null != selectStep) {
            return JqOut.toResult(selectStep.fetchInto(this.analyzer.type()), projection, this.analyzer);
        }
        if (null != conditionStep) {
            return JqOut.toResult(conditionStep.fetchInto(this.analyzer.type()), projection, this.analyzer);
        }
        return JqOut.toResult(started.fetchInto(this.analyzer.type()), projection, this.analyzer);
    }
}
