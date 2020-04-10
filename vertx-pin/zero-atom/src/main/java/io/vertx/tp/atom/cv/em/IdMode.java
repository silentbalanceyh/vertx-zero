package io.vertx.tp.atom.cv.em;

/**
 * 主键处理模式
 */
public enum IdMode {
    DIRECT,         // 单表，单主键
    COLLECTION,     // 单表，多主键
    /*
     * 同键单键连接，当一个 Model 和多个实体连接时，Model接收到的数据直接会通过
     * Json 对应的属性值映射到实体中，比如：
     * Model : ci.server
     *     Entity1 : key
     *     Entity2 : key
     * 当传入数据包含了 key = value 的时候，直接将值赋值给两个实体
     */
    JOIN_KEY,    // 同键单键连接
    /*
     * 同键多键连接，当一个 Model 和多个实体连接时，Model接受到的数据直接通过
     * Join 对应的属性映射到实体中，但是是多个键，如：
     * Model: ci.server
     *     Entity1 : key
     *     Entity2 : ciKey
     * 当传入数据包含了
     * {
     *     key: "xxx",
     *     ciKey: "ciXxx"
     * }
     * 这种模式下执行两个实体和
     */
    JOIN_MULTI,   // 同键多键连接
    /*
     * 这是多键连接的特殊情况：一个Model对应多个实体
     * joins 的尺寸 > schema 的尺寸
     */
    JOIN_COLLECTION, // 同键集合键连接
}
