package io.vertx.tp.atom.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.init.AoPin;
import io.vertx.tp.atom.modeling.builtin.DataModel;
import io.vertx.tp.atom.modeling.builtin.DataSchema;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.heart.EmptyStreamException;
import io.aeon.experiment.mixture.HApp;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.Objects;

class AoStore {
    private static final String PATH_EXCEL = "runtime/excel/";
    private static final String PATH_JSON = "runtime/json/";
    private static final String PATH_ADJUSTER = "runtime/adjuster/config.json";
    private static final String PATH_MODELING = "runtime/adjuster/modeling";

    static String namespace(final String appName) {
        final String prefix = AoPin.getConfig().getNamespace();
        if (Ut.isNil(prefix)) {
            // Default namespace
            return HApp.ns(appName);
        } else {
            // Configured namespace
            return MessageFormat.format(prefix, appName);
        }
    }

    static String defineExcel() {
        final String excel = AoPin.getConfig().getDefineExcel();
        return Ut.isNil(excel) ? PATH_EXCEL : excel;
    }

    static String defineJson() {
        final String json = AoPin.getConfig().getDefineJson();
        return Ut.isNil(json) ? PATH_JSON : json;
    }

    /*
     * Two Point for Null Pointer and EmptyStream Here
     */
    static JsonObject configAdjuster() {
        String adjuster = AoPin.getConfig().getConfigAdjuster();
        if (Ut.isNil(adjuster)) {
            adjuster = PATH_ADJUSTER;
        }
        // Check File Existing, If not, return empty adjuster directly
        if (Ut.ioExist(adjuster)) {
            return Ut.ioJObject(adjuster);
        }
        return new JsonObject();
    }

    static JsonObject configModeling(final String filename) {
        String modeling = AoPin.getConfig().getConfigModeling();
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
        final Boolean debug = AoPin.getConfig().getSqlDebug();
        if (Objects.isNull(debug)) {
            return Boolean.FALSE;
        } else {
            return debug;
        }
    }

    static Class<?> clazzPin() {
        return AoPin.getConfig().getImplPin();
    }

    static Class<?> clazzSchema() {
        Class<?> clazz = AoPin.getConfig().getImplSchema();
        if (Objects.isNull(clazz)) {
            /*
             * Default
             */
            clazz = DataSchema.class;
        }
        return clazz;
    }

    static Class<?> clazzModel() {
        Class<?> clazz = AoPin.getConfig().getImplModel();
        if (Objects.isNull(clazz)) {
            /*
             * Default
             */
            clazz = DataModel.class;
        }
        return clazz;
    }

    static Class<?> clazzSwitcher() {
        return AoPin.getConfig().getImplSwitcher();
    }
}
