package io.vertx.tp.ambient.init;

import cn.vertxup.ambient.service.file.DocBStub;
import cn.vertxup.ambient.service.file.DocBuilder;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.atom.AtConfig;
import io.vertx.tp.ambient.cv.AtFolder;
import io.vertx.tp.ambient.refine.At;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.di.DiPlugin;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

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
     * Zero standard logger of {@link io.vertx.up.log.Annal} instance.
     */
    private static final Annal LOGGER = Annal.get(AtConfiguration.class);

    private static final DiPlugin PLUGIN = DiPlugin.create(AtConfiguration.class);
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
            At.infoInit(LOGGER, "At Json Data: {0}", configData.encode());
            CONFIG = Ut.deserialize(configData, AtConfig.class);
            At.infoInit(LOGGER, "At Configuration: {0}", CONFIG.toString());
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

    static Future<Boolean> initDoc(final String appId){
        final boolean fileIs = Ut.isNil(CONFIG.getFileIntegration());
        if(!fileIs){
            At.infoInit(LOGGER, "Document Platform Disabled !!");
        }
        // 此处提前调用 initialize 方法，此方法保证无副作用的多次调用即可
        final DocBStub docStub = PLUGIN.createComponent(DocBuilder.class);
        return docStub.initialize(appId, CONFIG.getFileIntegration()).compose(initialized -> {
            At.infoInit(LOGGER, "AppId = {0}, Directory Size = {1}", appId, String.valueOf(initialized.size()));
            return Ux.futureT();
        });
    }
}
