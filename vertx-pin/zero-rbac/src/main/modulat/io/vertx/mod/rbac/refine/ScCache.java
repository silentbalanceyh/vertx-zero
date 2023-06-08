package io.vertx.mod.rbac.refine;

import cn.vertxup.rbac.domain.tables.pojos.OUser;
import cn.vertxup.rbac.domain.tables.pojos.SPath;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.exception.web._501NotSupportException;
import io.horizon.spi.web.Credential;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mod.ke.error._403TokenGenerationException;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.mod.rbac.atom.ScConfig;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.mod.rbac.error._401ImageCodeWrongException;
import io.vertx.mod.rbac.error._401MaximumTimesException;
import io.vertx.mod.rbac.init.ScPin;
import io.vertx.mod.rbac.logged.ScUser;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.uca.log.DevEnv;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.vertx.mod.rbac.refine.Sc.LOG;

class ScCache {
    private static final Annal LOGGER = Annal.get(ScCache.class);
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
        LOG.Auth.info(LOGGER, "Generated Authorization Code: {0}", authCode);
        return authCode;
    }

    static String valuePassword() {
        return CONFIG.getInitializePassword();
    }

    static Future<OUser> valueAuth(final SUser user, final JsonObject inputJ) {
        final String language = inputJ.getString(KName.LANGUAGE, KWeb.ARGS.V_LANGUAGE);
        final JsonObject initializeJ = CONFIG.getInitialize();
        final OUser oUser = Ux.fromJson(initializeJ, OUser.class);
        oUser.setClientId(user.getKey())
            .setClientSecret(Ut.randomString(64))
            .setLanguage(language)
            .setActive(Boolean.TRUE)
            .setKey(UUID.randomUUID().toString());
        return Ux.future(oUser);
    }

    static Future<List<SUser>> valueAuth(final JsonArray userA, final String sigma) {
        final List<SUser> users = Ux.fromJson(userA, SUser.class);
        users.forEach(user -> {
            user.setKey(UUID.randomUUID().toString());
            user.setActive(Boolean.TRUE);
            /* 12345678 */
            final String initPwd = valuePassword();
            user.setPassword(initPwd);
            user.setSigma(sigma);
            if (Objects.isNull(user.getLanguage())) {
                user.setLanguage(KWeb.ARGS.V_LANGUAGE);
            }
        });
        return Ux.future(users);
    }

    static Future<List<OUser>> valueAuth(final List<SUser> users) {
        if (users.isEmpty()) {
            /* Now inserted */
            return Ux.futureL();
        }

        // sigma 值聚集
        final Set<String> sigmaSet = Ut.valueSetString(users, SUser::getSigma);
        if (VValue.ONE != sigmaSet.size()) {
            return Future.failedFuture(new _403TokenGenerationException(ScCache.class, sigmaSet.size()));
        }
        /*
         * Credential 通道读取，主要读取 KCredential 对象，属性如：
         * - appId
         * - sigma
         * - language
         * - realm
         * - grantType
         * 此处主要信息为 realm 和 grantType 两个属性
         */
        final String sigma = sigmaSet.iterator().next();
        return Ux.channelA(Credential.class, Ux::futureL, stub -> stub.fetchAsync(sigma).compose(credential -> {
            // OUser processing ( Batch Mode )
            final List<OUser> ousers = new ArrayList<>();
            users.stream().map(user -> new OUser()
                    .setActive(Boolean.TRUE)
                    .setKey(UUID.randomUUID().toString())
                    .setClientId(user.getKey())
                    .setClientSecret(Ut.randomString(64))
                    .setScope(credential.getRealm())
                    .setLanguage(credential.getLanguage())
                    .setGrantType(credential.getGrantType()))
                .forEach(ousers::add);
            return Ux.future(ousers);
        }));
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

    static <R> Future<R> admitPath(final SPath path, final Function<SPath, Future<R>> executor, final String suffix) {
        if (DevEnv.cacheAdmit()) {
            // Cache Enabled for Default
            final String admitPool = CONFIG.getPoolAdmit();
            // Each sigma has been mapped to single pool
            final String poolName = admitPool + VString.SLASH + path.getSigma() + VString.SLASH + suffix;
            final Rapid<String, R> rapid = Rapid.t(poolName, 3600);
            return rapid.cached(path.getKey(), () -> executor.apply(path));
        } else {
            return executor.apply(path);
        }
    }

    /*
     * Image generation for tool
     */
    static <T> Future<T> imageVerify(final String sessionId, final JsonObject params, final Function<JsonObject, Future<T>> executor) {
        final Boolean support = CONFIG.getVerifyCode();
        if (Objects.nonNull(support) && support) {
            Fn.out(Objects.isNull(sessionId), _501NotSupportException.class, ScCache.class);
            final String imageCode = params.getString(AuthKey.CAPTCHA_IMAGE);
            if (Objects.isNull(imageCode)) {
                // Not Match
                return Fn.outWeb(_401ImageCodeWrongException.class, ScCache.class, null);
            }
            final String imagePool = CONFIG.getPoolVerify();
            final Rapid<String, String> rapid = Rapid.t(imagePool);
            return rapid.read(sessionId).compose(stored -> {
                if (Objects.isNull(stored)) {
                    // Not Match
                    return Fn.outWeb(_401ImageCodeWrongException.class, ScCache.class, imageCode);
                } else {
                    // Case Ignored
                    if (stored.equalsIgnoreCase(imageCode)) {
                        final JsonObject processed = params.copy();
                        processed.remove(AuthKey.CAPTCHA_IMAGE);
                        return rapid.clear(sessionId).compose(nil -> executor.apply(processed));
                    } else {
                        // Not Match
                        return Fn.outWeb(_401ImageCodeWrongException.class, ScCache.class, imageCode);
                    }
                }
            });
        } else {
            // Skip because image Verify off
            return executor.apply(params);
        }
    }

    static Future<Buffer> imageOn(final String sessionId, final Integer width, final Integer height) {
        final Boolean support = CONFIG.getVerifyCode();
        if (Objects.nonNull(support) && support) {
            // Username in Pool
            final String imagePool = CONFIG.getPoolVerify();
            final String code = Ut.randomCaptcha(5);
            return Rapid.<String, String>t(imagePool, 300).write(sessionId, code)
                // Generate Image Buffer to Front-End
                .compose(codeImage -> ScImage.imageGenerate(codeImage, width, height));
        } else {
            // Skip because image Verify off
            return Fn.outWeb(_501NotSupportException.class, ScCache.class);
        }
    }

    static Future<Boolean> imageKo(final String sessionId) {
        final Boolean support = CONFIG.getVerifyCode();
        if (Objects.nonNull(support) && support) {
            // Username in Pool
            final String imagePool = CONFIG.getPoolVerify();
            return Rapid.<String, String>t(imagePool).clear(sessionId)
                .compose(nil -> Ux.futureT());
        } else {
            // Skip because image Verify off
            return Fn.outWeb(_501NotSupportException.class, ScCache.class);
        }
    }

    /*
     * Login limitation for times in system, If failed the counter of current username should
     * increase by 1, the max limitation handler time is ScConfig.getVerifyLimitation
     * - lockVerify
     * - lockOn
     * - lockOff
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
                        return Fn.outWeb(_401MaximumTimesException.class, ScCache.class, limitation, verifyDuration);
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

    static Future<JsonObject> view(final RoutingContext context, final String habitus) {
        // habitus 为 Zero Framework 的专用 Session（内置业务会话标识）
        if (Ut.isNil(habitus)) {
            /*
             * Empty bound in current interface instead of other
             * -- Maybe the user has not been logged
             * */
            return Ux.futureJ();
        }

        final String viewKey = Ke.keyView(context);
        final ScUser scUser = ScUser.login(habitus);
        /*
         * 此处需要针对缓存中的 matrix 执行拷贝，后续流程中会直接执行如下流程
         * cache matrix -> Before + Visitant -> 影响 matrix
         *              -> After  + Visitant -> 影响 matrix
         * DataRegion中消费的 matrix 在新版本中会直接被 Cosmo 组件变更，而造成最终的影响
         * 所以读取出来的视图矩阵在此处执行拷贝
         */
        return scUser.view(viewKey)
            .compose(matrix -> Ux.future(Objects.isNull(matrix) ? null : matrix.copy()));
    }
}
