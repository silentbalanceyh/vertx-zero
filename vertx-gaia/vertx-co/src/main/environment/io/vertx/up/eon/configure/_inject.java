package io.vertx.up.eon.configure;

/**
 * @author lang : 2023-05-29
 */
interface YmlInject {
    String __KEY = "inject";

    // 内部扩展
    /** {@link jakarta.inject.infix.Mongo} */
    String MONGO = "mongo";
    /** {@link jakarta.inject.infix.MySql} */
    String MYSQL = "mysql";
    /** {@link jakarta.inject.infix.Redis} */
    String REDIS = "redis";
    /** {@link jakarta.inject.infix.Rpc} */
    String RPC = "rpc";
    /** {@link jakarta.inject.infix.Jooq} */
    String JOOQ = "jooq";

    String SESSION = "session";

    String SHARED = "shared";

    String LOGGER = "logger";

    String SECURE = "secure";

    String TRASH = "trash";

    String ES = "elasticsearch";
    String NEO4J = "neo4j";
    String EXCEL = "excel";
}
