package io.vertx.up.unity;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.up.atom.Kv;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class UxTimer {
    private transient final Kv<Long, Long> timer = Kv.create();

    UxTimer() {
    }

    @Fluent
    public UxTimer start() {
        // Clean the queue
        final long start = System.nanoTime();
        this.timer.set(start, null);
        return this;
    }

    @Fluent
    public UxTimer end() {
        final long end = System.nanoTime();
        this.timer.set(end);
        return this;
    }

    public String value() {
        final long start = this.timer.getKey();
        final long end = this.timer.getValue();
        final long duration = (end - start) / 1000 / 1000;
        final LocalDateTime datetime = Ut.toDuration(duration);
        final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        return format.format(datetime);
    }
}
