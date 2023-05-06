package io.vertx.up.uca.job.center;

import io.horizon.eon.em.scheduler.JobType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface CACHE {
    ConcurrentMap<JobType, Agha> AGHAS = new ConcurrentHashMap<>() {
        {
            this.put(JobType.FIXED, new FixedAgha());
            this.put(JobType.ONCE, new OnceAgha());
            this.put(JobType.FORMULA, new FormulaAgha());
        }
    };
}


