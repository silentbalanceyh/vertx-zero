package io.vertx.up.log;

import io.vertx.core.json.JsonObject;
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
    private static final JsonObject JSON_DEBUG = new JsonObject();


    /*
     * Check whether generate the log information to describe the `uri` detecting workflow process:
     *
     * 1) Remove additional `/` from uri such as :              /api//test                  -> /api/test
     * 2) Append start `/` to each uri definition such as:      api/test                    -> /api/test
     * 3) Join @Path of class and method to valid part:         /api + user/test            -> /api/user/test
     *
     * This log will record normalized uri detecting logs to provide developer to enhance the JSR311
     * definition for current RESTful Api, when you debug in current framework, you can enable this
     * option to check whether the definition of @Path is invalid ( Warning Level ).
     *
     * Recommend on Prod: ( Keep Default )
     *      web.uri.detecting:      false
     */
    private static final String KEY_WEB_URI_DETECTING = "web.uri.detecting";


    /*
     * When you use zero scheduler system, the system will start ZeroScheduler to scan all the jobs:
     *
     * 1) Static Job of @Job
     * 2) Dynamic Job those are stored into I_JOB
     *
     * When you enable this logger, you can see different logs when Job executing such as
     *
     * Start -> Income -> Job Run -> Outcome -> Callback -> End ( 6 Phase )
     *
     * Recommend on Prod: ( Keep Default )
     *      job.booting:            false
     */
    private static final String KEY_JOB_BOOTING = "job.booting";


    /*
     * This option is small configuration for database password show on log to system admin.
     * If you enable this option, the system admin could not see the database password information from
     * backend logs.
     *
     * Recommend on Prod: ( Keep Default )
     *      password.hidden:        true
     */
    private static final String KEY_PASSWORD_HIDDEN = "password.hidden";


    /*
     * Whether you can see stack information in logs, when you write programming in code such as:
     *
     *      ex.printStackTrace();
     * The stack information will show in logs, but for running part, this trace information won't show
     * in logs, this option is for 「Development」to debug current system.
     *
     * Recommend on Prod: ( Keep Default )
     *      stack.tracing:          false
     */
    private static final String KEY_STACK_TRACING = "stack.tracing";


    /*
     * If you want to see jooq query condition of json format, you should enable this option, when you turn off
     * this log, the logs won't see the jooq condition, the condition is as following such as:
     *
     * ( Jooq -> Condition ) Parsed result is condition = (
     *    URI = '/api/up/flow/:key'
     *    and METHOD = 'GET'
     * ).
     *
     * If you turn on this log, above log will show in logs
     *
     * Recommend on Prod: ( Keep Default )
     *      jooq.condition:         false
     */
    private static final String KEY_JOOQ_CONDITION = "jooq.condition";


    /*
     * This option allow you to see the log details of excel scanning process, default turn off.
     *
     * Recommend on Prod: ( Keep Default )
     *      excel.ranging:          false
     */
    private static final String KEY_EXCEL_RANGING = "excel.ranging";


    /*
     * Whether turn-on ui configuration cache, this option is for `zero-ui` module of extension
     * only. When you are on 「Development」 environment, you can turn-off the ui cache for ui
     * configuration debugging.
     *
     * Recommend on Prod: ( Keep Default )
     *      ui.cache:               true
     */
    private static final String KEY_UI_CACHE = "ui.cache";


    /*
     * Whether turn-on the expression bind to see the variable bind in Playbook, it's important
     * to enable this option to check the variable binding process details.
     * Recommend on Prod: ( Keep Default )
     *      expression.bind:        true
     */
    private static final String KEY_EXPRESSION_BIND = "expression.bind";

    /*
     * Check if the authorized cache logs generated ( zero-rbac )
     * Recommend on Prod: ( Keep Default )
     *      authorized.cache:        false
     */
    private static final String KEY_AUTHORIZED_CACHE = "authorized.cache";

    private static final String KEY_ADMIT_CACHE = "admit.cache";

    static {
        final JsonObject configuration = VISITOR.read();
        if (configuration.containsKey("debug")) {
            JSON_DEBUG.mergeIn(Ut.valueJObject(configuration.getJsonObject("debug")));
        }
    }

    private Debugger() {
    }

    // ------------------------------ Default = true ---------------------------------
    // ( null = true )    ->   false
    public static boolean offPasswordHidden() {
        return isDisabled(KEY_PASSWORD_HIDDEN);
    }

    public static boolean offUiCache() {
        return isDisabled(KEY_UI_CACHE);
    }

    public static boolean offAdmitCache() {
        return isDisabled(KEY_ADMIT_CACHE);
    }


    // ------------------------------ Default = false ---------------------------------
    // ( null = false )    ->   true
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

    public static boolean onWebUriDetect() {
        return isEnabled(KEY_WEB_URI_DETECTING);
    }

    public static boolean onExpressionBind() {
        return isEnabled(KEY_EXPRESSION_BIND);
    }

    public static boolean onAuthorizedCache() {
        return isEnabled(KEY_AUTHORIZED_CACHE);
    }

    // ------------------------------ Public method for disabled/enabled ---------------------------------
    public static boolean isDisabled(final String key) {
        return JSON_DEBUG.getBoolean(key, Boolean.TRUE);
    }

    public static boolean isEnabled(final String key) {
        return JSON_DEBUG.getBoolean(key, Boolean.FALSE);
    }
}
