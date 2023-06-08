package io.modello.eon.em;

/**
 * @author lang : 2023-05-31
 */
public final class EmKey {
    private EmKey() {
    }

    /**
     * 主键处理模式
     */
    public enum Mode {
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

    /**
     * 配合 {@link io.modello.specification.element.HKey} 用于描述
     * 键类型，键类型跟着 RDBMS 的范式处理。
     *
     * @author lang
     */
    public enum Type {
        UNIQUE,         // 唯一键
        PRIMARY,        // 主键
        FOREIGN,        // 外键
    }
}
