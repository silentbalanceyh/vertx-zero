package io.vertx.up.uca.yaml;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.heart.EmptyStreamException;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.uca.cache.Cd;
import io.vertx.up.util.Ut;

public class ZeroTool {
    static final Cc<String, JsonObject> CC_STORAGE = Cc.open();

    public static String produce(final String key) {
        if (null == key) {
            return "vertx" + Strings.DOT + FileSuffix.YML;
        } else {
            return "vertx" + Strings.DASH + key +
                Strings.DOT + FileSuffix.YML;
        }
    }

    static JsonObject read(final String key, final boolean extension) {
        // Read the original configuration
        final JsonObject original = readDirect(produce(key));
        final JsonObject merged = new JsonObject();
        if (extension) {
            // Read the internal configuration instead
            final JsonObject internal = readDirect(Values.CONFIG_INTERNAL_FILE + produce(key));
            if (null != internal) {
                merged.mergeIn(internal, true);
            }
        }
        if (null != original) {
            merged.mergeIn(original, true);
        }
        return merged;
    }

    private static JsonObject readDirect(final String filename) {
        // Fix Docker issue
        final Cd<String, JsonObject> dataRef = CC_STORAGE.store();
        if (dataRef.is(filename)) {
            return dataRef.data(filename);
        } else {
            // Fix issue of deployment
            /*
             * For direct read configuration file, it must be successful or empty
             * Maybe the file is missing here
             */
            final JsonObject data = new JsonObject();
            try {
                final JsonObject yamlData = Ut.ioYaml(filename);
                if (Ut.notNil(yamlData)) {
                    data.mergeIn(yamlData);
                }
            } catch (final EmptyStreamException ex) {
                // LOGGER.warn(ex.getMessage());
                // Here do nothing to avoid useless log out
                // ex.printStackTrace();
            }
            if (!data.isEmpty()) {
                dataRef.data(filename, data);
            }
            return data;
        }
    }
}
