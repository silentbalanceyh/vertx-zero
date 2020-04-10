package io.vertx.up.uca.job.center;

import io.vertx.up.eon.em.JobType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<JobType, Agha> AGHAS = new ConcurrentHashMap<JobType, Agha>() {
        {
            put(JobType.FIXED, new FixedAgha());
            put(JobType.ONCE, new OnceAgha());
            put(JobType.PLAN, new PlanAgha());
        }
    };
}


