package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.configuration.MetaInstance;
import io.vertx.tp.workflow.atom.runtime.WRecord;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.atom.runtime.WTransition;
import io.vertx.tp.workflow.uca.central.AbstractMovement;
import io.vertx.tp.workflow.uca.modeling.Register;
import io.vertx.tp.workflow.uca.toolkit.URequest;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferStandard extends AbstractMovement implements Transfer {
    @Override
    public Future<WRecord> moveAsync(final WRequest request, final WTransition wTransition) {
        /*
         * Capture the next task for standard workflow
         * 1. Here camunda-workflow task has been finished
         * 2. Check condition in workflow engine
         * -- 2.1. Active task is not end
         * -- 2.2. Next task is user task
         * */
        final JsonObject requestJ = request.request();
        return this.inputAsync(requestJ, wTransition)
            .compose(normalized -> Ux.future(wTransition.moveTicket(normalized)))
            /* __move field data processing for next ( Modify WRequest ) */
            .compose(request::movement)
            /*
             * Entity / Extension Ticket Record Execution, ( Update )
             * Todo Updated with normalized
             * */
            .compose(normalized -> this.saveInternal(normalized, wTransition))
            .compose(request::record)
            .compose(record -> {
                /*
                 * Todo Generation Condition
                 * 1. Instance is not ended
                 * 2. Next task is not null
                 */
                if (wTransition.isRunning()) {
                    /*
                     * Here the `to` tasks is not empty,
                     * It means here are next steps to generate WTodo.
                     *
                     * The old comments:
                     * Create new WProcess based on process / task and move
                     *
                     * Here instance contains previous data such as:
                     * 1. Task
                     * 2. ConfigRunner is runConfig
                     * 3. ProcessInstance
                     *
                     * The WMove should be generated by
                     * 1. Previous task definition key
                     * 2. Data with
                     *
                     * Add new bind on MetaInstance to fix following issue:
                     * java.lang.NullPointerException
                     *      at java.base/java.util.Objects.requireNonNull(Objects.java:221)
                     *      at io.vertx.tp.workflow.uca.component.MoveOnUser.transferAsync(MoveOnUser.java:37)
                     * Here means the `to` attribute is not null, it should generate many `todo`
                     * Also the record in request should contain values.
                     */
                    final MoveOn move = MoveOn.instance(MoveOnGenerate.class);
                    move.bind(this.metadataIn());
                    return move.transferAsync(request, wTransition);
                } else {
                    return Ux.future(record);
                }
            })
            .compose(record -> this.afterAsync(record, wTransition));
    }

    private Future<WRecord> saveInternal(final JsonObject normalized, final WTransition wTransition) {
        /*
         *  1. Close current WTodo first, checking the status from wTransition
         *     - If End, put the data of closeAt/closeBy and status
         *     - If Not, do not modify the main ticket record status/phase etc.
         */
        final JsonObject closeJ = URequest.closeJ(normalized, wTransition);

        return this.saveAsync(closeJ, wTransition).compose(record -> {
            /*
             * 2. Processing for `record` field on entity records
             */
            /*
             * Double check for `insert record`
             * Here will execute twice on entity record instead of one
             * 1. Insert -> Move Update
             * 2. Update -> Move Update
             * The `status` should be previous status
             * - ADD -> Inserted Status
             * - UPDATE -> Original Stored Status
             */
            final TodoStatus status = record.status();
            JsonObject parameterRegister = normalized.copy();
            /*
             * Here are some background data under generation process.
             * GenerateComponent contains `rule` movement to apply the data such as `status` instead of
             * original record here
             */
            parameterRegister.mergeIn(record.data(), true);
            final MetaInstance metadataOut = MetaInstance.output(record, this.metadataIn());
            if (TodoStatus.PENDING == status) {
                /* Move Rules: moveRecord Calling */
                parameterRegister = wTransition.moveRecord(parameterRegister);
            }
            /*
             * Contains record modification, do update on record.
             */
            final Register register = Register.instance(parameterRegister);
            return register.saveAsync(parameterRegister, metadataOut).compose(nil -> Ux.future(record));
        });
    }
}
