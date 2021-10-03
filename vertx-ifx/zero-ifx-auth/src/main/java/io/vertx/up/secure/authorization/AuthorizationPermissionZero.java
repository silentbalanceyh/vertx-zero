package io.vertx.up.secure.authorization;

import io.vertx.ext.auth.authorization.Authorization;
import io.vertx.ext.auth.authorization.AuthorizationContext;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AuthorizationPermissionZero implements AuthorizationPermission {
    private final Set<String> permissions = new HashSet<>();
    private String resourceId;

    public AuthorizationPermissionZero(final Set<String> permissions) {
        this.permissions.addAll(Objects.requireNonNull(permissions));
    }

    @Override
    public Set<String> permissions() {
        return this.permissions;
    }

    @Override
    public String resource() {
        return this.resourceId;
    }

    @Override
    public AuthorizationPermission resource(final String resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    @Override
    public boolean match(final AuthorizationContext context) {

        return false;
    }

    @Override
    public boolean verify(final Authorization otherAuthorization) {

        return false;
    }
}
