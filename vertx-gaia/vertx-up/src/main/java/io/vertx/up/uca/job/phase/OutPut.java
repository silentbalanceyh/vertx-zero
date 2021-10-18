package io.vertx.up.uca.job.phase;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.up.atom.Refer;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.Info;
import io.vertx.up.exception.WebException;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.job.plugin.JobOutcome;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

class OutPut {
    private static final Annal LOGGER = Annal.get(OutPut.class);
    private transient final Vertx vertx;
    private transient final Refer assist = new Refer();

    OutPut(final Vertx vertx) {
        this.vertx = vertx;
    }

    OutPut bind(final Refer assist) {
        if (Objects.nonNull(assist)) {
            this.assist.add(assist.get());
        }
        return this;
    }

    Future<Envelop> outcomeAsync(final Envelop envelop, final Mission mission) {
        if (envelop.valid()) {
            /*
             * Get JobOutcome
             */
            final JobOutcome outcome = Element.outcome(mission);
            if (Objects.isNull(outcome)) {
                /*
                 * Directly
                 */
                Element.onceLog(mission, () -> LOGGER.info(Info.PHASE_4TH_JOB, mission.getCode()));

                return Future.succeededFuture(envelop);
            } else {
                /*
                 * JobOutcome processing here
                 * Contract for vertx/mission
                 */
                LOGGER.info(Info.JOB_COMPONENT_SELECTED, "JobOutcome", outcome.getClass().getName());
                Ut.contract(outcome, Vertx.class, this.vertx);
                Ut.contract(outcome, Mission.class, mission);

                Element.onceLog(mission, () -> LOGGER.info(Info.PHASE_4TH_JOB_ASYNC, mission.getCode(), outcome.getClass().getName()));
                return outcome.afterAsync(envelop);
            }
        } else {
            Element.onceLog(mission, () -> LOGGER.info(Info.PHASE_ERROR, mission.getCode(), envelop.error().getClass().getName()));
            final WebException error = envelop.error();
            /*
             * For spec debug here, this code is very important
             */
            error.printStackTrace();
            return Ux.future(envelop);
        }
    }

    Future<Envelop> outputAsync(final Envelop envelop, final Mission mission) {
        if (envelop.valid()) {
            /*
             * Get outcome address
             */
            final String address = mission.getOutcomeAddress();
            if (Ut.isNil(address)) {
                /*
                 * Directly
                 */
                Element.onceLog(mission,
                    () -> LOGGER.info(Info.PHASE_5TH_JOB, mission.getCode()));
                return Future.succeededFuture(envelop);
            } else {
                /*
                 * Event bus provide output and then it will execute
                 */
                LOGGER.info(Info.JOB_ADDRESS_EVENT_BUS, "Outcome", address);
                final Promise<Envelop> output = Promise.promise();
                final EventBus eventBus = this.vertx.eventBus();
                Element.onceLog(mission,
                    () -> LOGGER.info(Info.PHASE_5TH_JOB_ASYNC, mission.getCode(), address));
                eventBus.<Envelop>request(address, envelop, Ux.Opt.on().delivery(), handler -> {
                    if (handler.succeeded()) {
                        output.complete(handler.result().body());
                    } else {
                        output.complete(Envelop.failure(handler.cause()));
                    }
                });
                return output.future();
            }
        } else {
            Element.onceLog(mission,
                () -> LOGGER.info(Info.PHASE_ERROR, mission.getCode(),
                    envelop.error().getClass().getName()));

            return Ux.future(envelop);
        }
    }
}
