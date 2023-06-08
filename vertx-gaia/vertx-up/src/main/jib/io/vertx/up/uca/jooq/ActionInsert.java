package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.up.util.Ut;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStepN;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 *
 * INSERT Operation, methods are default ( Package Only )
 */
@SuppressWarnings("all")
class ActionInsert extends AbstractAction {

    ActionInsert(final JqAnalyzer analyzer) {
        super(analyzer);
    }

    /* Future<T> */
    <T> Future<T> insertAsync(final T entity) {
        Objects.requireNonNull(entity);
        final T inserted = this.uuid(entity);
        return ((Future<Integer>) this.dao().insert(inserted)).compose(rows -> {
            this.logging("[ Jq ] insertAsync(T) executed rows: {0}", String.valueOf(rows));
            return Future.succeededFuture(entity);
        });
    }

    /* T */
    <T> T insert(final T entity) {
        Objects.requireNonNull(entity);
        final T inserted = this.uuid(entity);
        final InsertSetMoreStep insertStep = this.context().insertInto(this.analyzer.table())
            .set(this.newRecord(inserted));
        final int rows = insertStep.execute();
        this.logging("[ Jq ] insert(T) executed rows: {0}", String.valueOf(rows));
        return inserted;
    }

    /* Future<List<T>> */
    <T> Future<List<T>> insertAsync(final List<T> list) {
        Objects.requireNonNull(list);
        if (list.isEmpty()) {
            this.logging("[ Jq ] insertAsync(List<T>) executed empty: 0");
            return Future.succeededFuture(new ArrayList<>());
        }
        final List<T> inserted = this.uuid(list);
        return ((Future<Integer>) this.dao().insert(inserted, false)).compose(rows -> {
            this.logging("[ Jq ] insertAsync(List<T>) executed rows: {0}/{1}", String.valueOf(rows), String.valueOf(list.size()));
            return Future.succeededFuture(list);
        }).otherwise(error -> {
            if (Objects.nonNull(error)) {
                this.warning("[ Jq ] (E) insertAsync(List<T>) error: {0}", error.getMessage());
            }
            return new ArrayList<>();
        });
    }

    /* List<T> */
    <T> List<T> insert(final List<T> list) {
        Objects.requireNonNull(list);
        if (list.isEmpty()) {
            this.logging("[ Jq ] insert(List<T>) executed empty: 0");
            return list;
        }
        final List<T> inserted = this.uuid(list);
        InsertSetStep insertStep = this.context().insertInto(this.analyzer.table());
        InsertValuesStepN insertValuesStepN = null;
        for (T pojo : inserted) {
            insertValuesStepN = insertStep.values(newRecord(pojo).intoArray());
        }
        try {
            final int rows = insertValuesStepN.execute();
            this.logging("[ Jq ] insert(List<T>) executed rows: {0}/{1}", String.valueOf(rows), String.valueOf(list.size()));
        } catch (Throwable error) {
            this.warning("[ Jq ] (E) insertAsync(List<T>) error: {0}", error.getMessage());
        }
        return list;
    }

    /*
     * When primary key is String and missed `KEY` field in zero database,
     * Here generate default uuid key, otherwise the `key` field will be ignored.
     * This feature does not support pojo mapping
     */
    private <T> List<T> uuid(final List<T> list) {
        list.forEach(this::uuid);
        return list;
    }

    private <T> T uuid(final T input) {
        if (Objects.nonNull(input)) {
            try {
                final String primaryKey = this.analyzer.primary();
                final String primaryField = Objects.isNull(primaryKey) ? null : primaryKey;
                final Object keyValue = Ut.field(input, primaryField);
                if (Objects.isNull(keyValue)) {
                    Ut.field(input, primaryField, UUID.randomUUID().toString());
                }
            } catch (final Throwable ex) {
                /*
                 * To avoid java.lang.NoSuchFieldException: key
                 * Some relation tables do not contain `key` as primaryKey such as
                 * R_USER_ROLE
                 * - userId
                 * - roleId
                 * Instead of other kind of ids here
                 */
            }
        }
        return input;
    }
}
