package io.modello.specification;

import io.horizon.eon.VValue;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 当前包中的专用接口，只提供给 {@link HRecord} 接口继承，用于区分不同类型
 * 的API 分类专用，而不至于将所有接口放在一个文件中。
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

    /*
     *
     */
    boolean isValue(String field);
}

/*
 * Package scope here, provide record copying method
 */
interface Clone {
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
