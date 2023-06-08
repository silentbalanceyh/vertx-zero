package io.vertx.mod.atom.refine;

import io.horizon.eon.VPath;
import io.horizon.eon.VString;
import io.horizon.exception.internal.EmptyIoException;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.init.AoPin;
import io.vertx.mod.atom.modeling.builtin.DataModel;
import io.vertx.mod.atom.modeling.builtin.DataSchema;
import io.vertx.up.util.Ut;

import java.util.Objects;

;

class AoStore {
    private static final String PATH_EXCEL = "runtime/excel/";
    private static final String PATH_JSON = "runtime/json/";
    private static final String PATH_ADJUSTER = "runtime/adjuster/config.json";
    private static final String PATH_MODELING = "runtime/adjuster/modeling";

    static String namespace(final String appName) {
        final String prefix = AoPin.getConfig().getNamespace();
        if (Ut.isNil(prefix)) {
            // Default namespace
            return Ut.nsApp(appName);
        } else {
            // Configured namespace
            return Ut.fromMessage(prefix, appName);
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
            name = modeling + filename + VString.DOT + VPath.SUFFIX.JSON;
        } else {
            name = modeling + "/" + filename + VString.DOT + VPath.SUFFIX.JSON;
        }
        /*
         * Adjustment Processing
         */
        try {
            return Ut.ioJObject(name);
        } catch (final EmptyIoException ex) {
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
