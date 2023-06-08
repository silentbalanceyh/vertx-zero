package io.vertx.up.commune;

import io.horizon.eon.VPath;
import io.horizon.eon.VString;
import io.horizon.exception.ProgramException;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.stable.ForbiddenInsurer;
import io.vertx.up.uca.stable.Insurer;
import io.vertx.up.uca.stable.RequiredInsurer;
import io.vertx.up.uca.stable.TypedInsurer;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * Ruler checking for json object / json array
 */
public class Ruler {

    private static final Annal LOGGER = Annal.get(Ruler.class);

    private static final Cc<String, JsonObject> CC_RULE = Cc.open();
    private static final Cc<String, Insurer> CC_INSURER = Cc.open();


    /**
     * Verify data for each up.god.file
     *
     * @param file The rule up.god.file that input into this method.
     * @param data The data that will be verified.
     *
     * @throws ProgramException Error when verified handler.
     */
    public static void verify(final String file, final JsonObject data) throws ProgramException {
        Fn.bugAt(() -> {
            // 1. Rule for json object
            final JsonObject rule = getRule(file);
            if (Objects.isNull(rule)) {
                return;
            }
            verifyItem(data, rule);
            // 2. For json item
            for (final String field : data.fieldNames()) {
                final Object value = data.getValue(field);
                Fn.bugAt(() -> {
                    if (Ut.isJObject(value) || Ut.isJArray(value)) {
                        final String filename = file + VString.DOT + field;
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
     * @throws ProgramException Whether here throw validated exception
     */
    public static void verify(final String file, final JsonArray data) throws ProgramException {
        Fn.bugAt(() -> {
            // 1. Rule for json array
            final JsonObject rule = getRule(file);
            if (Objects.isNull(rule)) {
                return;
            }
            verifyItem(data, rule);
            // 2. For json item
            Fn.bugIt(data, (value, field) -> {
                // 3. Value = JsonObject, identify if extension.
                final String filename = file + VString.DOT + field;
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

    private static <T> void verifyItem(final T input, final JsonObject rule) throws ProgramException {
        Fn.bugAt(() -> {

            if (Ut.isJArray(input)) {
                final JsonArray data = (JsonArray) input;
                // Required
                Insurer reference = CC_INSURER.pick(RequiredInsurer::new, "A-Required");
                // Fn.po?lThread(POOL_INSURER, RequiredInsurer::new, "A-Required");
                reference.flumen(data, rule);
                // EmType
                reference = CC_INSURER.pick(TypedInsurer::new, "A-EmType");
                // Fn.po?lThread(POOL_INSURER, TypedInsurer::new, "A-Type");
                reference.flumen(data, rule);
                // Forbidden
                reference = CC_INSURER.pick(ForbiddenInsurer::new, "A-Forbidden");
                // Fn.po?lThread(POOL_INSURER, ForbiddenInsurer::new, "A-Forbidden");
                reference.flumen(data, rule);
            } else {
                final JsonObject data = (JsonObject) input;
                // Required
                Insurer reference = CC_INSURER.pick(RequiredInsurer::new, "J-Required");
                // Fn.po?lThread(POOL_INSURER, RequiredInsurer::new, "J-Required");
                reference.flumen(data, rule);
                // EmType
                reference = CC_INSURER.pick(TypedInsurer::new, "J-Type");
                // Fn.po?lThread(POOL_INSURER, TypedInsurer::new, "J-Type");
                reference.flumen(data, rule);
                // Forbidden
                reference = CC_INSURER.pick(ForbiddenInsurer::new, "J-Forbidden");
                // Fn.po?lThread(POOL_INSURER, ForbiddenInsurer::new, "J-Forbidden");
                reference.flumen(data, rule);
            }
        }, input, rule);
    }

    private static JsonObject getRule(final String file) {
        // Cached rule into memory pool
        final String filename = MessageFormat.format(VPath.SERVER.INTERNAL_RULE, file);
        final ConcurrentMap<String, JsonObject> data = CC_RULE.store();
        if (data.containsKey(filename)) {
            LOGGER.debug(MESSAGE.Ruler.RULE_CACHED_FILE, filename);
        } else {
            LOGGER.debug(MESSAGE.Ruler.RULE_FILE, filename);
        }
        if (Ut.ioExist(filename)) {
            return CC_RULE.pick(() -> Ut.ioYaml(filename), filename);
        } else {
            return null;
        }
    }
}
