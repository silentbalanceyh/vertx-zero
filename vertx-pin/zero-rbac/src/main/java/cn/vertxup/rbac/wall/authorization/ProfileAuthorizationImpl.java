package cn.vertxup.rbac.wall.authorization;

import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.Authorization;
import io.vertx.ext.auth.authorization.AuthorizationContext;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ProfileAuthorizationImpl implements ProfileAuthorization {
    private final ConcurrentMap<String, Set<String>> permissionMap = new ConcurrentHashMap<>();

    ProfileAuthorizationImpl(final ConcurrentMap<String, Set<String>> permissionMap) {
        Objects.requireNonNull(permissionMap);
        this.permissionMap.putAll(permissionMap);
    }

    @Override
    public ConcurrentMap<String, Set<String>> permissions() {
        return this.permissionMap;
    }

    @Override
    public Set<String> permissions(final String profile) {
        return this.permissionMap.getOrDefault(profile, new HashSet<>());
    }

    @Override
    public boolean match(final AuthorizationContext context) {
        Objects.requireNonNull(context);
        final User user = context.user();
        if (user != null) {
            final Authorization resolved = ProfileAuthorization.create(this.permissionMap);
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
        if (otherAuthorization instanceof ProfileAuthorization) {
            final ProfileAuthorization profileAuthorization = (ProfileAuthorization) otherAuthorization;
            final Set<Boolean> checked = profileAuthorization.permissions().keySet().stream().map(profileName -> {
                final Set<String> resourcePerms = profileAuthorization.permissions(profileName);
                final Set<String> userPerms = this.permissionMap.getOrDefault(profileName, new HashSet<>());
                return userPerms.containsAll(resourcePerms);
            }).collect(Collectors.toSet());
            // One is OK
            return checked.stream().anyMatch(item -> item);
        }
        return false;
    }
}
