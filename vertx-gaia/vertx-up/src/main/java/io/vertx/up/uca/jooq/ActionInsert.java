package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.up.util.Ut;

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
        return this.successed(this.vertxDAO.insertAsync(this.uuid(entity)), entity);
    }

    /* T */
    <T> T insert(final T entity) {
        this.vertxDAO.insert(uuid(entity));
        return entity;
    }

    /* Future<List<T>> */
    <T> Future<List<T>> insertAsync(final List<T> list) {
        return this.successed(this.vertxDAO.insertAsync(uuid(list)), list);
    }

    /* List<T> */
    <T> List<T> insert(final List<T> list) {
        this.vertxDAO.insert(uuid(list));
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
                    Ut.field(input, "key", UUID.randomUUID().toString());
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
