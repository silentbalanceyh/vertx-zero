package io.vertx.tp.ui.init;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ui.atom.UiConfig;
import io.vertx.tp.ui.cv.UiFolder;
import io.vertx.tp.ui.refine.Ui;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.List;
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
        /* Original `mapping` read */
        final JsonObject mapping = config.getMapping();
        final JsonObject combine = new JsonObject();
        if (Ut.notNil(mapping)) {
            combine.mergeIn(mapping, true);
        }
        /* Re-Calculate `mapping` configuration */
        final String configPath = config.getDefinition();
        final List<String> files = Ut.ioFiles(configPath, Strings.DOT + FileSuffix.JSON);
        files.forEach(file -> {
            final String identifier = file.replace(Strings.DOT + FileSuffix.JSON, Strings.EMPTY);
            combine.put(identifier, file);
        });
        /* Combine File Processing */
        combine.fieldNames().forEach(fileKey -> {
            final String file = combine.getString(fileKey);
            final String filePath = configPath + '/' + file;
            /*
             * Read column from configuration path
             */
            final JsonArray columns = Ut.ioJArray(filePath);
            if (Objects.nonNull(columns) && !columns.isEmpty()) {
                COLUMN_MAP.put(fileKey, columns);
            }
        });
        /* Re-Write mapping */
        CONFIG.setMapping(combine);
    }

    static JsonArray getColumn(final String key) {
        return COLUMN_MAP.getOrDefault(key, new JsonArray());
    }

    static JsonArray getOp() {
        return Ut.sureJArray(CONFIG.getOp());
    }

    static JsonArray attributes(final String key) {
        final JsonArray columns = getColumn(key);
        if (Ut.notNil(columns)) {
            /*
             * column transfer to
             */
            final ConcurrentMap<String, String> attributeMap = new ConcurrentHashMap<>();
            Ut.itJArray(columns).forEach(json -> {
                if (json.containsKey(KName.METADATA)) {
                    final String unparsed = json.getString(KName.METADATA);
                    final String[] parsed = unparsed.split(",");
                    if (2 < parsed.length) {
                        final String name = parsed[Values.IDX];
                        final String alias = parsed[Values.ONE];
                        if (!Ut.isNilOr(name, alias)) {
                            attributeMap.put(name, alias);
                        }
                    }
                } else {
                    final String name = json.getString("dataIndex");
                    final String alias = json.getString("title");
                    if (!Ut.isNilOr(name, alias)) {
                        attributeMap.put(name, alias);
                    }
                }
            });
            final JsonObject defaults = CONFIG.getAttributes();
            Ut.<String>itJObject(defaults, (alias, name) -> {
                if (!attributeMap.containsKey(name)) {
                    attributeMap.put(name, alias);
                }
            });
            /*
             * Converted to JsonArray
             */
            final JsonArray attributes = new JsonArray();
            attributeMap.forEach((name, alias) -> {
                final JsonObject attribute = new JsonObject();
                attribute.put(KName.NAME, name);
                attribute.put(KName.ALIAS, alias);
                attributes.add(attribute);
            });
            return attributes;
        } else {
            return new JsonArray();
        }
    }
}
