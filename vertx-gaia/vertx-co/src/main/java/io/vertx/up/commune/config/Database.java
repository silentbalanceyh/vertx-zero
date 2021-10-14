package io.vertx.up.commune.config;

import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Copyable;
import io.vertx.up.commune.Json;
import io.vertx.up.eon.em.DatabaseType;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.io.Serializable;
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
 *      "jdbcUrl": "jdbc:mysql://ox.engine.cn:3306/DB_ORIGIN_X?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&useSSL=false",
 * }
 * I_SERVICE -> configDatabase
 */
public class Database implements Serializable, Json, Copyable<Database> {

    private static final Annal LOGGER = Annal.get(Database.class);
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);
    /* Database options for different pool */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject options = new JsonObject();
    /* Database host name */
    private transient String hostname;
    /* Database instance name */
    private transient String instance;
    /* Database port number */
    private transient Integer port;
    /* Database category */
    private transient DatabaseType category;
    /* JDBC connection string */
    private transient String jdbcUrl;
    /* Database username */
    private transient String username;
    /* Database password */
    private transient String password;
    /* Database driver class */
    private transient String driverClassName;

    /* Database Connection Testing */
    public static boolean test(final Database database) {
        try {
            DriverManager.getConnection(database.getJdbcUrl(), database.getUsername(), database.getPassword());
            return true;
        } catch (final SQLException ex) {
            // Debug for database connection
            ex.printStackTrace();
            Database.LOGGER.jvm(ex);
            return false;
        }
    }

    /*
     * Get current jooq configuration for Application / Source
     */
    public static Database getCurrent() {
        final JsonObject raw = Database.VISITOR.read();
        final JsonObject jooq = Ut.visitJObject(raw, "jooq", "provider");
        final Database database = new Database();
        database.fromJson(jooq);
        return database;
    }

    /* Database Connection Testing */
    public boolean test() {
        return Database.test(this);
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    public String getInstance() {
        return this.instance;
    }

    public void setInstance(final String instance) {
        this.instance = instance;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(final Integer port) {
        this.port = port;
    }

    public DatabaseType getCategory() {
        return this.category;
    }

    public void setCategory(final DatabaseType category) {
        this.category = category;
    }

    public String getJdbcUrl() {
        return this.jdbcUrl;
    }

    public void setJdbcUrl(final String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return this.driverClassName;
    }

    public void setDriverClassName(final String driverClassName) {
        this.driverClassName = driverClassName;
    }

    @SuppressWarnings("unchecked")
    public <T> T getOption(final String optionKey) {
        final JsonObject options = this.options;
        final Object value = options.getValue(optionKey);
        return Objects.isNull(value) ? null : (T) value;
    }

    public <T> T getOption(final String optionKey, final T defaultValue) {
        final T result = this.getOption(optionKey);
        return Objects.isNull(result) ? defaultValue : result;
    }

    public JsonObject getOptions() {
        return Objects.isNull(this.options) ? new JsonObject() : this.options;
    }

    public void setOptions(final JsonObject options) {
        this.options = options;
    }

    @Override
    public JsonObject toJson() {
        return Ut.serializeJson(this);
    }

    @Override
    public void fromJson(final JsonObject data) {
        if (Ut.notNil(data)) {
            this.category = Ut.toEnum(() -> data.getString("category"),
                DatabaseType.class, DatabaseType.MYSQL5);
            this.hostname = data.getString("hostname");
            this.port = data.getInteger("port");
            this.instance = data.getString("instance");
            this.jdbcUrl = data.getString("jdbcUrl");
            this.username = data.getString("username");
            this.password = data.getString("password");
            this.driverClassName = data.getString("driverClassName");
            /*
             * options
             */
            final Object value = data.getValue("options");
            if (Objects.nonNull(value) && value instanceof JsonObject) {
                this.options.mergeIn((JsonObject) value);
                LOGGER.info("Database Options: {0}", this.options.encode());
            }
        }
    }

    @Override
    public Database copy() {
        final JsonObject json = this.toJson().copy();
        final Database database = new Database();
        database.fromJson(json);
        return database;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Database)) {
            return false;
        }
        final Database database = (Database) o;
        return this.jdbcUrl.equals(database.jdbcUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.jdbcUrl);
    }

    @Override
    public String toString() {
        return "Database{" +
            "hostname='" + this.hostname + '\'' +
            ", instance='" + this.instance + '\'' +
            ", port=" + this.port +
            ", category=" + this.category +
            ", jdbcUrl='" + this.jdbcUrl + '\'' +
            ", username='" + this.username + '\'' +
            ", password='" + this.password + '\'' +
            ", driverClassName='" + this.driverClassName + '\'' +
            ", options=" + (Objects.isNull(this.options) ? "{}" : this.options.encodePrettily()) +
            '}';
    }
}
