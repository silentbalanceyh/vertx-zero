package io.vertx.up.secure.authenticate;

import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface ProviderAdapter {
    /*
     * Basic / Redirect
     * use this method, if you want to extend features, you can write your
     * own provider and put into this package for extension
     */
    @SuppressWarnings("all")
    static ProviderAdapter common() {
        return (ProviderAdapter) Fn.poolThread(Pool.POOL_ADAPTER, CommonAdapter::new, CommonAdapter.class.getName());
    }

    AuthenticationProvider provider(Aegis aegis);
}
