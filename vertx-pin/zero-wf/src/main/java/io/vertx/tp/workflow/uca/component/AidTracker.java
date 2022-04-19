package io.vertx.tp.workflow.uca.component;

import io.vertx.tp.workflow.atom.MetaInstance;

/**
 * Tracker for different component history record
 * This tracker implementation is based on X_ACTIVITY_RULE
 *
 * 1) The default logging will be put into camunda
 * 2) Tracker is for business activities
 *
 * Internal call Journal interface for `zero-ambient` activity generation instead.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AidTracker {

    private final transient MetaInstance metadata;

    /*
     * Here must be noted that the record type inner WRequest
     * 1) Activity of Workflow ( Moving )
     *    before - input record
     *    after  - generated record
     * 2) Activity of Saving / Cancel
     *    before - input record
     *    after  - finished record
     *
     * All notification must be based on `after` instead of before such as:
     * - Notification
     * - Workflow Moving
     * - Start Job
     * - Change Activity ( after + before )
     *
     * The whole workflow should be:
     * 1) Movement -> Transfer
     *                Transfer ( Generated ) -> Generator
     * 2) Movement -> Stay
     *
     * In Camunda Workflow Engine, here before and after should be configured on
     * WMove rule node such as
     * {
     *     "e.start": {
     *         "data": { },
     *         "aspect": {
     *              "plugin.component.before": [],
     *              "plugin.component.after": [],
     *              "plugin.component.job": [],
     *              "plugin.config": {
     *              }
     *         }
     *     }
     * }
     *
     * 1) beforeAsync -> Movement -> Transfer -> afterAsync
     * 2) beforeAsync -> Movement -> Transfer -> afterAsync ->
     *                               beforeAsync -> Generator -> afterAsync
     * 3) beforeAsync -> Movement -> Stay     -> afterAsync
     *
     * Actually you can ignore Movement because it's only for Camunda Workflow Moving
     * The rule of tracker is for objective:
     * 1) Impact Request Data
     * 2) Callback after the whole operations
     */
    AidTracker(final MetaInstance metadata) {
        this.metadata = metadata;
    }
}
