package io.vertx.up.secure.provider;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.impl.JWTUser;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._401JwtExecutorException;
import io.vertx.up.exception.web._403ForbiddenException;
import io.vertx.up.secure.Security;
import io.vertx.up.unity.Ux;

import java.util.function.Supplier;

/**
 * JwtAuthProvider will call JwtSecurer here
 */
class JwtSecurer {

    private transient Security security;
    private transient String permissionsClaimKey;

    private JwtSecurer() {
    }

    static JwtSecurer create() {
        return new JwtSecurer();
    }

    void setSecurity(final Security security) {
        this.security = security;
    }

    void setPermissionsClaimKey(final String permissionsClaimKey) {
        this.permissionsClaimKey = permissionsClaimKey;
    }

    /*
     * 401
     * After 401 workflow, system go through 403 directly.
     */
    Future<User> authenticate(final JsonObject authInfo) {
        final String token = authInfo.getString("jwt");
        /*
         * User defined security interface and implement custom code logical
         * Call custom code logical here to do 401 validation.
         */
        return security.verify(authInfo).compose(authenticated -> next(authenticated,
            /*
             * 401 Passed, continue to do 403 workflow
             */
            () -> authorize(authInfo),
            /*
             * There is no error fired by Future.failedFuture method
             * But the validated result is false, it means that there is common
             * Authenticate workflow.
             */
            () -> new _401JwtExecutorException(getClass(), token)
        ));
    }

    /*
     * 403
     */
    Future<User> authorize(final JsonObject authInfo) {
        return security.access(authInfo).compose(authorized -> next(authorized,
            /*
             * 403 Passed, continue to do business workflow
             */
            () -> Ux.future(new JWTUser(authInfo, permissionsClaimKey)),
            /*
             * There is no error fired by Future.failedFuture method
             * But the validated result is false, it means that there is common
             * Authorization workflow.
             */
            () -> new _403ForbiddenException(getClass())));
    }

    /*
     * Success final steps
     */
    private Future<User> next(final boolean checked,
                              final Supplier<Future<User>> success,
                              final Supplier<WebException> error) {
        if (checked) {
            final Future<User> future = success.get();
            final Throwable failure = future.cause();
            return null == failure ? future : Future.failedFuture(failure);
        } else {
            return Future.failedFuture(error.get());
        }
    }
}
