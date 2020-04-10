package io.vertx.tp.ke.init;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.atom.Lexeme;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Cross Infix management here as bus
 * It's defined interface / abstract only and management uniform
 */
public class KePin {

    private static final String TUNNEL_CONFIG = "plugin/channel.json";

    private static final ConcurrentMap<String, Lexeme> LEXEME_MAP
            = new ConcurrentHashMap<>();

    /*
     * Static initialization for loading configuration
     */
    static {
        init();
    }

    public static Lexeme get(final Class<?> interfaceCls) {
        /* Lazy initialization */
        if (LEXEME_MAP.isEmpty()) {
            init();
        }
        return LEXEME_MAP.getOrDefault(interfaceCls.getName(), null);
    }

    private static void init() {
        final JsonObject config = Ut.ioJObject(TUNNEL_CONFIG);
        if (!Ut.isNil(config)) {
            /*
             * Load information here.
             */
            final JsonArray channels = config.getJsonArray("channels");
            channels.stream().filter(Objects::nonNull)
                    .map(item -> (JsonObject) item)
                    .map(Lexeme::new)
                    .filter(Lexeme::isValid)
                    .forEach(lexeme -> LEXEME_MAP.put(lexeme.getInterfaceCls().getName(), lexeme));
        }
    }
}
