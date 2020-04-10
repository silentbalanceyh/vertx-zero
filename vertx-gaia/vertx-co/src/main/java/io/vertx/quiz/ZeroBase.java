package io.vertx.quiz;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.Timeout;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public abstract class ZeroBase extends EpicBase {

    protected final static VertxOptions OPTIONS = /* Block issue of 2000ms for testing of long time works */
            new VertxOptions().setMaxEventLoopExecuteTime(1800_000_000_000L)
                    .setMaxWorkerExecuteTime(1800_000_000_000L)
                    .setBlockedThreadCheckInterval(10000);
    protected final static Vertx VERTX = Vertx.vertx(OPTIONS);
    @Rule
    public final RunTestOnContext rule = new RunTestOnContext(OPTIONS);

    @Rule
    public Timeout timeout = Timeout.seconds(1800L);
}
