package cn.vertxup.rbac.wall.authorization;

import io.vertx.ext.auth.authorization.Authorization;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface ProfileAuthorization extends Authorization {
    static ProfileAuthorization create(final ConcurrentMap<String, Set<String>> permissions) {
        return new ProfileAuthorizationImpl(permissions);
    }

    ConcurrentMap<String, Set<String>> permissions();

    Set<String> permissions(final String profile);
}
