package io.modello.dynamic.modular.jdbc;

import io.modello.atom.app.KDatabase;
import org.jooq.DSLContext;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * 连接数据库专用类，检查连接，读取连接信息
 * 业务数据的底层接口使用了 OxConnection中的内容，由于这部分内容使用了SQL标准化，所以连接的信息不依赖底层数据库种类。
 * 实现类：cn.vertxup.tp.modular.jdbc.DataConnection，主要负责一些SQL语句的CRUD操作，以及带有聚集的复杂聚集操作；
 * 和 vie一样，连接本身面向数据库，所以和上层的 Record 不相干，由于取消了 Value / DataType 的最底层接口（实际看来很繁琐），
 * 直接使用了Java语言中的核心内容，于是底层的 CRUD 就等价于 Connection + JqTool Engine的双组件结构。
 *
 * @author lang
 */
public interface AoConnection {

    /* 返回数据库实例 */
    KDatabase getDatabase();

    /* 返回Sql连接引用 */
    Connection getConnection();

    /* 返回Jooq引擎，动态SQL */
    DSLContext getDSL();

    /* 执行SQL语句 */
    int execute(String sql);

    /* 多列读取，所有类型 */
    List<ConcurrentMap<String, Object>> select(String sql, String[] columns);

    /* 单列读取，泛型处理 */
    <T> List<T> select(String sql, String column);

    /* 聚集函数 COUNT，不带参数处理，直接执行 */
    Long count(String sql);
}
