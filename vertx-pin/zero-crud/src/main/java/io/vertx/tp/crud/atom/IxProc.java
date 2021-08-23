package io.vertx.tp.crud.atom;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.error._404ModuleMissingException;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.WebException;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.function.BiFunction;

public class IxProc {
    private final transient Class<?> target;
    private transient KModule config;
    private transient UxJooq jooq;
    private transient WebException ex;

    private IxProc(final Class<?> clazz) {
        this.target = clazz;
    }

    public static IxProc create(final Class<?> clazz) {
        return new IxProc(clazz);
    }

    private void logRequest(final Envelop envelop) {
        final HttpMethod method = envelop.method();
        final String uri = envelop.uri();
        final Annal logger = Annal.get(this.target);

        Ix.infoRest(logger, "---> Uri Addr: {0} {1}", method, uri);
    }

    public IxProc input(final Envelop envelop) {
        /*
         * Actor value here for extracted from `Envelop`
         */
        final String actor = Ux.getString(envelop);
        try {
            /*
             * IxModule extracting by `actor`
             */
            this.config = IxPin.getActor(actor);
            /*
             *
             */
            this.jooq = IxPin.getDao(this.config, envelop.headers());
            if (null == this.jooq) {
                this.ex = new _404ModuleMissingException(this.target, actor);
            }
        } catch (final WebException error) {
            // TODO: Exception here.
            error.printStackTrace();
            this.ex = error;
        }
        this.logRequest(envelop);
        return this;
    }

    public Future<Envelop> envelop(final BiFunction<UxJooq, KModule, Future<Envelop>> actuator) {
        final WebException error = this.ex;
        if (null == error) {
            try {
                return actuator.apply(this.jooq, this.config);
            } catch (final WebException ex) {
                return Future.failedFuture(ex);
            } catch (final Throwable ex) {
                ex.printStackTrace();
                return Future.failedFuture(ex);
            }
        } else {
            /* IxIn Error */
            return Future.failedFuture(error);
        }
    }
}
