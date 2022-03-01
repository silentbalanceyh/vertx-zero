package cn.vertxup.rbac.service.batch;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.KCredential;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.environment.Modeling;
import io.vertx.tp.optic.web.Credential;
import io.vertx.up.eon.KName;
import io.vertx.up.exception.web._400BadRequestException;
import io.vertx.up.exception.web._400SigmaMissingException;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractIdc implements IdcStub {
    protected transient final String sigma;

    public AbstractIdc(final String sigma) {
        this.sigma = sigma;
    }

    /*
     * Credential channel
     */
    protected <T> Future<T> credential(final Supplier<Future<T>> supplier,
                                       final Function<KCredential, Future<T>> executor) {
        return Ke.channelAsync(Credential.class, supplier,
            stub -> stub.fetchAsync(this.sigma)
                .compose(executor));
    }

    /*
     * Put modelKey from `username` = `employee key`
     */
    protected Future<JsonArray> model(final JsonArray userJson) {
        return Ke.channelAsync(Modeling.class,
            () -> Ux.future(userJson),
            stub -> stub.keyAsync(this.sigma, userJson).compose(keyMap -> {
                /*
                 * Reference modification
                 */
                Ut.itJArray(userJson).forEach(user -> {
                    /*
                     * Fix issue of `modelKey` injection
                     */
                    final String username = user.getString(KName.USERNAME);
                    final JsonObject data = keyMap.get(username);
                    if (Ut.notNil(data)) {
                        /*
                         * Replace
                         * - modelKey
                         * - modelId
                         * .etc here
                         */
                        user.mergeIn(data.copy(), true);
                    }
                });
                return Ux.future(userJson);
            })
        );
    }

    /*
     * Model channel for modelId / modelKey
     */
    protected <T> Future<T> runPre(final T user) {
        if (Ut.isNil(this.sigma)) {
            return Future.failedFuture(new _400SigmaMissingException(this.getClass()));
        } else {
            if (Objects.isNull(user)) {
                return Future.failedFuture(new _400BadRequestException(this.getClass()));
            } else {
                return Ux.future(user);
            }
        }
    }
}
