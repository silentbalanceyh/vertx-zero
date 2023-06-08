package io.vertx.up.uca.job.phase;

import io.horizon.atom.common.Refer;
import io.horizon.eon.VMessage;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.commune.Envelop;
import io.vertx.up.uca.job.plugin.JobIncome;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

class Input {

    private static final Annal LOGGER = Annal.get(Input.class);

    private transient final Vertx vertx;
    private transient final Refer underway = new Refer();

    Input(final Vertx vertx) {
        this.vertx = vertx;
    }

    Refer underway() {
        return this.underway;
    }

    Future<Envelop> inputAsync(final Mission mission) {
        /*
         * Get income address
         * 1) If there configured income address, it means that there are some inputs came from
         *     'incomeAddress' ( For feature usage )
         * 2) No incomeAddress configured is often used for the job.
         * */
        final String address = mission.getIncomeAddress();
        if (Ut.isNil(address)) {
            /*
             * Event bus did not provide any input here
             */
            Element.onceLog(mission, () -> LOGGER.info(VMessage.Job.PHASE.PHASE_1ST_JOB, mission.getCode()));

            return Future.succeededFuture(Envelop.okJson());
        } else {
            /*
             * Event bus provide input and then it will pass to @On
             */
            LOGGER.info(VMessage.Job.PHASE.UCA_EVENT_BUS, "KIncome", address);
            final Promise<Envelop> input = Promise.promise();
            final EventBus eventBus = this.vertx.eventBus();
            eventBus.<Envelop>consumer(address, handler -> {

                Element.onceLog(mission, () -> LOGGER.info(VMessage.Job.PHASE.PHASE_1ST_JOB_ASYNC, mission.getCode(), address));

                final Envelop envelop = handler.body();
                if (Objects.isNull(envelop)) {
                    /*
                     * Success
                     */
                    input.complete(Envelop.ok());
                } else {
                    /*
                     * Failure
                     */
                    input.complete(envelop);
                }
            }).completionHandler(item -> {
                /*
                 * This handler will cause finally for future
                 * If no data came from address
                 */
                final Object result = item.result();
                if (Objects.isNull(result)) {
                    input.complete(Envelop.ok());
                } else {
                    input.complete(Envelop.success(result));
                }
            });
            return input.future();
        }
    }

    Future<Envelop> incomeAsync(final Envelop envelop, final Mission mission) {
        if (envelop.valid()) {
            /*
             * Get JobIncome
             */
            final JobIncome income = Element.income(mission);
            if (Objects.isNull(income)) {
                /*
                 * Directly
                 */
                Element.onceLog(mission, () -> LOGGER.info(VMessage.Job.PHASE.PHASE_2ND_JOB, mission.getCode()));
                return Future.succeededFuture(envelop);
            } else {
                /*
                 * JobIncome processing here
                 * Contract for vertx/mission
                 */
                LOGGER.info(VMessage.Job.PHASE.UCA_COMPONENT, "JobIncome", income.getClass().getName());
                /*
                 * JobIncome must define
                 * - Vertx reference
                 * - Mission reference
                 */
                Ut.contract(income, Vertx.class, this.vertx);
                Ut.contract(income, Mission.class, mission);
                /*
                 * Here we could calculate directory
                 */
                Element.onceLog(mission, () -> LOGGER.info(VMessage.Job.PHASE.PHASE_2ND_JOB_ASYNC, mission.getCode(), income.getClass().getName()));

                return income.underway().compose(refer -> {
                    /*
                     * Here provide extension for JobIncome
                     * 1 - You can do some operations in JobIncome to calculate underway data such as
                     *     dictionary data here.
                     * 2 - Also you can put some assist data into `Refer`, this `Refer` will be used
                     *     by major code logical instead of `re-calculate` the data again.
                     * 3 - For performance design, this structure could be chain passed in:
                     *     KIncome -> Job ( Channel ) -> Outcome
                     *
                     * Critical:  It's only supported by `Actor/Job` structure instead of `Api` passive
                     *     mode in Http Request / Response. it means that Api could not support this feature.
                     */
                    this.underway.add(refer.get());
                    return income.beforeAsync(envelop);
                });
            }
        } else {
            Element.onceLog(mission, () -> LOGGER.info(VMessage.Job.PHASE.ERROR_TERMINAL, mission.getCode(), envelop.error().getClass().getName()));
            return Ux.future(envelop);
        }
    }
}
