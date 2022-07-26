package io.vertx.aeon.component.boot;

import io.vertx.aeon.atom.iras.HAeon;
import io.vertx.aeon.atom.iras.HBoot;
import io.vertx.aeon.atom.iras.HRepo;
import io.vertx.aeon.eon.HCache;
import io.vertx.aeon.refine.HLog;
import io.vertx.aeon.specification.boot.HOn;
import io.vertx.aeon.specification.program.HAlive;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonOn implements HOn {
    private transient Vertx vertx;

    @Override
    @SuppressWarnings("unchecked")
    public HOn bind(final Vertx vertx) {
        this.vertx = vertx;
        return this;
    }

    @Override
    public Future<Boolean> configure(final HAeon aeon) {
        final HBoot boot = aeon.boot();
        final Set<Class<?>> aliveList = boot.alive();
        if (aliveList.isEmpty()) {
            HLog.warnAeon(this.getClass(), "Alive components have not been defined, Kidd/Kinect/KZero architecture has been Disabled.");
        }
        return Fn.combineB(aliveList, clazz -> this.configure(clazz, aeon));
    }

    private Future<Boolean> configure(final Class<?> aliveCls, final HAeon aeon) {
        Objects.requireNonNull(aliveCls);
        final HAlive alive = HCache.CCT_ALIVE.pick(() -> {
            final HAlive instance = Ut.instance(aliveCls);
            instance.bind(this.vertx);
            return instance;
        }, aliveCls.getName());
        final Set<HRepo> repos = aeon.repos(alive.support());
        return Fn.combineB(repos, alive::configure);
    }
}
