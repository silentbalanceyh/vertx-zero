package io.vertx.tp.modular.dao;

import io.vertx.tp.atom.modeling.data.DataAtom;

/**
 * 数据库访问器
 */
public interface AoDao extends
        AoReader,       // 读取
        AoWriter,       // 写入
        AoSearcher,     // 搜索
        AoBatch,        // 批量
        AoAggregator,   // 聚集
        AoPredicate     // 检查
{
    /**
     * SQL语句直接执行，返回影响的行
     */
    int execute(String sql);

    /**
     * 挂载到元数据中，主要用于链接 metadata
     */
    AoDao mount(DataAtom atom);
}
