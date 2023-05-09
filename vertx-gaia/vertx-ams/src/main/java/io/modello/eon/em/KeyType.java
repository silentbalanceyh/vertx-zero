package io.modello.eon.em;

/**
 * 配合 {@link io.modello.specification.element.HKey} 用于描述
 * 键类型，键类型跟着 RDBMS 的范式处理。
 *
 * @author lang
 */
public enum KeyType {
    UNIQUE,         // 唯一键
    PRIMARY,        // 主键
    FOREIGN,        // 外键
}

