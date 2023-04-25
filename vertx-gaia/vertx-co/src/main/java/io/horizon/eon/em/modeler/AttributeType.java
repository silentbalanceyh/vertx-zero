package io.horizon.eon.em.modeler;

/**
 * ## Model for Attribute
 *
 * ### 1. Intro
 *
 * MAttribute field ( type ) value
 * <p>
 * - INTERNAL means Data Source came from our database ( Business Database )
 * - EXTERNAL means Data Source should be third part such as
 * RESTful, Integration, FileSystem etc.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
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
    /*
     * Reference Data Source
     * 引用专用
     */
    REFERENCE,
}
