package io.vertx.mod.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.mod.workflow.atom.runtime.WRequest;
import io.vertx.mod.workflow.atom.runtime.WTransition;
import io.vertx.mod.workflow.uca.central.AbstractTransfer;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementStay extends AbstractTransfer implements Movement {
    @Override
    public Future<WTransition> moveAsync(final WRequest request) {
        final WTransition wTransition = this.createTransition(request);
        /*
         * For new workflow here, when you trigger `Stay` component
         * Following internal variables must contain value in WTransition
         *
         * 1. The from ( Task ) in WTransition
         * 2. The to   ( WTask ) must be null because of `Stay`
         * 3. The instance ( ProcessInstance ) must not be null
         * 4. The move ( WMove must contain value.
         *
         * The distinguish code logical is not needed in new version because
         * the `start()` method will bind the task by `taskId` here.
         *
         * 「Node」
         * When you stay at `e.start`, the workflow has not been started
         * In this kind of situation: here should return to default transition
         * In common situation, the will not happen because the `Saving` button
         * to call /api/up/flow/saving will be valid after `e.start` node, it means
         * that the original else code logical is invalid.
         */
        return this.beforeAsync(request, wTransition)
            .compose(normalized -> Ux.future(wTransition));
        // Old Code
        //            .compose(normalized -> {
        //            /* Here normalized/request shared the same reference */
        //            if (wTransition.isStarted()) {
        //                // Started
        //                final ProcessInstance instance = wTransition.instance();
        //                final Io<Task> ioTask = Io.ioTask();
        //                return ioTask.child(instance.getId())
        //                    /* Task Bind */
        //                    //.compose(task -> Ux.future(wTransition.from(task)))
        //                    .compose(nil -> Ux.future(wTransition));
        //            } else {
        //                /*
        //                 *  When you stay at `e.start`, the workflow has not been started
        //                 *  In this kind of situation: here should return to default process
        //                 *  Not Started -> null
        //                 */
        //                return Ux.future(wTransition);
        //            }
        //        });
    }
}
