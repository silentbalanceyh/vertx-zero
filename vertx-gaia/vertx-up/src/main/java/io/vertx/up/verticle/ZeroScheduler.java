package io.vertx.up.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Worker;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.JobType;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.job.center.Agha;
import io.vertx.up.uca.job.store.JobConfig;
import io.vertx.up.uca.job.store.JobPin;
import io.vertx.up.uca.job.store.JobStore;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

/**
 * Background worker of Zero framework, it's for schedule of background tasks here.
 * This scheduler is for task deployment, it should deploy all tasks
 * This worker must be SINGLE ( instances = 1 ) because multi worker with the same tasks may be
 * conflicts
 */
@Worker(instances = Values.SINGLE)
public class ZeroScheduler extends AbstractVerticle {

    private static final Annal LOGGER = Annal.get(ZeroScheduler.class);
    private static final JobStore STORE = JobPin.getStore();

    public ZeroScheduler() {
    }

    @Override
    public void start() {
        /* Whether contains JobConfig? */
        final JobConfig config = JobPin.getConfig();
        if (Objects.nonNull(config)) {
            /* Pick Up all Mission definition from system */
            final Set<Mission> missions = STORE.fetch();
            /* Whether there exist Mission definition */
            if (missions.isEmpty()) {
                LOGGER.info(Info.JOB_EMPTY);
            } else {
                LOGGER.info(Info.JOB_MONITOR, missions.size());
                /* Start each job here by different types */
                missions.forEach(this::start);
            }
        } else {
            LOGGER.info(Info.JOB_CONFIG_NULL);
        }
    }

    private void start(final Mission mission) {
        /*
         * Prepare for mission, it's verf important to bind mission object to Vertx
         * instead of bind(Vertx) method.
         */
        final Object reference = mission.getProxy();
        if (Objects.nonNull(reference)) {
            /*
             * Bind vertx
             */
            Ut.contract(reference, Vertx.class, this.vertx);
        }
        /*
         * Agha calling
         */
        final Agha agha = Agha.get(mission.getType());
        if (Objects.nonNull(agha)) {
            /*
             * Bind vertx
             */
            Ut.contract(agha, Vertx.class, this.vertx);
            /*
             * Invoke here to provide input
             */
            LOGGER.info(Info.JOB_AGHA_SELECTED, agha.getClass(), mission.getCode(), mission.getType());
            /*
             * If job type is ONCE, it's not started
             */
            if (JobType.ONCE != mission.getType()) {
                agha.begin(mission);
            }
        }
    }
}
