package io.vertx.aeon.specification.app;

import io.vertx.aeon.atom.secure.Hoi;
import io.vertx.aeon.runtime.H3H;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Values;
import io.vertx.up.experiment.specification.power.KApp;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 「环境工厂」
 * 静态环境工厂专用方法，替换原始的 Highway 部分的连接，并且从接口中剥离出来，可直接调用
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HES {
    private static final Annal LOGGER = Annal.get(HES.class);


    // ------------------ Owner Environment --------------------
    /*
     * 拥有者环境配置，内置于 Envelop 在绑定环境时执行，Hoi和KApp的支持维度如
     *               Hoi             KApp
     * name           x               o
     * code           x               o
     * appId          x               o
     * appKey         x               o
     * sigma          o               o ( 反向引用 )
     * tenant         o               x
     *
     * 输入数据只有三个：tenant / appId ( appKey ) / sigma，其中 appKey 不用于租户鉴别
     * 当前容器环境中有两个固定维度：
     * 1. tenant / language 是已经固定好的，都只有一个
     * 2. 上层维度：tenant
     *    下层维度：appId
     * 3. 最终计算看 sigma 是表示上层还是下层
     */
    public static void configure(final MultiMap headers) {
        final XHeader header = new XHeader();
        header.fromHeader(headers);
        /*
         * 先计算当前应用类型，基础维度是 sigma（统一标识符），根据 sigma 执行缓存创建
         * 1. 「单机环境」下 sigma 只有一个，Hoi也只有一个（tenant可null）
         * 2. 「多租户环境」 sigma = tenant 维度，Hoi也只有一个（tenant不为空，且children为空）
         * 3. 「多层租户环境」无等价维度，Hoi会存在多个，且某些Hoi会出现子租户映射表
         *
         * 任何场景下都执行的是 sigma 的缓存处理，且可直接根据 sigma 的值执行 Hoi 提取
         */
        final String sigma = header.getSigma();
        H3H.CC_OI.pick(() -> {
            /*
             * 参数：
             * {
             *     "appId": "xx",
             *     "appKey": "xx",
             *     "sigma": "xx",
             *     "tenantId": "xx",
             *     "language": "xx"
             * }
             */
            final JsonObject inputJ = header.toJson().copy();
            inputJ.remove(KName.SESSION);
            LOGGER.info("[HES] Environment Tenant initialized: {0} with parameters: {1}",
                sigma, inputJ.encode());
            return Ux.channelS(HET.class, het -> het.configure(inputJ));
        }, sigma);
    }

    public static Hoi caller(final String sigma) {
        return H3H.CC_OI.store(sigma);
    }

    public static Hoi callee(final String sigma, final String tenant) {
        if (Objects.isNull(tenant) || Objects.isNull(sigma)) {
            return null;
        }
        // 提取租户，此时 sigma 必定和 tenant 同维度，而
        // 传入的 tenant 可能是「空间租户」
        final Hoi hoi = caller(sigma);
        if (Objects.isNull(hoi)) {
            return null;
        }
        return hoi.child(tenant);
    }

    // ------------------ 提取和初始化处理上下文应用 --------------------
    /*
     * HAtom 建模上下文专用
     * 1. 内置调用 H3H 核心数据结构
     * 2. 按 name 提取（两种模式）,     context
     * 3. 按 sigma/appId 提取,        connect（原ES方法）
     */
    public static KApp context(final String name) {
        /*
         * 无填充模式，key = value，此时 key 就是应用程序名称
         */
        return H3H.CC_APP.pick(() -> KApp.instance(name), name);
    }

    public static KApp context(final JsonObject unityApp) {
        /*
         * 附加填充模式，key = value，此时 key 优先选择应用程序名称
         * 由于此时通常为动态建模初始化，所以 key 会执行附加填充，填充键
         * - appId
         * - appKey
         * - sigma
         * - code
         */
        final String name = Ut.valueString(unityApp, KName.NAME);
        return H3H.CC_APP.pick(
            () -> KApp.instance(name).bind(unityApp).synchro(),
            name
        );
    }

    // ------------------ 直接连接应用 --------------------
    public static KApp connect(final String id) {
        return H3H.CC_APP.store(id);
    }

    public static KApp connect(final String sigma, final String id) {
        KApp app = connect(sigma);
        if (Objects.isNull(app)) {
            app = connect(id);
        }
        return app;
    }

    /*
     * 连接当前应用，工作流程
     * 1. 先调用 HET 的 initialize 方法（如果CC_APP为空则执行，如果不为空不执行初始化）
     * 2. 填充最终上下文环境提取当前应用，调用 values()，只有一个则返回
     */
    public static KApp connect() {
        final Set<KApp> appSet = new HashSet<>(H3H.CC_APP.store().values());
        if (appSet.isEmpty()) {
            appSet.addAll(initialize());
        }
        KApp env = null;
        if (Values.ONE == appSet.size()) {
            env = appSet.iterator().next();
        }
        return env;
    }

    // ------------------ 内置调用 HET 初始化应用 --------------------
    private static Set<KApp> initialize() {
        return Ux.channelS(HET.class, het -> {
            // 执行初始化配置信息
            final ConcurrentMap<String, JsonObject> appMap = het.initialize();
            LOGGER.info("[HES] Environment connecting..., size = {0}", String.valueOf(appMap.size()));
            final Set<KApp> appSet = new HashSet<>();
            appMap.forEach((appId, json) -> {
                final KApp env = context(json);
                LOGGER.info("[HES] \tnamespace = {0}, name = {1}", env.ns(), env.name());
                appSet.add(env);
            });
            return appSet;
        });
    }
}
