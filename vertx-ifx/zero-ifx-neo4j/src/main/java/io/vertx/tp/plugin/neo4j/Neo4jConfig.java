package io.vertx.tp.plugin.neo4j;

import io.vertx.core.json.JsonObject;
import io.horizon.uca.log.Annal;
import io.vertx.up.util.Ut;
import org.neo4j.driver.AuthToken;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Objects;

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
        this.username = input.getString("username");
        this.password = input.getString("password");
        this.port = Objects.isNull(input.getInteger("port")) ? 7687 : input.getInteger("port");
        this.hostname = input.getString("hostname");
        this.async = Objects.isNull(input.getBoolean("async")) ? false : input.getBoolean("async");
        this.protocol = Objects.isNull(input.getString("protocol")) ? "bolt" : input.getString("protocol");
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
