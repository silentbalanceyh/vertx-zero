package io.vertx.tp.ambient.init;

import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.atom.AtConfig;
import io.vertx.tp.ambient.cv.AtFolder;
import io.vertx.up.util.Ut;

import static io.vertx.tp.ambient.refine.At.LOG;

/**
 * ## 「Init」AtConfiguration
 *
 * ### 1. Intro
 *
 * This class if for configuration initialization that related to file:
 *
 * ```shell
 * // <pre><code>
 *    plugin/ambient/configuration.json
 * // </code></pre>
 * ```
 *
 * ### 2. Specification
 *
 * This specification is defined by Zero Extension framework to read the fixed configuration file. The file
 * data content may be as following:
 *
 * ```json
 * // <pre><code class="json">
 * {
 *     "supportSource": "Boolean, Whether support multi `Database` in current environment.",
 *     "initializer": "Extension component for `Init` interface.",
 *     "prerequisite": "Extension component for `Prerequisite` interface.",
 *     "loader": "Extension component for `Init` of data loader.( OOB Data ).",
 *     "dataFolder": "OOB data stored folder, the default value is `init/oob/`.",
 *     "fileStorage": "The storage mode here, default value is `FILE`.",
 *     "fileLanguage": "The configuration language information, default value is `cn`."
 * }
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class AtConfiguration {
    /**
     * Zero standard logger of {@link Annal} instance.
     */
    private static final Annal LOGGER = Annal.get(AtConfiguration.class);
    /**
     * The singleton instance of {@link io.vertx.tp.ambient.atom.AtConfig} to store configuration data.
     */
    private static AtConfig CONFIG = null;

    /**
     * The private constructor to let current class be Util only.
     */
    private AtConfiguration() {
    }

    /**
     * 「Booting」This method will be called when zero container booting up.
     */
    static void init() {
        /* Read definition of ambient configuration of default */
        if (null == CONFIG) {
            final JsonObject configData = Ut.ioJObject(AtFolder.CONFIG_FILE);
            LOG.Init.info(LOGGER, "At Json Data: {0}", configData.encode());
            CONFIG = Ut.deserialize(configData, AtConfig.class);
            LOG.Init.info(LOGGER, "At Configuration: {0}", CONFIG.toString());
        }
    }

    /**
     * Return to reference of {@link io.vertx.tp.ambient.atom.AtConfig} instance.
     *
     * @return {@link io.vertx.tp.ambient.atom.AtConfig} Deserialized application configuration data.
     */
    static AtConfig getConfig() {
        return CONFIG;
    }
}
