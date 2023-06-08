package io.vertx.up.uca.job.phase;

import io.horizon.fn.Actuator;
import io.horizon.runtime.Runner;
import io.horizon.uca.cache.Cc;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.eon.em.EmJob;
import io.vertx.up.uca.job.plugin.JobIncome;
import io.vertx.up.uca.job.plugin.JobOutcome;
import io.vertx.up.util.Ut;

import java.util.Objects;

/*
 * Assist class to help Agha object to process income / outcome extraction
 */
class Element {

    private static final Cc<String, JobIncome> CC_INCOME = Cc.open();
    private static final Cc<String, JobOutcome> CC_OUTCOME = Cc.open();

    static JobIncome income(final Mission mission) {
        final Class<?> incomeCls = mission.getIncome();
        JobIncome income = null;
        if (Objects.nonNull(incomeCls) && Ut.isImplement(incomeCls, JobIncome.class)) {
            income = CC_INCOME.pick(() -> Ut.instance(incomeCls), mission.getCode());
            // income = Fn.po?l(Pool.INCOMES, mission.getCode(), () -> Ut.instance(incomeCls));
        }
        return income;
    }

    static JobOutcome outcome(final Mission mission) {
        final Class<?> outcomeCls = mission.getOutcome();
        JobOutcome outcome = null;
        if (Objects.nonNull(outcomeCls) && Ut.isImplement(outcomeCls, JobOutcome.class)) {
            outcome = CC_OUTCOME.pick(() -> Ut.instance(outcomeCls), mission.getCode());
            // outcome = Fn.po?l(Pool.OUTCOMES, mission.getCode(), () -> Ut.instance(outcomeCls));
        }
        return outcome;
    }

    static void onceLog(final Mission mission, final Actuator actuator) {
        if (EmJob.JobType.ONCE == mission.getType()) {
            Runner.run(actuator::execute, "once-logger-debug");
        }
    }
}
