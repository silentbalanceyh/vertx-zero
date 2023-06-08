package io.vertx.up.uca.job.plan;

import io.horizon.eon.em.typed.PerMode;
import io.vertx.up.atom.sch.KTimerFormula;
import io.vertx.up.util.Ut;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class JobAtTc {
    // ==================== Testing for Format ===================

    @Test
    public void testFormatDay() {
        final String format = format(PerMode.D, LocalDateTime.now());
        System.out.println(format);
    }

    // ==================== Testing for Daily ====================

    @Test
    public void testParse() {
        // this.parsing("D,00:08");
        // this.parsing("D,07:01,06:15");
        this.parsing("Y,00:15/57,00:16/11-9");
    }

    // ==================== Private Format ===================
    private void parsing(final String formula) {
        final KTimerFormula kFormula = new KTimerFormula(formula, null);
        System.out.println(kFormula.mode());
        System.out.println(kFormula.formatter());
        Ut.itRepeat(10, () -> {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            System.out.println(formatter.format(Ut.toDateTime(kFormula.runAt())));
        });
    }

    private void parsing(final String formula, final Instant runAt) {
        final KTimerFormula kFormula = new KTimerFormula(formula, runAt);
        System.out.println(kFormula.mode());
        System.out.println(kFormula.formatter());
        Ut.itRepeat(10, () -> System.out.println(kFormula.runAt()));
    }

    private String format(final PerMode mode, final LocalDateTime date) {
        final JobAt job = JobAt.instance(PerMode.D);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(job.format());
        return formatter.format(date);
    }
}
