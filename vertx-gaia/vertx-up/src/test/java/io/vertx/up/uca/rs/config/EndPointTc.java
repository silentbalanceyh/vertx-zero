package io.vertx.up.uca.rs.config;

import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.UpBase;
import io.vertx.quiz.example.ED;
import io.vertx.quiz.example.ED1;
import io.vertx.up.atom.agent.Event;
import org.junit.Test;

import java.util.Set;

public class EndPointTc extends UpBase {

    @Test
    public void testEndPoint(final TestContext context) {
        final Set<Event> all = new ConcurrentHashSet<>();
        all.addAll(this.extractor().extract(ED1.class));
        all.addAll(this.extractor().extract(ED.class));
        for (final Event event : all) {
            this.getLogger().info("[ ZERO Test ] Extract event: {0}.", event);
        }
    }
}
