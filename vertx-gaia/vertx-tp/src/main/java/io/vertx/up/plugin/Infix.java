package io.vertx.up.plugin;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Ruler;
import io.vertx.up.exception.zero.ConfigKeyMissingException;
import io.vertx.up.fn.Fn;
import io.horizon.uca.log.Annal;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.util.function.Function;

interface InfixTool {

    static JsonObject init(
        final Annal logger,
        final String key,
        final Class<?> clazz) {
        final Node<JsonObject> node = Ut.instance(ZeroUniform.class);
        final JsonObject options = node.read();
        Fn.outUp(null == options || !options.containsKey(key)
            , logger, ConfigKeyMissingException.class,
            clazz, key);
        return options;
    }

    static <R> R init(
        final Annal logger,
        final String key,
        final JsonObject config,
        final Function<JsonObject, R> executor) {
        Fn.outUp(() -> Ruler.verify(key, config), logger);
        // Copy the JsonObject of configuration
        return executor.apply(config.copy());
    }
}

public interface Infix {
    /**
     * Old code of BUGS
     *
     * // <pre><code class="java">
     * static <R> R initTp(final String key,
     * final Function<JsonObject, R> executor,
     * final Class<?> clazz) {
     * // </code></pre>
     *
     * The new code is here for new
     *
     * // <pre><code class="java">
     * final Annal logger = Annal.get(clazz);
     * final JsonObject options = InfixTool.init(logger, key, clazz);
     * final JsonObject config = null == options.getJsonObject(key) ? new JsonObject() : options.getJsonObject(key);
     * final JsonObject ready = config.containsKey("config") ? config.getJsonObject("config") : new JsonObject();
     * return InfixTool.init(logger, key, ready, executor);
     * return init(key, (config) ->
     * executor.apply(Ut.sureJObject(config.getJsonObject("config"))), clazz);
     * }
     * // </code></pre>
     */
    static <R> R init(final String key,
                      final Function<JsonObject, R> executor,
                      final Class<?> clazz) {
        final Annal logger = Annal.get(clazz);
        final JsonObject options = InfixTool.init(logger, key, clazz);
        return InfixTool.init(logger, key,
            Ut.valueJObject(options.getJsonObject(key)), executor);
    }

    <T> T get();
}
