package io.vertx.up.uca.job;

import io.vertx.core.Vertx;
import io.vertx.up.annotations.Contract;
import io.vertx.up.uca.job.store.JobPin;
import io.vertx.up.uca.job.store.JobStore;

public abstract class AbstractMission {
    private final transient JobStore store = JobPin.getStore();
    @Contract
    private transient Vertx vertx;

    protected Vertx vertx() {
        return vertx;
    }

    protected JobStore store() {
        return store;
    }
}
