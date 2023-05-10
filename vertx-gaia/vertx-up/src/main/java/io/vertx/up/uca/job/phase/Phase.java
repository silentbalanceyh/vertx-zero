package io.vertx.up.uca.job.phase;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.commune.Envelop;

/*
 * Major phase for code logical here
 */
public class Phase {

    private static final Cc<String, Phase> CC_PHASE = Cc.open();

    /* Dict */
    private transient Vertx vertx;
    private transient Mission mission;
    /* Phase */
    private transient Input input;
    private transient RunOn runOn;
    private transient OutPut output;

    private Phase() {
    }

    public static Phase start(final String name) {
        return CC_PHASE.pick(Phase::new, name); // Fn.po?l(Pool.PHASES, name, Phase::new);
    }

    public Phase bind(final Vertx vertx) {
        this.vertx = vertx;
        this.input = new Input(this.vertx);
        this.runOn = new RunOn(this.vertx);
        this.output = new OutPut(this.vertx);
        return this;
    }

    public Phase bind(final Mission mission) {
        this.mission = mission;
        return this;
    }

    /*
     * 1. Event Bus with Input
     */
    public Future<Envelop> inputAsync(final Mission mission) {
        return this.input.inputAsync(mission);
    }

    /*
     * 2. JobIncome here
     */
    public Future<Envelop> incomeAsync(final Envelop envelop) {
        return this.input.incomeAsync(envelop, this.mission);
    }

    /*
     * 3. Major code logical
     */
    public Future<Envelop> invokeAsync(final Envelop envelop) {
        /*
         * Bind assist to call
         */
        return this.runOn.bind(this.input.underway())
            .invoke(envelop, this.mission);
    }

    /*
     * 4. JobOutcome here
     */
    public Future<Envelop> outcomeAsync(final Envelop envelop) {
        return this.output.outcomeAsync(envelop, this.mission);
    }

    /*
     * 5. Output here ( Send message )
     * This method existing because you want to set some call back because
     * Output Address will be defined !
     */
    public Future<Envelop> outputAsync(final Envelop envelop) {
        /*
         * Bind assist to call
         */
        return this.output.bind(this.input.underway())
            .outputAsync(envelop, this.mission);
    }

    /*
     * 6. Output callback ( Consume message )
     * 1) - Write log
     * 2) - Set some status
     * 3) - Do some checking or job status changing.
     */
    public Future<Envelop> callbackAsync(final Envelop envelop) {
        return this.runOn.callback(envelop, this.mission);
    }
}
