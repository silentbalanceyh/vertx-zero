package io.vertx.up.uca.yaml;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.heart.EmptyStreamException;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

public class ZeroTool {
    private static final Annal LOGGER = Annal.get(ZeroTool.class);

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
        if (Storage.CONFIG.containsKey(filename)) {
            return Storage.CONFIG.get(filename);
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
                Storage.CONFIG.put(filename, data);
            }
            return data;
        }
    }
}
