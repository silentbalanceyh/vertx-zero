package io.vertx.up.secure.authorization;

import io.vertx.ext.auth.authorization.Authorization;
import io.vertx.ext.auth.authorization.AuthorizationContext;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WallForbiddenAuthorization implements Authorization {

    private WallForbiddenAuthorization() {
    }

    public static Authorization create() {
        return Fn.poolThread(Pool.NO_ACCESS_POOL, WallForbiddenAuthorization::new, WallForbiddenAuthorization.class.getName());
    }

    @Override
    public boolean match(final AuthorizationContext context) {
        return false;
    }

    @Override
    public boolean verify(final Authorization authorization) {
        return false;
    }
}
