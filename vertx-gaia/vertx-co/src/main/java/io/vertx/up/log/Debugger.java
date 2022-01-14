package io.vertx.up.log;

import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

/**
 * # 「Co」Zero Extension for Debugger to process logs
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Debugger {
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);
    private static final JsonObject JSON_DEBUG = new JsonObject();
    private static final String KEY_WEB_URI_DETECTING = "web.uri.detecting";
    private static final String KEY_JOB_BOOTING = "job.booting";
    private static final String KEY_PASSWORD_HIDDEN = "password.hidden";
    private static final String KEY_STACK_TRACING = "stack.tracing";
    private static final String KEY_JOOQ_CONDITION = "jooq.condition";
    private static final String KEY_EXCEL_RANGING = "excel.ranging";
    private static final String KEY_UI_CACHE = "ui.cache";

    static {
        final JsonObject configuration = VISITOR.read();
        if (configuration.containsKey("debug")) {
            JSON_DEBUG.mergeIn(Ut.sureJObject(configuration.getJsonObject("debug")));
        }
    }

    private Debugger() {
    }

    /*
     * Default false
     * If no configuration, the value is true
     */
    public static boolean onJooqCondition() {
        return isEnabled(KEY_JOOQ_CONDITION);
    }

    public static boolean onExcelRanging() {
        return isEnabled(KEY_EXCEL_RANGING);
    }

    public static boolean onStackTracing() {
        return isEnabled(KEY_STACK_TRACING);
    }

    public static boolean onJobBooting() {
        return isEnabled(KEY_JOB_BOOTING);
    }

    public static boolean onUiCache() {
        return isEnabled(KEY_UI_CACHE);
    }

    public static boolean onPasswordHidden() {
        return isEnabled(KEY_PASSWORD_HIDDEN);
    }

    /*
     * Default false
     * If no configuration, the value is false
     */
    public static boolean onWebUriDetect() {
        return isDisabled(KEY_WEB_URI_DETECTING);
    }


    /*
     * Default is false, when true, it' ok
     */
    public static boolean isDisabled(final String key) {
        return JSON_DEBUG.getBoolean(key, Boolean.TRUE);
    }

    public static boolean isEnabled(final String key) {
        return JSON_DEBUG.getBoolean(key, Boolean.FALSE);
    }
}
