package io.vertx.tp.crud.uca.desk;

import io.vertx.core.Future;
import io.vertx.ext.auth.User;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.error._404ModuleMissingException;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.fn.Fn;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Wrap `envelop` here as request params
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IxMod {
    private transient Envelop envelop;
    private transient KModule module;
    private transient KModule connect;
    private transient WebException error;

    private IxMod(final String actor) {
        final KModule module;
        try {
            /* 2. IxModule extracting by `actor` */
            module = IxPin.getActor(actor);
            Fn.out(Objects.isNull(module), _404ModuleMissingException.class, this.getClass(), actor);
            this.module = module;
        } catch (final WebException error) {
            error.printStackTrace();
            this.error = error;
        } catch (final Throwable error) {
            error.printStackTrace();
            this.error = new _500InternalServerException(this.getClass(), error.getMessage());
        }
    }

    public static IxMod create(final String actor) {
        return new IxMod(actor);
    }

    public boolean canJoin() {
        return Objects.nonNull(this.connect);
    }

    public Envelop envelop() {
        return this.envelop;
    }

    public User user() {
        return this.envelop.user();
    }

    public KModule module() {
        return this.module;
    }

    public KModule connect() {
        return this.connect;
    }

    public IxMod bind(final Envelop envelop) {
        this.envelop = envelop;
        return this;
    }

    public IxMod connect(final IxMod target) {
        if (Objects.nonNull(target)) {
            this.connect = target.module;
        }
        return this;
    }

    @SafeVarargs
    public final <T> Future<T> ready(
        final T input,
        final BiFunction<T, IxMod, Future<T>>... executors) {
        // Error Checking for request building
        if (Objects.nonNull(this.error)) {
            return Future.failedFuture(this.error);
        }
        return Ix.passion(input, this, executors);
    }
}