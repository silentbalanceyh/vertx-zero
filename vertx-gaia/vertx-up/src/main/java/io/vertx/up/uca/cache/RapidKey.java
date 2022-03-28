package io.vertx.up.uca.cache;

/**
 * The Cache Key Definition
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface RapidKey {
    String DIRECTORY = "ZERO-CACHE-DIRECTORY";

    String JOB_DIRECTORY = "ZERO-CACHE-JOB_DIRECTORY";

    String REFERENCE = "ZERO-CACHE-REFERENCE";

    String VIEW_FULL = "ZERO-CACHE-VIEW-FULL";

    String RESOURCE = "ZERO-CACHE-RESOURCE";

    String DATABASE = "ZERO-CACHE-DATABASE";

    String DATABASE_MULTI = "ZERO-CACHE_MULTI_DATABASE";

    String UI_LAYOUT = "ZERO-CACHE-TPL";

    interface User {
        String MY_VIEW = "ZERO-CACHE-USER-VIEW";

        String MY_HABITUS = "ZERO-CACHE-HABITUS";

        String AUTHORIZATION = "ZERO-CACHE-403";

        String AUTHENTICATE = "ZERO-CACHE-401";
    }
}
