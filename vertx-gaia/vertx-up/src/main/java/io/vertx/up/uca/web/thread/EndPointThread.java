package io.vertx.up.uca.web.thread;

import io.vertx.up.atom.agent.Event;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.rs.Extractor;
import io.vertx.up.uca.rs.config.EventExtractor;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;

public class EndPointThread extends Thread {

    private static final Annal LOGGER = Annal.get(EndPointThread.class);

    private final Set<Event> events = new HashSet<>();

    private final transient Extractor<Set<Event>> extractor =
        Ut.instance(EventExtractor.class);

    private final transient Class<?> reference;

    public EndPointThread(final Class<?> clazz) {
        this.setName("zero-endpoint-scanner-" + this.getId());
        this.reference = clazz;
    }

    @Override
    public void run() {
        if (null != this.reference) {
            this.events.addAll(this.extractor.extract(this.reference));
            LOGGER.info(Info.SCANNED_EVENTS, this.reference.getName(),
                this.events.size());
        }
    }

    public Set<Event> getEvents() {
        return this.events;
    }
}
