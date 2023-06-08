package io.vertx.up.util;

import io.horizon.uca.cache.Cc;
import io.horizon.util.HUt;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;

/**
 * Connect to vertx config to getNull options
 * From filename to ConfigStoreOptions
 */
final class Store {

    private static final Cc<String, ConfigStoreOptions> CC_STORE = Cc.open();

    /**
     * Return yaml
     */

    private Store() {
    }

    /**
     * Return json
     *
     * @param filename input filename
     *
     * @return Stored
     */
    static ConfigStoreOptions getJson(final String filename) {
        return Fn.failOr(() -> {
            final JsonObject data = HUt.ioJObject(filename);
            return CC_STORE.pick(() -> new ConfigStoreOptions()
                .setType(StoreType.JSON.key())
                .setConfig(data), filename);
/*            return Fn.getJvm(() -> Fn.po?l(Storage.STORE, filename,
                        () -> new ConfigStoreOptions()
                            .setType(SessionType.JSON.key())
                            .setConfig(data))
                , data);*/
        }, filename);
    }

    /**
     * Return yaml
     *
     * @param filename input filename
     *
     * @return Stored
     */
    static ConfigStoreOptions getYaml(final String filename) {
        return getFile(filename, StoreFormat.YAML);
    }

    /**
     * Return properties
     *
     * @param filename input filename
     *
     * @return Stored
     */
    static ConfigStoreOptions getProp(final String filename) {
        return getFile(filename, StoreFormat.PROP);
    }

    private static ConfigStoreOptions getFile(final String filename,
                                              final StoreFormat format) {
        return Fn.failOr(() -> {
            final JsonObject config = new JsonObject()
                .put(StoreConfig.PATH.key(), HUt.ioPath(filename));
            return CC_STORE.pick(() -> new ConfigStoreOptions()
                .setType(StoreType.FILE.key())
                .setFormat(format.key())
                .setConfig(config), filename);
/*            return Fn.po?l(Storage.STORE, filename,
                () -> new ConfigStoreOptions()
                    .setType(SessionType.FILE.key())
                    .setFormat(format.key())
                    .setConfig(config));*/
        }, filename, format);
    }
}
