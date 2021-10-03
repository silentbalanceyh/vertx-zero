package io.vertx.up.secure.authorization;

import io.vertx.ext.auth.authorization.Authorization;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface PermissionAuthorization extends Authorization {

    static PermissionAuthorization create(final Set<String> permissions) {
        return new WallPermissionAuthorization(permissions);
    }

    Set<String> permissions();

    String resource();

    PermissionAuthorization resource(String resourceId);
}
