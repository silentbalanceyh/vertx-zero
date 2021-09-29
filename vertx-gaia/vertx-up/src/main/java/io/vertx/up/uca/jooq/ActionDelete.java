package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.jooq.Condition;
import org.jooq.DeleteConditionStep;
import org.jooq.Query;

import java.util.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class ActionDelete extends AbstractAction {

    private transient final ActionFetch fetch;

    ActionDelete(final JqAnalyzer analyzer) {
        super(analyzer);
        this.fetch = new ActionFetch(analyzer);
    }

    /* Future<T> */
    <T> Future<T> deleteAsync(final T entity) {
        final Condition condition = this.analyzer.conditionUk(entity);
        return ((Future<Integer>) this.dao().deleteByCondition(condition)).compose(rows -> {
            this.logger().info("[ Jq ] deleteAsync(T) executed rows: {0}", String.valueOf(rows));
            return Future.succeededFuture(entity);
        });
    }

    /* T */
    <T> T delete(final T entity) {
        final Condition condition = this.analyzer.conditionUk(entity);
        final DeleteConditionStep deleteStep = this.context().deleteFrom(this.dsl.getTable())
            .where(condition);
        final int rows = deleteStep.execute();
        this.logger().info("[ Jq ] delete(T) executed rows: {0}", String.valueOf(rows));
        return entity;
    }

    /* Future<List<T>> */
    <T> Future<List<T>> deleteAsync(final List<T> entity) {
        return this.dsl.executeBlocking(h -> h.complete(this.delete(entity)));
    }

    /* List<T> */
    <T> List<T> delete(final List<T> entity) {
        Objects.requireNonNull(entity);
        final List<Query> batchOps = new ArrayList<>();
        entity.stream().map(item -> {
            final Condition condition = this.analyzer.conditionUk(item);
            return this.context().delete(this.dsl.getTable()).where(condition);
        }).forEach(batchOps::add);
        final int rows[] = this.context().batch(batchOps).execute();
        final long updated = Arrays.stream(rows).filter(value -> Values.ONE == value).count();
        this.logger().info("[ Jq ] delete(List<T>) executed rows: {0}/{1}",
            String.valueOf(updated), String.valueOf(rows.length));
        return entity;
    }

    /* By Id */

    <T, ID> Boolean deleteById(final Collection<ID> ids) {
        Objects.requireNonNull(ids);
        final List<Query> batchOps = new ArrayList<>();
        ids.stream().map(id -> {
            final Condition condition = this.analyzer.conditionId(id);
            return this.context().delete(this.dsl.getTable()).where(condition);
        }).forEach(batchOps::add);
        final int rows[] = this.context().batch(batchOps).execute();
        final long updated = Arrays.stream(rows).filter(value -> Values.ONE == value).count();
        this.logger().info("[ Jq ] deleteById(Collection<ID>) executed rows: {0}/{1}",
            String.valueOf(updated), String.valueOf(rows.length));
        return Boolean.TRUE;
    }

    <T, ID> Future<Boolean> deleteByIdAsync(final Collection<ID> ids) {
        Objects.requireNonNull(ids);
        return ((Future<Boolean>) this.dao().deleteByIds(ids)).compose(rows -> {
            this.logger().info("[ Jq ] deleteByIdAsync(Collection<ID>) executed rows: {0}/{1}",
                String.valueOf(rows), String.valueOf(ids.size()));
            return Future.succeededFuture(Boolean.TRUE);
        });
    }

    <T, ID> Future<Boolean> deleteByAsync(final JsonObject criteria) {
        if (Ut.isNil(criteria)) {
            /*
             * To avoid deleting all records
             */
            this.logger().info("[ Jq ] deleteByAsync(JsonObject) Ignore because the condition is null: {0}",
                criteria);
            return Ux.future(Boolean.TRUE);
        } else {
            return fetch.<T>fetchAsync(criteria).compose(list -> {
                /*
                 * Extract primary key for eatch entities
                 */
                return this.deleteAsync(list).compose(nil -> Future.succeededFuture(Boolean.TRUE));
            });
        }
    }

    <T, ID> Boolean deleteBy(final JsonObject criteria) {
        if (Ut.isNil(criteria)) {
            /*
             * To avoid deleting all records
             */
            this.logger().info("[ Jq ] deleteBy(JsonObject) Ignore because the condition is null: {0}",
                criteria);
            return Boolean.TRUE;
        } else {
            final List<T> entities = this.fetch.<T>fetch(criteria);
            this.delete(entities);
            return Boolean.TRUE;
        }
    }
}
