package io.vertx.quiz;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.Timeout;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * # 「Co」 Testing Framework
 *
 * This class is Testing Bases ( annotated with `@RunWith` in `vertx-unit` ) and it provide default
 * Vertx instance. For fixing the block issue ( Default is 2000ms ), I extend the time here
 *
 * 1. Event Loop Execute Time: 1800s -> 30mins
 * 2. Worker Execute Time: 1800s -> 30mins
 *
 * It means that our test cases must do some long time works especially in enterprise application or
 * complex web applications. This class provide default testing environment in vert.x so that developers could
 * do unit testing smartly.
 *
 * From this class, it provide vertx environment instead of JUnit purely.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@RunWith(ZeroUnitRunner.class)
public abstract class ZeroBase extends EpicBase {
    /**
     * Default vertx options to fix 2000ms issue
     */
    protected final static VertxOptions OPTIONS =
        /* Block issue of 2000ms for testing of long time works */
        new VertxOptions().setMaxEventLoopExecuteTime(1800_000_000_000L)
            .setMaxWorkerExecuteTime(1800_000_000_000L)
            .setBlockedThreadCheckInterval(10000);
    /**
     * Default Vertx instance based on testing options
     */
    protected final static Vertx VERTX = Vertx.vertx(OPTIONS);

    /**
     * Default testing context in `vertx-unit` with `RunTestOnContext` here.
     */
    @Rule
    public final RunTestOnContext rule = new RunTestOnContext(OPTIONS);

    /**
     * Defined timeout in each test case that's matched with 1800s.
     */
    @Rule
    public Timeout timeout = Timeout.seconds(1800L);

    /*
     * Async Processing for T
     */
    public <T> void tcAsync(final TestContext context, final Future<T> future, final Consumer<T> consumer) {
        Objects.requireNonNull(future);
        final Async async = context.async();
        future.onComplete(handler -> {
            if (handler.succeeded()) {
                consumer.accept(handler.result());
                async.complete();
            } else {
                final Throwable ex = handler.cause();
                if (Objects.nonNull(ex)) {
                    ex.printStackTrace();
                }
                context.fail(ex);
            }
        });
    }

    public void tcAsync(final TestContext context, final Future<Boolean> future, final boolean expected) {
        this.tcAsync(context, future, result -> context.assertEquals(expected, result));
    }

    public void tcAsync(final TestContext context, final Future<Long> future, final Long expected) {
        this.tcAsync(context, future, result -> context.assertEquals(expected, result));
    }
}
