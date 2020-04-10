package io.vertx.tp.atom.modeling;

import io.vertx.tp.atom.modeling.element.DataKey;

import java.util.Set;

/**
 * 单名空间
 */
interface AoConnect extends AoRelation {

    /* 从Json中连接Schema：会针对joins做过滤 **/
    Model onJson(Set<Schema> schemas);

    /* 从数据库中连接Schema：不考虑joins，直接连接 **/
    void onDatabase(Set<Schema> schemas);

    DataKey getKey();

    void setKey(DataKey dataKey);
}
