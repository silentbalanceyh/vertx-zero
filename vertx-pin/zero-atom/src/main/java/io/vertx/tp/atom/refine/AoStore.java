package io.vertx.tp.atom.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.init.AoPin;
import io.vertx.tp.atom.modeling.config.AoConfig;
import io.vertx.tp.optic.modeling.JsonModel;
import io.vertx.tp.optic.modeling.JsonSchema;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.heart.EmptyStreamException;
import io.vertx.up.experiment.mixture.HApp;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.Objects;

class AoStore {
    private static final String PATH_EXCEL = "runtime/excel/";
    private static final String PATH_JSON = "runtime/json/";
    private static final String PATH_ADJUSTER = "runtime/adjuster/config.json";
    private static final String PATH_MODELING = "runtime/adjuster/modeling";

    private static final AoConfig CONFIG = AoPin.getConfig();

    static String namespace(final String appName) {
        final String prefix = CONFIG.getNamespace();
        if (Ut.isNil(prefix)) {
            // Default namespace
            return HApp.ns(appName);
        } else {
            // Configured namespace
            return MessageFormat.format(prefix, appName);
        }
    }

    static String defineExcel() {
        final String excel = CONFIG.getDefineExcel();
        return Ut.isNil(excel) ? PATH_EXCEL : excel;
    }

    static String defineJson() {
        final String json = CONFIG.getDefineJson();
        return Ut.isNil(json) ? PATH_JSON : json;
    }

    static JsonObject configAdjuster() {
        String adjuster = CONFIG.getConfigAdjuster();
        if (Ut.isNil(adjuster)) {
            adjuster = PATH_ADJUSTER;
        }
        return Ut.ioJObject(adjuster);
    }

    static JsonObject configModeling(final String filename) {
        String modeling = CONFIG.getConfigModeling();
        if (Ut.isNil(modeling)) {
            modeling = PATH_MODELING;
        }
        /*
         * Read critical path
         */
        final String name;
        if (modeling.endsWith("/")) {
            name = modeling + filename + Strings.DOT + FileSuffix.JSON;
        } else {
            name = modeling + "/" + filename + Strings.DOT + FileSuffix.JSON;
        }
        /*
         * Adjustment Processing
         */
        try {
            return Ut.ioJObject(name);
        } catch (final EmptyStreamException ex) {
            return new JsonObject();
        }
    }

    static boolean isDebug() {
        final Boolean debug = CONFIG.getSqlDebug();
        if (Objects.isNull(debug)) {
            return Boolean.FALSE;
        } else {
            return debug;
        }
    }

    static Class<?> clazzPin() {
        return CONFIG.getImplPin();
    }

    static Class<?> clazzSchema() {
        Class<?> clazz = CONFIG.getImplSchema();
        if (Objects.isNull(clazz)) {
            /*
             * Default
             */
            clazz = JsonSchema.class;
        }
        return clazz;
    }

    static Class<?> clazzModel() {
        Class<?> clazz = CONFIG.getImplModel();
        if (Objects.isNull(clazz)) {
            /*
             * Default
             */
            clazz = JsonModel.class;
        }
        return clazz;
    }

    static Class<?> clazzSwitcher() {
        return CONFIG.getImplSwitcher();
    }
}
