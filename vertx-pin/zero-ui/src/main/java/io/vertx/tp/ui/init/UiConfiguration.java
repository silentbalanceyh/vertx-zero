package io.vertx.tp.ui.init;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ui.atom.UiConfig;
import io.vertx.tp.ui.cv.UiFolder;
import io.vertx.tp.ui.refine.Ui;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Configuration class initialization
 * plugin/ui/configuration.json
 *
 */
class UiConfiguration {
    private static final Annal LOGGER = Annal.get(UiConfiguration.class);
    /*
     * Static configuration mapping for column, it could not be modified
     * once configured into the column mapping
     * column map stored fileKey = filename
     * Here `fileKey` will be configured in other place to be connected.
     * The file absolute path should be `definition` + `filename` to be calculated
     * the final result.
     * Such as `ColumnStub` in CRUD, this service will be called to read
     * different columns, it support two mode:
     * Static: read column information from file ( column map support )
     * Dynamic: read column information from database ( skip file )
     */
    private static final ConcurrentMap<String, JsonArray> COLUMN_MAP =
            new ConcurrentHashMap<>();
    private static UiConfig CONFIG = null;

    static void init() {
        if (null == CONFIG) {
            final JsonObject uiData = Ut.ioJObject(UiFolder.CONFIG_FILE);
            Ui.infoInit(LOGGER, "Ui Json Data: {0}", uiData.encode());
            CONFIG = Ut.deserialize(uiData, UiConfig.class);
            Ui.infoInit(LOGGER, "Ui Configuration: {0}", CONFIG.toString());
            /* Static Loading */
            initColumn(CONFIG);
            Ui.infoInit(LOGGER, "Ui Columns: Size = {0}", COLUMN_MAP.size());
        }
    }

    static UiConfig getConfig() {
        return CONFIG;
    }

    private static void initColumn(final UiConfig config) {
        final JsonObject mapping = config.getMapping();
        if (!Ut.isNil(mapping)) {
            final String configPath = config.getDefinition();
            mapping.fieldNames().forEach(fileKey -> {
                final String file = mapping.getString(fileKey);
                final String filePath = configPath + '/' + file;
                /*
                 * Read column from configuration path
                 */
                final JsonArray columns = Ut.ioJArray(filePath);
                if (Objects.nonNull(columns) && !columns.isEmpty()) {
                    COLUMN_MAP.put(fileKey, columns);
                }
            });
        }
    }

    static JsonArray getColumn(final String key) {
        return COLUMN_MAP.getOrDefault(key, new JsonArray());
    }
}
