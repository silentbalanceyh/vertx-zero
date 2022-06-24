package io.vertx.up.uca.job.plan;

import java.time.Instant;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class JobDaily extends AbstractJobAt {
    @Override
    protected Instant analyze(final String formula, final Instant instant) {
        return null;
    }

    @Override
    public String format() {
        return "HH:mm:ss.SSS";
    }
}
