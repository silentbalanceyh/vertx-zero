package io.vertx.up.atom.sch;

import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.eon.em.typed.PerMode;
import io.vertx.up.uca.job.plan.JobAt;
import io.vertx.up.util.Ut;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KTimerFormula {
    /*
     * Two Way Here
     * 1.  runFormula = null, runAt ( value )       ( Simple )
     * 2.  runFormula = value,  runAt ( ignored )   ( Complex )
     * 3.  runFormula = value,  runAt = null        ( Complex )
     */
    private final Queue<Instant> complexAt = new ConcurrentLinkedQueue<>();
    private final List<String> formulas = new ArrayList<>();
    private final boolean isComplex;
    private Instant simpleAt;
    private PerMode mode;

    public KTimerFormula(final String runFormula, final Instant runAt) {
        this.isComplex = Ut.isNotNil(runFormula);
        if (this.isComplex) {
            // Advanced Formula Support
            this.initComplex(runFormula);
        } else {
            // Legacy ( Common and Standard )
            this.initSimple(runAt);
        }
    }

    private void initComplex(final String runFormula) {
        final String[] split = runFormula.split(VString.COMMA);
        for (int idx = VValue.ONE; idx < split.length; idx++) {
            // Skip the first flag
            final String formula = split[idx];
            if (Ut.isNotNil(formula)) {
                this.formulas.add(formula);
            }
        }
        this.mode = Ut.toEnum(() -> split[VValue.IDX], PerMode.class, PerMode.D);
    }

    // Parsing for Formula to calculate the result
    private void initSimple(final Instant runAt) {
        this.simpleAt = runAt;
        this.mode = PerMode.D;
    }

    public Instant runAt() {
        if (this.isComplex) {
            // Double check whether current queue is empty
            if (this.complexAt.isEmpty()) {
                final JobAt job = JobAt.instance(this.mode);
                this.complexAt.addAll(job.analyze(this.formulas, Instant.now()));
            }
            // The parsed queue must not be empty
            return this.complexAt.isEmpty() ? Instant.now() : this.complexAt.poll();
        } else {
            return this.simpleAt;
        }
    }

    public PerMode mode() {
        return this.mode;
    }

    public DateTimeFormatter formatter() {
        final JobAt job;
        if (this.isComplex) {
            job = JobAt.instance(this.mode);
        } else {
            // Daily Formatter
            job = JobAt.instance(PerMode.D);
        }
        return DateTimeFormatter.ofPattern(job.format());
    }

    @Override
    public String toString() {
        return "KTimerFormula{" +
            "formulas=" + this.formulas +
            ", isComplex=" + this.isComplex +
            ", simpleAt=" + this.simpleAt +
            ", mode=" + this.mode +
            '}';
    }
}
