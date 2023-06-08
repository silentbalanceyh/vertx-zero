package io.vertx.mod.battery.init;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BkPin {

    public static Set<String> getBuiltIn() {
        return BkConfiguration.builtIn();
    }
}
