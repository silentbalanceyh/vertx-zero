package io.horizon.specification.storage;

import io.horizon.eon.em.EmDS;
import io.modello.atom.app.KDatabase;

/**
 * 「抽象数据库接口」Database Store
 * <hr/>
 * 可搭载不同的文件读取内容，实现完整的RDBMS服务，用于存储结构搭建，该接口负责：
 * <pre><code>
 *     1. SQL模式的数据库调用
 *     2. NO-SQL模式的数据库调用
 * </code></pre>
 * 直接使用此接口服务，将数据库访问合并到 HDS 中
 *
 * @author lang : 2023-05-21
 */
public interface HDS extends HStore {
    /**
     * 数据库类型
     * <pre><code>
     *     1. MySQL
     *     2. TiDB
     *     3. PgSQL
     * </code></pre>
     *
     * @return {@link EmDS.Category}
     */
    EmDS.Category category();

    /**
     * 数据库实例
     *
     * @return {@link KDatabase}
     */
    KDatabase database();
}
