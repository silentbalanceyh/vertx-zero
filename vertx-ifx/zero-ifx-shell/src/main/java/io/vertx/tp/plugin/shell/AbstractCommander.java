package io.vertx.tp.plugin.shell;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.tp.plugin.shell.atom.CommandArgs;
import io.vertx.tp.plugin.shell.atom.CommandOption;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.log.Annal;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public abstract class AbstractCommander implements Commander {
    protected transient CommandOption option;
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
    public Commander bind(final CommandOption option) {
        this.option = option;
        return this;
    }

    @Override
    public Commander bind(final Vertx vertx) {
        this.vertxRef = vertx;
        return this;
    }

    @Override
    public Future<TermStatus> executeAsync(final CommandArgs args) {
        return Future.succeededFuture(this.execute(args));
    }

    @Override
    public TermStatus execute(final CommandArgs args) {
        return TermStatus.SUCCESS;
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}