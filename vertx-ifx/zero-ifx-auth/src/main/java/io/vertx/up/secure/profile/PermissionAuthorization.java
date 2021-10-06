package io.vertx.up.secure.profile;

import io.vertx.ext.auth.authorization.Authorization;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface PermissionAuthorization extends Authorization {
    static PermissionAuthorization create(final Set<String> permissions) {
        return new PermissionAuthorizationImpl(permissions);
    }

    static PermissionAuthorization create(final String permission) {
        return new PermissionAuthorizationImpl(new HashSet<>() {
            {
                this.add(permission);
            }
        });
    }

    Set<String> permissions();
}
