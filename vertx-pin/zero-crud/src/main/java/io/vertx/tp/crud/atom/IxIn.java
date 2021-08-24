package io.vertx.tp.crud.atom;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.error._404ModuleMissingException;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.WebException;
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
    private final transient Class<?> target;
    private final transient Envelop envelop;
    private transient KModule module;
    private transient WebException error;

    private IxIn(final Class<?> target, final Envelop envelop) {
        this.target = target;
        this.envelop = envelop;
        {
            final HttpMethod method = envelop.method();
            final String uri = envelop.uri();
            Ix.Log.rest(this.getClass(), "---> Uri: {0} {1}", method, uri);
        }
        /* 1. Actor value here from `Envelop` */
        final String actor = Ux.getString(envelop);
        try {
            /* 2. IxModule extracting by `actor` */
            this.module = IxPin.getActor(actor);
            if (Objects.isNull(this.module)) {
                /* 3. Not Found */
                throw new _404ModuleMissingException(this.target, actor);
            }
        } catch (final WebException error) {
            // TODO: Exception here.
            error.printStackTrace();
            this.error = error;
        }
    }

    public static IxIn request(final Class<?> clazz, final Envelop envelop) {
        return new IxIn(clazz, envelop);
    }

    public Envelop envelop() {
        return this.envelop;
    }

    public KModule module() {
        return this.module;
    }

    public Class<?> target() {
        return this.target;
    }

    @SafeVarargs
    public final Future<JsonObject> readyJ(
            final JsonObject input,
            final BiFunction<JsonObject, IxIn, Future<JsonObject>>... executors
    ) {
        final JsonObject request = Ut.sureJObject(input);
        return this.ready(request.copy(), executors);
    }

    @SafeVarargs
    public final Future<JsonArray> readyA(
            final JsonArray input,
            final BiFunction<JsonArray, IxIn, Future<JsonArray>>... executors
    ) {
        final JsonArray request = Ut.sureJArray(input);
        return this.ready(request.copy(), executors);
    }

    @SafeVarargs
    private final <T> Future<T> ready(
            final T input,
            final BiFunction<T, IxIn, Future<T>>... executors) {
        // Error Checking for request building
        if (Objects.nonNull(this.error)) {
            return Future.failedFuture(this.error);
        }
        Future<T> future = Future.succeededFuture(input);
        for (final BiFunction<T, IxIn, Future<T>> executor : executors) {
            if (Objects.nonNull(executor)) {
                future = future.compose(data -> executor.apply(data, this));
            }
        }
        return future;
    }
}
