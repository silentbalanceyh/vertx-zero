package io.vertx.up.runtime;

import io.vertx.core.Vertx;
import io.vertx.tp.plugin.shared.MapInfix;
import io.vertx.up.Launcher;
import io.vertx.up.annotations.Up;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.web.ZeroLauncher;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.UpClassArgsException;
import io.vertx.zero.exception.UpClassInvalidException;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Starter of 3 Applications
 * - VertxApplication               ( Standard )
 * - MicroApplication               ( Legacy Micro Service )
 * - AeonApplication                ( Native Cloud )
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class ZeroApplication {

    protected final Class<?> upClazz;
    protected ConcurrentMap<String, Annotation> annotationMap = new ConcurrentHashMap<>();

    protected ZeroApplication(final Class<?> clazz) {
        // Must not be null
        /*
         * Although the input `clazz` is not important, but zero container require the input
         * clazz mustn't be null, for future usage such as plugin extension for it.
         */
        Fn.out(null == clazz, UpClassArgsException.class, this.getClass());

        /*
         * Stored clazz information
         * 1. clazz stored
         * 2. annotation extraction from Annotation[] -> Annotation Map
         */
        this.upClazz = clazz;
        this.annotationMap = Anno.get(clazz);
        /*
         * Zero specification definition for @Up here.
         * The input class must annotated with @Up instead of other description
         */
        Fn.out(!this.annotationMap.containsKey(Up.class.getName()), UpClassInvalidException.class, this.getClass(),
            null == this.upClazz ? null : this.upClazz.getName());
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    protected void ready() {
        throw new _501NotSupportException(this.getClass());
    }

    public void run(final Object... args) {
        // Check for basic preparing
        this.ready();
        // Execute the Vertx Running for initialized
        final Launcher<Vertx> launcher = Ut.singleton(ZeroLauncher.class);
        launcher.start(vertx -> {

            if (ZeroHeart.isShared()) {
                /*
                 * Map infix initialized first to fix
                 * Boot issue here to enable map infix ( SharedMap will be used widely )
                 * It means that the MapInfix should started twice for safe usage in future
                 *
                 * In our production environment, only MapInfix plugin booting will cost some time
                 * to be ready, it may take long time to be ready after container started
                 * In this kind of situation, Zero container start up MapInfix internally first
                 * to leave more time to be prepared.
                 */
                MapInfix.init(vertx);
            }

            this.runInternal(vertx, args);
        });
    }

    protected abstract void runInternal(final Vertx vertx, final Object... args);
}
