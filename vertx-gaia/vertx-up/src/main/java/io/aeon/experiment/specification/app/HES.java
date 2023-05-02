package io.aeon.experiment.specification.app;

import io.aeon.runtime.CRunning;
import io.horizon.atom.app.KApp;
import io.horizon.atom.cloud.HOI;
import io.horizon.eon.VValue;
import io.horizon.spi.cloud.HET;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 「环境工厂」
 * 静态环境工厂专用方法，替换原始的 Highway 部分的连接，并且从接口中剥离出来，可直接调用
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public final class HES {
    private static final String MSG_HOI = "[HES] Environment Tenant initialized: {0} with parameters: {1}, mode = {2}";
    private static final Annal LOGGER = Annal.get(HES.class);

    private HES() {
    }


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
    public static Future<Boolean> configure() {
        return Ux.channel(HET.class,
            () -> Boolean.FALSE,
            // 1. 初始化所有应用（CRunning.CC_APP就绪）
            het -> het.initialize().compose(initialized -> {
                /*
                 * 先计算当前应用类型，基础维度是 sigma（统一标识符），根据 sigma 执行缓存创建
                 * 1. 「单机环境」下 sigma 只有一个，Hoi也只有一个（tenant可null）
                 * 2. 「多租户环境」 sigma = tenant 维度，Hoi也只有一个（tenant不为空，且children为空）
                 * 3. 「多层租户环境」无等价维度，Hoi会存在多个，且某些Hoi会出现子租户映射表
                 *
                 * 任何场景下都执行的是 sigma 的缓存处理，且可直接根据 sigma 的值执行 Hoi 提取
                 */
                CRunning.CC_APP.store().values().forEach(app -> {
                    final JsonObject inputJ = app.dataJ();
                    /*
                     * CC_APP / CC_HOI 不同点
                     * 1）（应用级）CC_APP的键值是跨越级的，有多个，保证每个都可以提取数据
                     * 2）（租户级）CC_HOI则不然，它包含了 sigma（开启租户时的租户标识）
                     */
                    final String sigma = Ut.valueString(inputJ, KName.SIGMA);
                    CRunning.CC_OI.pick(() -> {
                        final HOI hoi = het.configure(inputJ);
                        LOGGER.info(MSG_HOI, sigma, inputJ.encode(), hoi.mode());
                        return hoi;
                    }, sigma);
                });
                return Ux.futureT();
            })
        );
    }

    public static HOI caller(final String sigma) {
        return CRunning.CC_OI.store(sigma);
    }

    public static HOI callee(final String sigma, final String tenant) {
        if (Objects.isNull(tenant) || Objects.isNull(sigma)) {
            return null;
        }
        // 提取租户，此时 sigma 必定和 tenant 同维度，而
        // 传入的 tenant 可能是「空间租户」
        final HOI hoi = caller(sigma);
        if (Objects.isNull(hoi)) {
            return null;
        }
        return hoi.child(tenant);
    }

    // ------------------ 提取和初始化处理上下文应用 --------------------
    /*
     * HAtom 建模上下文专用
     * 1. 内置调用 H3H 核心数据结构
     * 2. 按 name 提取（两种模式）,     connect
     */
    public static KApp connect(final String name) {
        /*
         * 无填充模式，key = value，此时 key 就是应用程序名称
         */
        return CRunning.CC_APP.pick(() -> KApp.instance(name), name);
    }

    /*
     * 反向调用，此处有可能会引起不必要的死循环
     * HET / HES 在调用过程中有可能会出现相互之间循环调用，但
     * 1）由于HES是静态调用，HET是实例化过后调用，所以此处不会轻易出现死循环
     * 2）HET调用HES时实际规划成反向调用，其目的就是让系统不会出现死循环，否则方法名容易引起误解
     */
    public static KApp connect(final String sigma, final String key) {
        /*
         * 1. 按sigma查找
         * 2. 按key查找
         * 3. 都找不到时读取当前
         */
        KApp app = null;
        if (Ut.isNotNil(sigma)) {
            // 按sigma查找
            app = CRunning.CC_APP.store(sigma);
        }
        if (Ut.isNotNil(key) && Objects.isNull(app)) {
            // 按key查找
            app = CRunning.CC_APP.store(key);
        }
        return Objects.isNull(app) ? connect() : app;
    }

    // ------------------ 直接连接应用 --------------------
    /*
     * 连接当前应用，工作流程
     * 1. 先调用 HET 的 initialize 方法（如果CC_APP为空则执行，如果不为空不执行初始化）
     * 2. 填充最终上下文环境提取当前应用，调用 values()，只有一个则返回
     */
    public static KApp connect() {
        final Set<KApp> appSet = new HashSet<>(CRunning.CC_APP.store().values());
        KApp env = null;
        if (VValue.ONE == appSet.size()) {
            env = appSet.iterator().next();
        }
        return env;
    }
}
