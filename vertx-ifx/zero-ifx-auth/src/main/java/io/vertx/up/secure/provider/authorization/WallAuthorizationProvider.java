package io.vertx.up.secure.provider.authorization;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.AuthorizationProvider;
import io.vertx.up.atom.secure.Aegis;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WallAuthorizationProvider implements AuthorizationProvider {

    private transient final Aegis aegis;

    private WallAuthorizationProvider(final Aegis aegis) {
        this.aegis = aegis;
    }

    public static AuthorizationProvider provider(final Aegis aegis) {
        return new WallAuthorizationProvider(aegis);
    }

    @Override
    public String getId() {
        return this.aegis.getType().key();
    }

    @Override
    public void getAuthorizations(final User user, final Handler<AsyncResult<Void>> handler) {
        if (user.expired()) {
            // Expired, Please re-login into the system
            handler.handle(Future.failedFuture("Error"));
        } else {
            // Ready for future validation, build user attributes
            handler.handle(Future.failedFuture("Error"));
        }
    }
}
