package io.vertx.tp.plugin.shell;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.tp.plugin.shell.atom.CommandAtom;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.log.Annal;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public abstract class AbstractCommander implements Commander {
    protected transient CommandAtom atom;
    protected transient Vertx vertxRef;
    protected transient Environment environment = Environment.Production;

    @Override
    public Commander bind(final Environment environment) {
        if (Objects.nonNull(environment)) {
            this.environment = environment;
        }
        return this;
    }

    @Override
    public Commander bind(final CommandAtom atom) {
        this.atom = atom;
        return this;
    }

    @Override
    public Commander bind(final Vertx vertx) {
        this.vertxRef = vertx;
        return this;
    }

    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        return Future.succeededFuture(this.execute(args));
    }

    @Override
    public TermStatus execute(final CommandInput args) {
        return TermStatus.SUCCESS;
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}