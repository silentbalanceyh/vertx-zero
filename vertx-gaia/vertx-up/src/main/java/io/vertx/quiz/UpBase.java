package io.vertx.quiz;

import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.rs.Extractor;
import io.vertx.up.uca.rs.config.EventExtractor;
import io.vertx.up.util.Ut;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.Set;

@RunWith(VertxUnitRunner.class)
public abstract class UpBase {

    @Rule
    public final RunTestOnContext rule = new RunTestOnContext();

    protected Extractor<Set<Event>> extractor() {
        return Ut.singleton(EventExtractor.class);
    }

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }
}
