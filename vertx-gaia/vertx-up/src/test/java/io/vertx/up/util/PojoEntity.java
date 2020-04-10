package io.vertx.up.util;

import java.time.LocalDateTime;

public class PojoEntity {

    private LocalDateTime start;
    private LocalDateTime end;

    public LocalDateTime getStart() {
        return this.start;
    }

    public void setStart(final LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }

    public void setEnd(final LocalDateTime end) {
        this.end = end;
    }
}
