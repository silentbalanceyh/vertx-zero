package io.vertx.up.secure.authorization;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.AuthorizationProvider;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._501NotSupportException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface AuthorizationNativeProvider extends AuthorizationProvider {

    @Override
    default void getAuthorizations(final User user, final Handler<AsyncResult<Void>> handler) {
        final WebException error = new _501NotSupportException(this.getClass());
        handler.handle(Future.failedFuture(error));
    }

    void getAuthorizations(User user, JsonObject request, Handler<AsyncResult<Void>> handler);
}
