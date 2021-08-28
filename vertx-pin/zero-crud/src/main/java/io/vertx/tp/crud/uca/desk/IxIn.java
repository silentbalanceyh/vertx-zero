package io.vertx.tp.crud.uca.desk;

import io.vertx.core.Future;
import io.vertx.ext.auth.User;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.error._404ModuleMissingException;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.tp.ke.atom.connect.KJoin;
import io.vertx.tp.ke.atom.connect.KPoint;
import io.vertx.tp.ke.cv.em.JoinMode;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Wrap `envelop` here as request params
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IxIn {
    private final transient Envelop envelop;
    private transient KModule module;
    private transient KModule connect;
    private transient WebException error;

    private IxIn(final Envelop envelop, final String identifier) {
        this.envelop = envelop;
        /* 1. Actor value here from `Envelop` */
        final String actor = Ux.getString(envelop);
        KModule module;
        try {
            /* 2. IxModule extracting by `actor` */
            module = IxPin.getActor(actor);
            Fn.out(Objects.isNull(module), _404ModuleMissingException.class, this.getClass(), actor);
            /*
             * 3. Joined module processing for standBy
             *    Here are switcher for IxIn to create request
             *    When you passed identifier it means that here are query string as following:
             *
             * /xxxx?module=
             *
             * The `identifier` of `actor` is different from `module`
             *
             * Example:
             *
             * /x-category/xxxx?module=pay-term
             * */
            if (Ut.isNil(identifier)) {
                // Active
                this.module = module;
            } else {
                // StandBy
                /*
                 * Connect processing, here are `module.isDirect` means
                 * KJoin must not be null
                 */
                final KJoin connect = module.getConnect();
                final KPoint target = connect.procTarget(identifier);
                if (Objects.nonNull(target) && JoinMode.CRUD == target.modeTarget()) {
                    assert Objects.nonNull(target.getCrud());
                    module = IxPin.getActor(target.getCrud());
                    Fn.out(Objects.isNull(module), _404ModuleMissingException.class, this.getClass(), target.getCrud());
                    this.module = module;
                }
            }
        } catch (final WebException error) {
            error.printStackTrace();
            this.error = error;
        } catch (final Throwable error) {
            error.printStackTrace();
            this.error = new _500InternalServerException(this.getClass(), error.getMessage());
        }
    }

    public static IxIn active(final Envelop envelop) {
        return new IxIn(envelop, null);
    }

    public static IxIn standBy(final Envelop envelop, final String identifier) {
        return new IxIn(envelop, identifier);
    }

    public boolean canJoin() {
        if (Objects.isNull(this.connect)) {
            return false;
        }
        final KJoin join = this.module.getConnect();
        if (Objects.isNull(join)) {
            return false;
        }
        final KPoint point = join.procTarget(this.connect.getIdentifier());
        return Objects.nonNull(point);
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

    public IxIn connect(final KModule connect) {
        this.connect = connect;
        return this;
    }

    @SafeVarargs
    public final <T> Future<T> ready(
        final T input,
        final BiFunction<T, IxIn, Future<T>>... executors) {
        // Error Checking for request building
        if (Objects.nonNull(this.error)) {
            return Future.failedFuture(this.error);
        }
        return Ix.passion(input, this, executors);
    }
}
