package io.vertx.mod.ambient.cv.em;

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
     * Model Tube, in this kind of situation, it will do as following:
     * 1) Compare the field of HAtom
     *  - RULE_IDENTIFIER
     *  - RULE_FIELD
     * 2) Execute the expression checking
     */
    ATOM,

    /*
     * Workflow Tube, in this kind of situation, it will do as following:
     * 1) Compare the workflow field
     *  - TASK_KEY
     *  - PHASE
     * 2) Execute the expression checking
     *
     * Workflow Field:
     * - RULE_IDENTIFIER
     * - RULE_FIELD
     * - DEFINITION_KEY
     * - TASK_KEY
     */
    PHASE,

    /*
     * Expression Tube, in this kind of situation, it will do as following:
     * 1) Execute the expression checking directly
     */
    EXPRESSION,

    APPROVAL,       // Approval Workflow
    ATTACHMENT,     // Attachment Workflow

    NOTIFY,             // Notification Tube
    SCHEDULER,          // Scheduler Task Tube
    JOB,                // Job Tube
}
