package io.vertx.up.unity;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.job.JobClient;
import io.vertx.tp.plugin.job.JobInfix;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

public class UxJob {
    private static final Annal LOGGER = Annal.get(UxJob.class);
    private transient final JobClient client;

    UxJob() {
        this.client = JobInfix.getClient();
    }

    // Start job
    public Future<Boolean> startAsync(final String code) {
        return Fn.thenGeneric(future -> this.client.startAsync(code, res -> {
            LOGGER.info(Info.JOB_START, code, res.result());
            future.complete(Boolean.TRUE);
        }));
    }

    // Stop job
    public Future<Boolean> stopAsync(final String code) {
        return Fn.thenGeneric(future -> this.client.stopAsync(code,
                res -> {
                    LOGGER.info(Info.JOB_STOP, code);
                    future.complete(Boolean.TRUE);
                }));
    }

    // Resume job
    public Future<Boolean> resumeAsync(final String code) {
        return Fn.thenGeneric(future -> this.client.resumeAsync(code,
                res -> {
                    LOGGER.info(Info.JOB_RESUME, code);
                    future.complete(Boolean.TRUE);
                }));
    }

    public Future<JsonObject> statusAsync(final String namespace) {
        return this.client.statusAsync(namespace);
    }
}
