package io.vertx.up.uca.job.phase;

import io.vertx.up.atom.worker.Mission;
import io.vertx.up.eon.em.JobType;
import io.vertx.up.fn.Actuator;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.Runner;
import io.vertx.up.uca.job.plugin.JobIncome;
import io.vertx.up.uca.job.plugin.JobOutcome;
import io.vertx.up.util.Ut;

import java.util.Objects;

/*
 * Assist class to help Agha object to process income / outcome extraction
 */
class Element {

    static JobIncome income(final Mission mission) {
        final Class<?> incomeCls = mission.getIncome();
        JobIncome income = null;
        if (Objects.nonNull(incomeCls) && Ut.isImplement(incomeCls, JobIncome.class)) {
            income = Fn.pool(Pool.INCOMES, mission.getCode(), () -> Ut.instance(incomeCls));
        }
        return income;
    }

    static JobOutcome outcome(final Mission mission) {
        final Class<?> outcomeCls = mission.getOutcome();
        JobOutcome outcome = null;
        if (Objects.nonNull(outcomeCls) && Ut.isImplement(outcomeCls, JobOutcome.class)) {
            outcome = Fn.pool(Pool.OUTCOMES, mission.getCode(), () -> Ut.instance(outcomeCls));
        }
        return outcome;
    }

    static void onceLog(final Mission mission, final Actuator actuator) {
        if (JobType.ONCE == mission.getType()) {
            Runner.run(actuator::execute, "once-logger-debug");
        }
    }
}
