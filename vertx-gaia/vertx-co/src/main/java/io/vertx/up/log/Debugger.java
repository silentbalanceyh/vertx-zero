package io.vertx.up.log;

import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

/**
 * # 「Co」Zero Extension for Debugger to process logs
 *
 * @author <a href="http://www.origin-x.cn">lang</a>
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

    public static boolean isJqCondition() {
        return isFalse("jooq.condition");
    }

    public static boolean isJqPassword() {
        return isFalse("jooq.password");
    }

    public static boolean isExcelRange() {
        return isFalse("excel.range");
    }

    public static boolean isFalse(final String key) {
        return JSON_DEBUG.getBoolean(key, Boolean.FALSE);
    }

    public static boolean isTrue(final String key) {
        return JSON_DEBUG.getBoolean(key, Boolean.TRUE);
    }
}
