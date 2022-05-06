package io.vertx.up.secure.authenticate;

import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.uca.cache.Cc;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface AdapterProvider {
    Cc<String, AdapterProvider> CC_ADAPTER = Cc.openThread();

    /*
     * Basic / Redirect
     * use this method, if you want to extend features, you can write your
     * own provider and put into this package for extension
     */
    @SuppressWarnings("all")
    static AdapterProvider common() {
        return (AdapterProvider) CC_ADAPTER.pick(AdapterCommon::new, AdapterCommon.class.getName());
        // Fn.po?lThread(Pool.POOL_ADAPTER, AdapterCommon::new, AdapterCommon.class.getName());
    }

    AuthenticationProvider provider(Aegis aegis);
}
