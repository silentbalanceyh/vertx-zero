package io.vertx.up.secure.profile;

import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.Authorization;
import io.vertx.ext.auth.authorization.AuthorizationContext;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class PermissionAuthorizationImpl implements PermissionAuthorization {
    private final Set<String> permissions = new HashSet<>();

    public PermissionAuthorizationImpl(final Set<String> permissions) {
        this.permissions.addAll(Objects.requireNonNull(permissions));
    }

    @Override
    public Set<String> permissions() {
        return this.permissions;
    }

    @Override
    public boolean match(final AuthorizationContext context) {
        Objects.requireNonNull(context);
        final User user = context.user();
        if (user != null) {
            final Authorization resolved = PermissionAuthorization.create(this.permissions);
            for (final String providerId : user.authorizations().getProviderIds()) {
                for (final Authorization authorization : user.authorizations().get(providerId)) {
                    if (authorization.verify(resolved)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean verify(final Authorization otherAuthorization) {
        Objects.requireNonNull(otherAuthorization);
        if (otherAuthorization instanceof PermissionAuthorization) {
            final PermissionAuthorization permission = (PermissionAuthorization) otherAuthorization;
            return this.permissions.containsAll(permission.permissions());
        }
        return false;
    }
}
