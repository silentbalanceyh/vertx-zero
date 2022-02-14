package io.vertx.tp.rbac.refine;

import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._401MaximumTimesException;
import io.vertx.tp.rbac.atom.ScConfig;
import io.vertx.tp.rbac.init.ScPin;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

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
        final String codePool = CONFIG.getPoolCode();
        return Rapid.<String, V>t(codePool).clear(key);
    }

    static <V> Future<V> code(final String key, final V value) {
        final String codePool = CONFIG.getPoolCode();
        final Integer codeExpired = CONFIG.getCodeExpired();
        return Rapid.<String, V>t(codePool, codeExpired).write(key, value);
    }

    /*
     * Login limitation for times in system, If failed the counter of current username should
     * increase by 1, the max limitation failure time is ScConfig.getVerifyLimitation
     * -
     */
    static Future<JsonObject> lockVerify(final String username, final Supplier<Future<JsonObject>> executor) {
        final Integer limitation = CONFIG.getVerifyLimitation();
        if (Objects.isNull(limitation)) {
            // Verify Limitation is null, skip limitation code
            return executor.get();
        } else {
            final String codeLimitation = CONFIG.getPoolLimitation();
            return Rapid.<String, Integer>t(codeLimitation).read(username).compose(counter -> {
                if (Objects.isNull(counter)) {
                    // Passed
                    return executor.get();
                } else {
                    // Compared
                    if (counter < limitation) {
                        // Passed
                        return executor.get();
                    } else {
                        // Failure
                        final Integer verifyDuration = CONFIG.getVerifyDuration();
                        return Ux.thenError(_401MaximumTimesException.class, ScTool.class, limitation, verifyDuration);
                    }
                }
            });
        }
    }

    static Future<Integer> lockOn(final String username) {
        final Integer limitation = CONFIG.getVerifyLimitation();
        if (Objects.isNull(limitation)) {
            // Verify Limitation is null, skip limitation code
            return Ux.future();
        } else {
            final String codeLimitation = CONFIG.getPoolLimitation();
            final Integer verifyDuration = CONFIG.getVerifyDuration();
            final Rapid<String, Integer> rapid = Rapid.t(codeLimitation, verifyDuration);
            return rapid.read(username).compose(counter -> {
                if (Objects.isNull(counter)) {
                    // First, the limitation counter is 1.
                    return rapid.write(username, 1);
                } else {
                    return rapid.write(username, counter + 1);
                }
            });
        }
    }

    static <V> Future<V> lockOff(final String username) {
        final Integer limitation = CONFIG.getVerifyLimitation();
        if (Objects.isNull(limitation)) {
            // Verify Limitation is null, skip limitation code
            return Ux.future();
        } else {
            final String codeLimitation = CONFIG.getPoolLimitation();
            return Rapid.<String, V>t(codeLimitation).clear(username);
        }
    }
}
