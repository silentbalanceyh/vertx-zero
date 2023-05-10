package io.vertx.up.secure.authenticate;

import io.horizon.uca.cache.Cc;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.up.atom.secure.Aegis;

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
    static AdapterProvider common() {
        return CC_ADAPTER.pick(AdapterCommon::new, AdapterCommon.class.getName());
    }

    static AdapterProvider extension(final AuthenticationProvider standard) {
        return CC_ADAPTER.pick(
            () -> new AdapterExtension(standard), AdapterExtension.class.getName());
    }

    AuthenticationProvider provider(Aegis aegis);
}
