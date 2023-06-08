package io.modello.atom.app;

import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.horizon.eon.VOption;
import io.horizon.eon.em.EmDS;
import io.horizon.runtime.Macrocosm;
import io.horizon.specification.typed.TCopy;
import io.horizon.specification.typed.TJson;
import io.horizon.uca.log.Annal;
import io.horizon.util.HUt;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 抽象数据配置
 * 原 Database 部分从此处继承，实现标准的数据库相关定义，且后续所有数据库定义可以直接在 HDatabase 中继承
 * 而不需要开发自定义的数据库适配定义等信息
 *
 * @author lang : 2023/5/2
 */
public class KDatabase implements Serializable, TCopy<KDatabase>, TJson {
    private static final Annal LOGGER = Annal.get(KDatabase.class);
    /*
     * Get current jooq configuration for EmApp / Source
     */
    /* Database options for different pool */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject options = new JsonObject();
    /* Database host name */
    private String hostname;
    /* Database instance name */
    private String instance;
    /* Database port number */
    private Integer port;
    /* Database category */
    private EmDS.Category category;
    /* JDBC connection string */
    private String jdbcUrl;
    /* Database username */
    private String username;
    /* Database password */
    private String password;
    /* Database driver class */
    private String driverClassName;

    /* Database Connection Testing */
    public static boolean test(final KDatabase database) {
        try {
            DriverManager.getConnection(database.getJdbcUrl(), database.getUsername(), database.getSmartPassword());
            return true;
        } catch (final SQLException ex) {
            // Debug for database connection
            ex.printStackTrace();
            LOGGER.fatal(ex);
            return false;
        }
    }

    public static KDatabase configure(final JsonObject databaseJ) {
        final JsonObject jooq = HUt.valueJObject(databaseJ);
        final KDatabase database = new KDatabase();
        database.fromJson(jooq);
        return database;
    }


    /* Database Connection Testing */
    public boolean test() {
        return KDatabase.test(this);
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

    public EmDS.Category getCategory() {
        return this.category;
    }

    public void setCategory(final EmDS.Category category) {
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

    public String getSmartPassword() {
        final Boolean enabled = HUt.envWith(Macrocosm.HED_ENABLED, false, Boolean.class);
        LOGGER.info("[ HED ] Encrypt of HED enabled: {0}", enabled);
        if (enabled) {
            // HED_ENABLED=true
            return HUt.decryptRSAV(this.password);
        } else {
            return this.password;
        }
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

    public Long getLong(final String optionKey, final Long defaultValue) {
        final Long result = this.getLong(optionKey);
        return Objects.isNull(result) ? defaultValue : result;
    }

    public Long getLong(final String optionKey) {
        final JsonObject options = this.options;
        return options.getLong(optionKey);
    }

    public JsonObject getOptions() {
        return Objects.isNull(this.options) ? new JsonObject() : this.options;
    }

    public void setOptions(final JsonObject options) {
        this.options = options;
    }

    @Override
    public JsonObject toJson() {
        /*
         * 由于不同类型序列化在应用中有所差异，所以此处序列化的标准方法
         * 采用手工执行，而不直接使用序列化操作，若使用序列化则不同序列化
         * 子系统会导致序列化结果不一致
         */
        final JsonObject databaseJ = new JsonObject();
        databaseJ.put(VOption.database.CATEGORY, this.category.name());
        databaseJ.put(VOption.database.HOSTNAME, this.hostname);
        databaseJ.put(VOption.database.PORT, this.port);
        databaseJ.put(VOption.database.INSTANCE, this.instance);
        databaseJ.put(VOption.database.JDBC_URL, this.jdbcUrl);
        databaseJ.put(VOption.database.USERNAME, this.username);
        databaseJ.put(VOption.database.PASSWORD, this.password);
        databaseJ.put(VOption.database.DRIVER_CLASS_NAME, this.driverClassName);
        databaseJ.put(VOption.database.OPTIONS, this.options);
        return databaseJ;
    }

    @Override
    public void fromJson(final JsonObject data) {
        if (HUt.isNotNil(data)) {
            // category
            this.category = HUt.toEnum(() -> data.getString(VOption.database.CATEGORY), EmDS.Category.class, EmDS.Category.MYSQL5);
            // hostname
            this.hostname = data.getString(VOption.database.HOSTNAME);
            // port
            this.port = data.getInteger(VOption.database.PORT);
            // instance
            this.instance = data.getString(VOption.database.INSTANCE);
            this.jdbcUrl = data.getString(VOption.database.JDBC_URL);
            // username
            this.username = data.getString(VOption.database.USERNAME);
            // password
            this.password = data.getString(VOption.database.PASSWORD);
            this.driverClassName = data.getString(VOption.database.DRIVER_CLASS_NAME);
            // options
            final JsonObject options = HUt.valueJObject(data, VOption.database.OPTIONS);
            if (HUt.isNotNil(options)) {
                this.options.mergeIn(options);
                LOGGER.info("Database Options: {0}", this.options.encode());
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <CHILD extends KDatabase> CHILD copy() {
        final JsonObject json = this.toJson().copy();
        final KDatabase database = new KDatabase();
        database.fromJson(json);
        return (CHILD) database;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final KDatabase kDatabase = (KDatabase) o;
        return Objects.equals(this.jdbcUrl, kDatabase.jdbcUrl);
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
