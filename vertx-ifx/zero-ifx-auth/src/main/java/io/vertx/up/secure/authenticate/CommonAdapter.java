package io.vertx.up.secure.authenticate;

import io.vertx.ext.auth.ChainAuth;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.up.atom.secure.Aegis;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class CommonAdapter extends AbstractAdapter {
    @Override
    public AuthenticationProvider provider(final Aegis aegis) {
        // Chain Provider
        final ChainAuth chain = ChainAuth.all();
        final AuthenticationProvider provider = this.provider401Internal(aegis);
        if (Objects.nonNull(provider)) {
            chain.add(provider);
        }
        // 2. Wall Provider ( Based on Annotation )
        final AuthenticationProvider wallProvider = AuthenticateBuiltInProvider.provider(aegis);
        chain.add(wallProvider);
        return chain;
    }
}
