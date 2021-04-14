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
     */
    public static boolean onJooqCondition() {
        return isEnabled("jooq.condition");
    }

    public static boolean onJooqPassword() {
        return isEnabled("jooq.password");
    }

    public static boolean onExcelRange() {
        return isEnabled("excel.range");
    }

    public static boolean onJobBoot() {
        return isEnabled("job.boot");
    }

    public static boolean onStackTrace() {
        return isEnabled("stack.trace");
    }

    /*
     * Default true
     */
    public static boolean offUrlDetect() {
        return isDisabled("rest.url.detect");
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
