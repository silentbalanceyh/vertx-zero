package io.vertx.up.uca.job.plan;

import io.horizon.eon.em.typed.PerMode;
import io.horizon.uca.cache.Cc;

import java.time.Instant;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * Calculation for the next runAt to update the timer
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface JobAt {

    static JobAt instance(final PerMode mode) {
        return Pool.CC_JOB_AT.pick(() -> {
            final Supplier<JobAt> jobAt = Pool.AT_MAP.get(mode);
            return jobAt.get();
        }, mode.name());
    }

    default Queue<Instant> analyze(final List<String> formula) {
        return this.analyze(formula, Instant.now());
    }

    Queue<Instant> analyze(List<String> formula, Instant started);

    String format();
}

interface Pool {

    Cc<String, JobAt> CC_JOB_AT = Cc.openThread();

    ConcurrentMap<PerMode, Supplier<JobAt>> AT_MAP = new ConcurrentHashMap<>() {
        {
            this.put(PerMode.D, JobDaily::new);
            this.put(PerMode.W, JobWeekly::new);
            this.put(PerMode.M, JobMonthly::new);
            this.put(PerMode.Q, JobQuarterly::new);
            this.put(PerMode.Y, JobYearly::new);
        }
    };
}
