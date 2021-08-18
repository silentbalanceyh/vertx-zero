package io.vertx.up.atom;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.stable.ForbiddenInsurer;
import io.vertx.up.uca.stable.Insurer;
import io.vertx.up.uca.stable.RequiredInsurer;
import io.vertx.up.uca.stable.TypedInsurer;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Ruler checking for json object / json array
 */
public class Ruler {

    private static final Annal LOGGER = Annal.get(Ruler.class);

    private static final ConcurrentMap<String, JsonObject> RULE_MAP =
            new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Insurer> POOL_INSURER =
            new ConcurrentHashMap<>();

    /**
     * Verify data for each up.god.file
     *
     * @param file The rule up.god.file that input into this method.
     * @param data The data that will be verified.
     *
     * @throws ZeroException Error when verified failure.
     */
    public static void verify(final String file, final JsonObject data) throws ZeroException {
        Fn.onZero(() -> {
            // 1. Rule for json object
            final JsonObject rule = getRule(file);
            verifyItem(data, rule);
            // 2. For json item
            for (final String field : data.fieldNames()) {
                final Object value = data.getValue(field);
                Fn.onZero(() -> {
                    if (Ut.isJObject(value) || Ut.isJArray(value)) {
                        final String filename = file + Strings.DOT + field;
                        if (Ut.isJObject(value)) {
                            // 3.1.2 Json array child
                            verify(filename, (JsonObject) value);
                        } else if (Ut.isJArray(value)) {
                            // 3.1.3 Json array child
                            verify(filename, (JsonArray) value);
                        }
                    }
                }, value);
            }
        }, file, data);
    }

    /**
     * Verify data for each up.god.file
     *
     * @param file The file that current object bind
     * @param data The data that will be verified
     *
     * @throws ZeroException Whether here throw validated exception
     */
    public static void verify(final String file, final JsonArray data) throws ZeroException {
        Fn.onZero(() -> {
            // 1. Rule for json array
            final JsonObject rule = getRule(file);
            verifyItem(data, rule);
            // 2. For json item
            Fn.etJArray(data, (value, field) -> {
                // 3. Value = JsonObject, identify if extension.
                final String filename = file + Strings.DOT + field;
                if (Ut.isJObject(value)) {
                    // JsonObject
                    verify(filename, (JsonObject) value);
                } else if (Ut.isJArray(value)) {
                    // JsonArray
                    verify(filename, (JsonArray) value);
                }
            });
        }, file, data);
    }

    private static <T> void verifyItem(final T input, final JsonObject rule) throws ZeroException {
        Fn.onZero(() -> {

            if (Ut.isJArray(input)) {
                final JsonArray data = (JsonArray) input;
                // Required
                Insurer reference = Fn.poolThread(POOL_INSURER, RequiredInsurer::new, "A-Required");
                reference.flumen(data, rule);
                // Typed
                reference = Fn.poolThread(POOL_INSURER, TypedInsurer::new, "A-Type");
                reference.flumen(data, rule);
                // Forbidden
                reference = Fn.poolThread(POOL_INSURER, ForbiddenInsurer::new, "A-Forbidden");
                reference.flumen(data, rule);
            } else {
                final JsonObject data = (JsonObject) input;
                // Required
                Insurer reference = Fn.poolThread(POOL_INSURER, RequiredInsurer::new, "J-Required");
                reference.flumen(data, rule);
                // Typed
                reference = Fn.poolThread(POOL_INSURER, TypedInsurer::new, "J-Type");
                reference.flumen(data, rule);
                // Forbidden
                reference = Fn.poolThread(POOL_INSURER, ForbiddenInsurer::new, "J-Forbidden");
                reference.flumen(data, rule);
            }
        }, input, rule);
    }

    private static JsonObject getRule(final String file) {
        // Cached rule into memory pool
        final String filename = MessageFormat.format(Values.CONFIG_INTERNAL_RULE, file);
        if (RULE_MAP.containsKey(filename)) {
            LOGGER.debug(Info.RULE_CACHED_FILE, filename);
        } else {
            LOGGER.debug(Info.RULE_FILE, filename);
        }
        return Fn.pool(RULE_MAP, filename, () -> Ut.ioYaml(filename));
    }
}
