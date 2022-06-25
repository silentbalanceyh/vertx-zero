package io.vertx.up.uca.job.plan;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractJobAt implements JobAt {
    @Override
    public Queue<Instant> analyze(final List<String> formulas, final Instant instant) {
        final List<Instant> parsedList = new ArrayList<>();
        formulas.forEach(formula -> {
            final Instant parsed = this.analyze(formula, instant);
            if (parsed.isAfter(instant)) {
                /*
                 * parsed > instant ( Valid )
                 **/
                parsedList.add(parsed);
            }
        });
        // Instant from `past -> now -> future`
        parsedList.sort(Instant::compareTo);
        return new ConcurrentLinkedDeque<>(parsedList);
    }

    @Override
    public String format() {
        return "YYYY-MM-dd HH:mm:ss.SSS";
    }

    protected abstract Instant analyze(final String formula, final Instant instant);
}
