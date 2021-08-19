package io.vertx.up.unity;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class UxTimer {
    private transient long start;
    private transient long end;

    UxTimer() {
    }

    @Fluent
    public UxTimer start(final long start) {
        // Clean the queue
        this.start = start;
        return this;
    }

    @Fluent
    public UxTimer end(final long end) {
        this.end = end;
        return this;
    }

    public String value() {
        final long duration = end - start;
        final LocalDateTime datetime = Ut.toDuration(duration);
        final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        return format.format(datetime);
    }
}
