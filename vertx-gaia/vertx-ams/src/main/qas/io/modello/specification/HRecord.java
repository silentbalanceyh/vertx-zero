package io.modello.specification;

import io.horizon.eon.VValue;
import io.horizon.specification.typed.TJson;
import io.vertx.core.json.JsonObject;

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
public interface HRecord extends Serializable, HMeta, HCheck, HClone, TJson {
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
    <V> HRecord set(String field, V value);

    HRecord set(JsonObject data);

    /*
     * 「Append Mode」
     * Set data or missing, if existing do not set the value
     */
    <V> HRecord add(String field, V value);

    HRecord add(JsonObject data);

    /*
     * 「Add Virtual Mode」
     */
    <V> HRecord attach(String field, V value);

    /*
     * Remove by field / field
     */
    HRecord remove(String field);

    HRecord remove(String... fields);
}


/**
 * 当前包中的专用接口，只提供给 {@link HRecord} 接口继承，用于区分不同类型
 * 的API 分类专用，而不至于将所有接口放在一个文件中。
 */
interface HCheck {
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

    /*
     *
     */
    boolean isValue(String field);
}

/*
 * Package scope here, provide record copying method
 */
interface HClone {
    /*
     * Create new record with `attributes`, the subset of current record.
     */
    HRecord createSubset(String... attributes);

    /*
     * Create new record
     */
    HRecord createNew();

    /*
     * Create new record based on current
     */
    HRecord createCopy();
}

/*
 * Package scope here, provide metadata extraction interface.
 */
interface HMeta {
    /*
     * JsonObject ( field names ) based on existing data
     */
    Set<String> fieldUse();

    /*
     * Declared fields information ( All attributes )
     */
    Set<String> field();

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
        return VValue.DFT.M_IDENTIFIER_NULL;
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