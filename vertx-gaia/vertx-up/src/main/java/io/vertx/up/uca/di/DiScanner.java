package io.vertx.up.uca.di;

import com.google.inject.Injector;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.atom.worker.Receipt;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.uca.container.VInstance;

import java.util.Objects;

/*
 * New structure for JSR310 / JSR299
 * In future
 * - Now: Set terminal
 * - Future: Scc calculation extension
 */
@Deprecated
public class DiScanner {

    private final transient Annal logger;

    private DiScanner(final Class<?> callee) {
        this.logger = Annal.get(callee);
    }

    public static DiScanner create(final Class<?> callee) {
        return Fn.pool(Pool.INJECTION, callee, () -> new DiScanner(callee));
    }

    @SuppressWarnings("all")
    public <T> T singleton(final Class<?> instanceCls) {
        /*
         * Initialize object for Agent / Worker
         */
        final Injector di = ZeroAnno.getDi();
        logger.info("[ DI ] Di environment will create class: {0}", instanceCls);
        final T instance = (T) di.getInstance(instanceCls);
        /*
         * Infix Processing
         */
        return instance;
    }

    public void singleton(final Event agent) {
        /*
         * JSR299 / 310 injection
         */
        final Object replaced = this.singletonInternal(agent.getProxy());
        if (Objects.nonNull(replaced)) {
            agent.setProxy(replaced);
        }
    }

    public void singleton(final Receipt worker) {
        final Object replaced = this.singletonInternal(worker.getProxy());
        if (Objects.nonNull(replaced)) {
            worker.setProxy(replaced);
        }
    }

    public Object singletonInternal(final Object proxy) {
        if (Objects.nonNull(proxy)) {
            final Class<?> clazz = proxy.getClass();
            if (VInstance.class != clazz) {
                return this.singleton(clazz);
            }
        }
        return null;
    }
}
