package io.vertx.up.commune.config;

import io.horizon.atom.app.KDatabase;
import io.horizon.eon.em.app.DsSource;
import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.runtime.env.MatureOn;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/*
 * Database linker for JDBC
 * {
 *      "hostname": "localhost",
 *      "instance": "DB_ORIGIN_X",
 *      "username": "lang",
 *      "password": "xxxx",
 *      "port": 3306,
 *      "category": "MYSQL5",
 *      "driverClassName": "Fix driver issue here",
 *      "jdbcUrl": "jdbc:mysql://ox.engine.cn:3306/DB_ORIGIN_X?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&useSSL=false&allowPublicKeyRetrieval=true",
 * }
 * I_SERVICE -> configDatabase
 *
 * YAML 格式说明
 * jooq:
 *    provider:         // PRIMARY
 *    orbit:            // HISTORY
 * workflow:
 *    database:         // WORKFLOW
 */
public class Database extends KDatabase {

    public static final String CURRENT = "provider";
    public static final String HISTORY = "orbit";
    private static final Annal LOGGER = Annal.get(Database.class);
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);
    private static Database DATABASE;

    /* Database Connection Testing */
    public static boolean test(final Database database) {
        try {
            DriverManager.getConnection(database.getJdbcUrl(), database.getUsername(), database.getSmartPassword());
            return true;
        } catch (final SQLException ex) {
            // Debug for database connection
            ex.printStackTrace();
            Database.LOGGER.fatal(ex);
            return false;
        }
    }

    public static Database getCurrent() {
        if (Objects.isNull(DATABASE)) {
            DATABASE = getDatabase(DsSource.PRIMARY, "jooq", CURRENT);
        }
        return (Database) DATABASE.copy();
    }

    public static Database getHistory() {
        return getDatabase(DsSource.HISTORY, "jooq", HISTORY);
    }

    public static Database getCamunda() {
        return getDatabase(DsSource.WORKFLOW, KName.Flow.WORKFLOW, KName.DATABASE);
    }

    private static Database getDatabase(final DsSource mode, final String... keys) {
        final JsonObject raw = Database.VISITOR.read();
        final JsonObject jooq = Ut.visitJObject(raw, keys);
        final JsonObject jooqJ = MatureOn.envDatabase(jooq, mode);
        return configure(jooqJ);
    }

    public static Database configure(final JsonObject databaseJ) {
        final JsonObject jooq = Ut.valueJObject(databaseJ);
        final Database database = new Database();
        database.fromJson(jooq);
        return database;
    }
}
