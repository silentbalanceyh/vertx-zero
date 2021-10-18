package cn.originx.stellaris;

import io.vertx.core.Vertx;
import io.vertx.tp.plugin.elasticsearch.ElasticSearchInfix;
import io.vertx.tp.plugin.excel.ExcelInfix;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.tp.plugin.neo4j.Neo4jInfix;
import io.vertx.tp.plugin.shared.MapInfix;
import io.vertx.up.fn.Actuator;
import io.vertx.up.log.Annal;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class OInfix {

    /**
     * Zero专用日志器，{@link Annal}
     */
    private static final Annal LOGGER = Annal.get(OInfix.class);

    /* 静态初始化 api / ambient 环境 */
    /* 初始化完整环境 */
    // ZeroHeart.init();

    /**
     * 私有构造，转换成工具类
     */
    private OInfix() {
    }

    static void on(final Vertx vertx) {
        // Jooq
        onDB(vertx);
        // Elastic Search
        onES(vertx);
        // Excel
        onEX(vertx);
        // Neo4j
        onGE(vertx);
        // Shared Map
        onSD(vertx);
    }

    /**
     * 初始化Jooq数据库访问环境。
     *
     * @param vertx {@link Vertx} 输入的Vertx实例
     */
    private static void onDB(final Vertx vertx) {
        on("Jooq", () -> JooqInfix.init(vertx));
    }

    /**
     * 初始化Elastic Search全文检索引擎环境。
     *
     * @param vertx {@link Vertx} 输入的Vertx实例
     */
    private static void onES(final Vertx vertx) {
        on("Elastic", () -> ElasticSearchInfix.init(vertx));
    }

    /**
     * 内部调用初始化日志器，出错时输出异常信息
     *
     * @param label    {@link String} 日志显示标签
     * @param actuator {@link Actuator} 「函数接口」同步执行函数
     */
    private static void on(final String label, final Actuator actuator) {
        try {
            actuator.execute();
        } catch (final Throwable ex) {
            LOGGER.warn("[ ZERO ] {0}: {1}", label, ex.getMessage());
        }
    }

    /**
     * 初始化Excel导入/导出环境。
     *
     * @param vertx {@link Vertx} 输入的Vertx实例
     */
    private static void onEX(final Vertx vertx) {
        on("Excel", () -> ExcelInfix.init(vertx));
    }

    /**
     * 初始化Neo4j图引擎。
     *
     * @param vertx {@link Vertx} 输入的Vertx实例
     */
    private static void onGE(final Vertx vertx) {
        on("Neo4j", () -> Neo4jInfix.init(vertx));
    }

    /**
     * 初始化异步缓存池SharedMap环境。
     *
     * @param vertx {@link Vertx} 输入的Vertx实例
     */
    private static void onSD(final Vertx vertx) {
        on("Pool", () -> MapInfix.init(vertx));
    }
}
