package io.vertx.up.unity;

import io.vertx.core.Future;
import io.vertx.tp.plugin.job.JobClient;
import io.vertx.tp.plugin.job.JobInfix;
import io.vertx.tp.plugin.job.JobPool;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

public class UxJob {
    private static final Annal LOGGER = Annal.get(UxJob.class);
    private transient final JobClient client;

    UxJob() {
        this.client = JobInfix.getClient();
    }

    // Start job
    public Future<Boolean> start(final String code) {
        return Fn.thenGeneric(future -> this.client.start(code, res -> {
            LOGGER.info(Info.JOB_START, code, res.result());
            future.complete(Boolean.TRUE);
        }));
    }

    // Stop job
    public Future<Boolean> stop(final String code) {
        final long timeId = JobPool.timeId(code);
        return Fn.thenGeneric(future -> this.client.stop(timeId,
                res -> {
                    LOGGER.info(Info.JOB_STOP, code, timeId);
                    future.complete(Boolean.TRUE);
                }));
    }

    // Resume job
    public Future<Boolean> resume(final String code) {
        final long timeId = JobPool.timeId(code);
        return Fn.thenGeneric(future -> this.client.resume(timeId,
                res -> {
                    LOGGER.info(Info.JOB_RESUME, code, timeId);
                    future.complete(Boolean.TRUE);
                }));
    }
}
