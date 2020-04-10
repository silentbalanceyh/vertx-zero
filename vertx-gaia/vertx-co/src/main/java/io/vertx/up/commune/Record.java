package io.vertx.up.commune;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/*
 * Record interface for some data record using
 * 1. namespace: There are some format of `xxx.yyy.xxx`, and each `application` must contain only one namespace,
 * it must be business scope to split different application.
 * 2. identifier: In the whole environment, there must be gold rule:
 *  2.1 ) namespace + identifier should be unique ( Business Scope )
 *  2.2 ) Above format should be global id of one defined model.
 */
public interface Record extends Serializable, Meta, Check, Clone, Json {
    /*
     * Provide attribute name and get related value
     * 1) field -> single field value
     * 2) fields ( field... ) -> subset of current record
     * 3) Get JsonObject of `this.data`
     */
    <T> T get(String field);

    JsonObject get(String... field);

    JsonObject get();

    /*
     * 「Replace Mode」
     * Set data here, @Fluent api
     * 1) Single field = value set
     * 2) JsonObject set into current record
     */
    <V> Record set(String field, V value);

    Record set(JsonObject data);

    /*
     * 「Append Mode」
     * Set data or missing, if existing do not set the value
     */
    <V> Record add(String field, V value);

    Record add(JsonObject data);

    /*
     * Remove by field / field
     */
    Record remove(String field);

    Record remove(final String... fields);
}

/*
 * Package scope here, checking here
 */
interface Check {
    /*
     * Check whether current record has data
     */
    boolean isEmpty();

    /*
     * Whether current record is related to database
     * 1) Persist: Primary Key and keep status with database
     * 2) Not Persist:
     *    2.1 ) The data is not latest
     *    2.2 ) The data may be dirty
     */
    boolean isPersist();
}

/*
 * Package scope here, provide record copying method
 */
interface Clone {
    /*
     * Create new record with `attributes`, the subset of current record.
     */
    Record createSubset(String... attributes);

    /*
     * Create new record
     */
    Record createNew();

    /*
     * Create new record based on current
     */
    Record createCopy();
}

/*
 * Package scope here, provide metadata extraction interface.
 */
interface Meta {
    /*
     * JsonObject ( field names ) based on existing data
     */
    Set<String> fields();

    /*
     * Declared fields information ( All attributes )
     */
    Set<String> declaredFields();

    /*
     * Get MJoin entityKey set
     */
    Set<String> joins();

    /*
     * Type
     */
    ConcurrentMap<String, Class<?>> types();

    /*
     * attributes count.
     */
    int size();

    /*
     * Model identifier
     */
    default String identifier() {
        return Values.EMPTY_IDENTIFIER;
    }

    /*
     * Get primary key value
     */
    <ID> ID key();

    /*
     * Set primary key value
     */
    <ID> void key(ID key);
}
