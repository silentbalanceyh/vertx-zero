package io.vertx.up.plugin.neo4j;

import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.util.Ut;
import org.neo4j.driver.AuthToken;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;

import java.io.Serializable;
import java.text.MessageFormat;

public class Neo4jConfig implements Serializable {

    private static final Annal LOGGER = Annal.get(Neo4jConfig.class);

    private final transient String username;
    private final transient String hostname;
    private final transient String password;
    private final transient String protocol;
    private final transient Integer port;
    private final transient boolean async;

    private final transient Config config;

    private Neo4jConfig(final JsonObject params) {
        final JsonObject input = Ut.valueJObject(params);
        LOGGER.info("[ ZERO ] Neo4j configuration: {0}", input.encode());
        this.username = input.getString(YmlCore.neo4j.USERNAME);
        this.password = input.getString(YmlCore.neo4j.PASSWORD);
        this.port = input.getInteger(YmlCore.neo4j.PORT, 7687);
        this.hostname = input.getString(YmlCore.neo4j.HOSTNAME);
        this.async = input.getBoolean(YmlCore.neo4j.ASYNC, Boolean.FALSE);
        this.protocol = input.getString(YmlCore.neo4j.PROTOCOL, "bolt");
        this.config = this.getConfig(input);
    }

    static Neo4jConfig create() {
        return new Neo4jConfig(new JsonObject());
    }

    static Neo4jConfig create(final JsonObject params) {
        return new Neo4jConfig(params);
    }

    private Config getConfig(final JsonObject input) {
        final Config.ConfigBuilder builder = Config.builder();
        final JsonObject options = new JsonObject();
        // TODO: Build options
        return builder.build();
    }

    /*
     * Return token instance
     */
    public AuthToken token() {
        return AuthTokens.basic(this.username, this.password);
    }

    /*
     * Return uri
     */
    public String uri() {
        return MessageFormat.format("{2}://{0}:{1}",
            this.hostname, String.valueOf(this.port), this.protocol);
    }

    /*
     * Return config
     */
    public Config config() {
        return this.config;
    }

    /*
     * Whether async / sync
     */
    public boolean isAsync() {
        return this.async;
    }
}
