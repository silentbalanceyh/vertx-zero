package io.horizon.eon.em.typed;

/**
 * @author lang : 2023-05-21
 */
public enum ObjectFrom {
    RDBMS,      // 实体来自于数据库
    PATH,       // 实体来自于文件
    CLONE,      // 实体来自于克隆（模型模板）
}
