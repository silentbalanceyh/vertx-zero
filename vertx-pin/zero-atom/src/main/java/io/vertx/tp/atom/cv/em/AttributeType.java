package io.vertx.tp.atom.cv.em;

/**
 * Model for Attribute
 * MAttribute field ( type ) value
 * <p>
 * - INTERNAL means Data Source came from our database ( Business Database )
 * - EXTERNAL means Data Source should be third part such as
 * RESTful, Integration, FileSystem etc.
 *
 * @author lang
 */
public enum AttributeType {
    /*
     * Business Database
     * 自身数据源
     */
    INTERNAL,
    /*
     * External Data Source
     * 第三方数据源
     */
    EXTERNAL,
}
