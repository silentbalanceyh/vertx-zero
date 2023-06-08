package io.macrocosm.atom.context;

import io.horizon.eon.VName;
import io.horizon.eon.em.EmApp;
import io.horizon.specification.app.HBelong;
import io.horizon.util.HUt;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.macrocosm.specification.secure.HoI;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang : 2023-06-07
 */
class KAmbientRuntime {
    private static final ConcurrentMap<String, String> VECTOR = new ConcurrentHashMap<>();

    /**
     * 替换原始的 HES / HET / HoI 专用
     * <pre><code>
     *     构造向量表
     *     name
     *     ns           = cacheKey
     *     appId
     *     appKey       = cacheKey
     *     tenantId     = cacheKey
     *     sigma        = cacheKey
     * </code></pre>
     *
     * @param ark  HArk 应用配置容器
     * @param mode EmApp.Mode 应用模式
     */
    void registry(final HArk ark, final EmApp.Mode mode) {
        // 提取缓存键和应用程序引用
        final String key = HUt.keyApp(ark);
        final HApp app = ark.app();

        // 1. 基础规范：name / ns
        final String ns = app.ns();
        VECTOR.put(ns, key);
        final String name = app.name();
        VECTOR.put(name, key);

        // 2. 扩展规范：code / appKey / appId
        final String code = app.option(VName.CODE);
        Optional.ofNullable(code).ifPresent(each -> VECTOR.put(each, key));
        final String appId = app.option(VName.APP_ID);
        Optional.ofNullable(appId).ifPresent(each -> VECTOR.put(each, key));
        final String appKey = app.option(VName.APP_KEY);
        Optional.ofNullable(appKey).ifPresent(each -> VECTOR.put(each, key));

        // 3. 选择规范：sigma / tenantId
        if (EmApp.Mode.CUBE == mode) {
            // 单租户 / 单应用，tenantId / sigma 可表示缓存键（唯一的情况）
            final HoI hoi = ark.owner();
            Optional.ofNullable(hoi).map(HBelong::owner).ifPresent(each -> VECTOR.put(each, key));
            final String sigma = app.option(VName.SIGMA);
            Optional.ofNullable(sigma).ifPresent(each -> VECTOR.put(each, key));
        }
    }

    String keyFind(final String key) {
        return VECTOR.getOrDefault(key, null);
    }
}
