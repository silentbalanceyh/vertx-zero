package io.vertx.up.log;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.runtime.develop.DiagnosisOption;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

/**
 * # 「Co」Zero Extension for Debugger to process logs
 *
 * This configuration stored into `.yml` file such as:
 *
 * Default configuration is as following:
 *
 * // <pre><code class="yaml">
 * debug:
 *     ui.cache:            true        PROD
 *     password.hidden:     true        PROD
 *
 *     web.uri.detecting:   false       PROD
 *     job.booting:         false       PROD
 *     stack.tracing:       false       PROD
 *     jooq.condition:      false       PROD
 *     excel.ranging:       false       PROD
 *     expression.bind:     false       PROD
 *
 * // </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Debugger {
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);
    private static final DiagnosisOption OPTION;

    static {
        final JsonObject configuration = VISITOR.read();
        if (configuration.containsKey(KName.DEVELOPMENT)) {
            final JsonObject envJ = Ut.visitJObject(configuration, KName.DEVELOPMENT, KName.ENV);
            OPTION = Ut.deserialize(envJ, DiagnosisOption.class);
        } else {
            OPTION = new DiagnosisOption();
        }
    }

    private Debugger() {
    }

    // ------------------------------ 缓存
    // Z_CACHE_UI
    public static boolean cacheUi() {
        return OPTION.getCacheUi();
    }

    // Z_CACHE_ADMIT
    public static boolean cacheAdmit() {
        return OPTION.getCacheAdmit();
    }

    // ------------------------------ 开发

    // Z_DEV_AUTHORIZED
    public static boolean devAuthorized() {
        return OPTION.getDevAuthorized();
    }

    // Z_DEV_EXPR_BIND
    public static boolean devExprBind() {
        return OPTION.getDevExprBind();
    }

    // Z_DEV_JOOQ_COND
    public static boolean devJooqCond() {
        return OPTION.getDevJooqCond();
    }

    // Z_DEV_EXCEL_RANGE
    public static boolean devExcelRange() {
        return OPTION.getDevExcelRange();
    }

    // Z_DEV_JVM_STACK
    public static boolean devJvmStack() {
        return OPTION.getDevJvmStack();
    }

    // Z_DEV_JOB_BOOT
    public static boolean devJobBoot() {
        return OPTION.getDevJobBoot();
    }

    // Z_DEV_WEB_URI
    public static boolean devWebUri() {
        return OPTION.getDevWebUri();
    }

    // Z_DEV_DAO_BIND
    public static boolean devDaoBind() {
        return OPTION.getDevDaoBind();
    }
}
