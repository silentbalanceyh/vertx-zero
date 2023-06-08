package io.modello.eon;

/**
 * 扩展配置部分的标准化
 * High Order Database Configuration
 * 存储于数据库中的相关配置，针对字段级的操作
 *
 * @author lang : 2023-06-03
 */
public interface VDBC {

    /**
     * 关联数据表：{@see I_SERVICE}
     */
    interface I_SERVICE extends io.modello.eon.I_SERVICE {
    }
}
