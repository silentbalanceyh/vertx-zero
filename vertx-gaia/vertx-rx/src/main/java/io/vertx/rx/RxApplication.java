package io.vertx.rx;

import io.vertx.reactivex.core.Vertx;
import io.vertx.rx.web.ZeroLauncher;
import io.vertx.rx.web.anima.AgentScatter;
import io.vertx.up.Launcher;
import io.vertx.up.annotations.Up;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.Anno;
import io.vertx.up.runtime.Runner;
import io.vertx.up.uca.web.anima.Scatter;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.UpClassArgsException;
import io.vertx.zero.exception.UpClassInvalidException;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * RxJava Application begin information
 */
public class RxApplication {

    private static final Annal LOGGER = Annal.get(RxApplication.class);

    private transient final Class<?> clazz;

    private ConcurrentMap<String, Annotation> annotationMap = new ConcurrentHashMap<>();

    private RxApplication(final Class<?> clazz) {
        // Must not null
        Fn.outUp(
                null == clazz,
                LOGGER,
                UpClassArgsException.class, this.getClass());
        this.clazz = clazz;
        this.annotationMap = Anno.get(clazz);
        // Must be invalid
        Fn.outUp(
                !this.annotationMap.containsKey(Up.class.getName()),
                LOGGER,
                UpClassInvalidException.class, this.getClass(), clazz.getName());
    }

    public static void run(final Class<?> clazz, final Object... args) {
        Fn.onRun(() -> {
            // Run Rx application.
            new RxApplication(clazz).run(args);
        }, LOGGER);
    }

    private void run(final Object... args) {
        final Launcher<Vertx> launcher = Ut.singleton(ZeroLauncher.class);
        launcher.start(vertx -> {
            /** 1.Find Agent for deploy **/
            Runner.run(() -> {
                final Scatter<Vertx> scatter = Ut.singleton(AgentScatter.class);
                scatter.connect(vertx);
            }, "rx-agent-runner");
        });
    }
}
