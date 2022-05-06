package io.vertx.tp.ambient.cv.em;

/**
 * Here are two critical fields:
 * - RULE_TPL: parameters are based on Tpl ( tpl, input ) -> parameters
 * - RULE_CONFIG: configuration information of each rule here.
 * - RULE_MESSAGE: message template of current rule for logging.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum TubeType {
    /*
     * Model Tube, it's for changing history activities, in this kind of situation, it will
     * check expression directly.
     *
     * Here are reference fields:
     * - RULE_IDENTIFIER
     * - RULE_FIELD
     */
    ATOM,

    /*
     * Workflow Tube, it's for camuda workflow activities, it will check expression directly
     * Here are reference fields:
     * - RULE_IDENTIFIER
     * - RULE_FIELD
     * - DEFINITION_KEY
     * - TASK_KEY
     */
    WORKFLOW,

    NOTIFY,             // Notification Tube
    SCHEDULER,          // Scheduler Task Tube
    JOB,                // Job Tube
}
