package io.vertx.tp.rbac.refine;

import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.core.Future;
import io.vertx.tp.rbac.atom.ScConfig;
import io.vertx.tp.rbac.init.ScPin;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.util.Ut;

import java.util.Locale;

class ScTool {
    private static final Annal LOGGER = Annal.get(ScTool.class);
    private static final ScConfig CONFIG = ScPin.getConfig();
    /*
     * Data Pool ( Pond ) for Cache
     * There are some pools
     * 1) name = POOL_CODE ( Default Value )
     *      Desc: Authorization cache pool for oauth logging workflow.
     *      1.1. The data existing inner 30s ( configured by `codeExpired` )
     *      1.2. The name `POOL_CODE` could be configured by `codePool`
     *      1.3. The authorization code length could be configured by `codeLength`
     *      1.4. Method: `getCode/putCode`
     */

    static String valueCode() {
        final Integer codeLength = CONFIG.getCodeLength();
        final String authCode = Ut.randomString(codeLength);
        ScLog.infoAuth(LOGGER, "Generated Authorization Code: {0}", authCode);
        return authCode;
    }

    static String valuePassword() {
        return CONFIG.getPasswordInit();
    }

    static String valueProfile(final SResource resource) {
        /*
         * Get Role/Group/Tree modes
         */
        final String modeRole = resource.getModeRole();
        final String modeGroup = resource.getModeGroup();
        if (Ut.isNil(modeGroup)) {
            /*
             * User Mode
             *
             * USER_UNION
             * USER_INTERSECT
             * USER_EAGER
             * USER_LAZY
             */
            return "USER_" + modeRole.toUpperCase(Locale.getDefault());
        } else {
            final String modeTree = resource.getModeTree();
            final String group = modeGroup.toUpperCase(Locale.getDefault()) +
                "_" + modeRole.toUpperCase(Locale.getDefault());
            if (Ut.isNil(modeTree)) {
                /*
                 * Group Mode
                 * HORIZON_XXX
                 * CRITICAL_XXX
                 * OVERLOOK_XXX
                 */
                return group;
            } else {
                /*
                 * Inherit / Child / Parent/ Extend
                 * EXTEND_XXX
                 * PARENT_XXX
                 * CHILD_XXX
                 * INHERIT_XXX
                 */
                return modeTree.toUpperCase(Locale.getDefault()) +
                    "_" + group;
            }
        }
    }

    /*
     * Pool configured default parameters
     * - codePool
     * - codeExpired
     * This pool is for /oauth/authorize
     * key = user id
     * - S_USER ( key )
     * - O_USER ( client_id )
     * value = requested code ( String )
     *
     * The code feature:
     * 1. When you add the code in Pool, it will be expired in 30s
     * 2. When you fetch the code, it will be removed ( Once )
     */
    @SuppressWarnings("all")
    static <V> Future<V> code(final String key) {
        final String codePool = CONFIG.getCodePool();
        return Rapid.<String, V>t(codePool).clear(key);
    }

    static <V> Future<V> code(final String key, final V value) {
        final String codePool = CONFIG.getCodePool();
        final Integer codeExpired = CONFIG.getCodeExpired();
        return Rapid.<String, V>t(codePool, codeExpired).write(key, value);
    }
}
