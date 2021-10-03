package io.vertx.up.secure.authorization;

import io.vertx.ext.auth.authorization.Authorization;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface AuthorizationPermission extends Authorization {
    static AuthorizationPermission create(final Set<String> permissions) {
        return new AuthorizationPermissionZero(permissions);
    }

    static AuthorizationPermission create(final String permission) {
        return new AuthorizationPermissionZero(new HashSet<>() {
            {
                this.add(permission);
            }
        });
    }

    Set<String> permissions();
}
