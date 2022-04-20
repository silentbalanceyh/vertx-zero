package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.MetaInstance;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.tp.workflow.plugin.activity.ActivityTabb;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.uca.sectio.Around;
import io.vertx.up.uca.sectio.Aspect;
import io.vertx.up.uca.sectio.AspectConfig;

import java.util.List;
import java.util.Objects;

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

    Future<WRequest> beforeAsync(final WRequest request, final WMove move) {
        // Build Aspect Component
        final Aspect aspect = this.aspect(move);
        return aspect.wrapJBefore(Around.TYPE_ALL.toArray(new ChangeFlag[0]))
            .apply(request.request())
            .compose(request::future);
    }

    Future<WRecord> afterAsync(final WRecord record, final WMove move) {
        // Build Aspect Component
        final Aspect aspect = this.aspect(move);
        return aspect.wrapJAfter(Around.TYPE_ALL.toArray(new ChangeFlag[0]))
            .apply(record.data())
            .compose(record::dataAfter);
    }

    private Aspect aspect(final WMove move) {
        final AspectConfig aspectConfig;
        if (Objects.isNull(move)) {
            // DEFAULT
            aspectConfig = AspectConfig.create();
            aspectConfig.nameAfter().add(ActivityTabb.class);
        } else {
            // Configured
            aspectConfig = move.configAop();
            // Because default will add here
            final List<Class<?>> afterList = aspectConfig.nameAfter();
            if (!afterList.contains(ActivityTabb.class)) {
                afterList.add(ActivityTabb.class);
            }
        }
        return Aspect.create(aspectConfig);
    }
}
