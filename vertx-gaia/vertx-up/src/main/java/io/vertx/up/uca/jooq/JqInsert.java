package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
public class JqInsert extends AbstractJq {

    public JqInsert(final JqAnalyzer analyzer) {
        super(analyzer);
    }

    /*
     * insertAsync(T)
     *      <-- insertAsyncJ(T)
     *      <-- insertAsync(JsonObject, String)
     *          <-- insertAsync(JsonObject)
     *          <-- insertAsyncJ(JsonObject, String)
     *              <-- insertAsyncJ(JsonObject)
     * */

    /*
     * Future<T>
     */
    <T> Future<T> insertAsync(final T entity) {
        return this.successed(this.vertxDAO.insertAsync(this.uuid(entity)), entity);
    }

    <T> Future<T> insertAsync(final JsonObject data, final String pojo) {
        return insertAsync((T) this.in(data, pojo));
    }

    <T> Future<T> insertAsync(final JsonObject data) {
        return this.insertAsync(data, this.analyzer.pojoFile());
    }

    /*
     * Future<JsonObject>
     */
    <T> Future<JsonObject> insertAsyncJ(final T entity) {
        return this.insertAsync(entity).compose(this::outJAsync);
    }

    <T> Future<JsonObject> insertAsyncJ(final JsonObject data, final String pojo) {
        return this.insertAsync(data, pojo).compose(result -> this.outJAsync(result, pojo));
    }

    <T> Future<JsonObject> insertAsyncJ(final JsonObject data) {
        return this.insertAsyncJ(data, this.analyzer.pojoFile());
    }

    /*
     * insert(T)
     *      <-- insertJ(T)
     *      <-- insert(JsonObject, String)
     *          <-- insert(JsonObject)
     *          <-- insertJ(JsonObject, String)
     *              <-- insertJ(JsonObject)
     * */

    /*
     * T
     */
    <T> T insert(final T entity) {
        this.vertxDAO.insert(uuid(entity));
        return entity;
    }

    <T> T insert(final JsonObject data, final String pojo) {
        return this.insert(this.in(data, pojo));
    }

    <T> T insert(final JsonObject data) {
        return this.insert(data, this.analyzer.pojoFile());
    }

    /*
     * JsonObject
     */
    <T> JsonObject insertJ(final T entity) {
        return this.outJ(this.insert(entity));
    }

    <T> JsonObject insertJ(final JsonObject data, final String pojo) {
        return this.outJ(this.insert(data, pojo));
    }

    <T> JsonObject insertJ(final JsonObject data) {
        return this.insertJ(data, this.analyzer.pojoFile());
    }

    /*
     *
     */
    <T> Future<List<T>> insertAsync(final List<T> list) {
        return this.successed(this.vertxDAO.insertAsync(list), list);
    }

    <T> Future<List<T>> insertAsync(final JsonArray input, final String pojo) {
        return this.insertAsync(this.in(input, pojo));
    }

    <T> Future<List<T>> insertAsync(final JsonArray input) {
        return this.insertAsync(input, this.analyzer.pojoFile());
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
