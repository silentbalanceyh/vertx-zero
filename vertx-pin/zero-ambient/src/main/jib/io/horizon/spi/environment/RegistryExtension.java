package io.horizon.spi.environment;

import cn.vertxup.ambient.domain.tables.pojos.XApp;
import cn.vertxup.ambient.domain.tables.pojos.XSource;
import io.horizon.exception.web._501NotSupportException;
import io.horizon.fn.HFn;
import io.macrocosm.specification.app.HRegistry;
import io.macrocosm.specification.config.HConfig;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.unity.Ux;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 扩展容器注册器
 * <pre><code>
 *     1. 此注册器仅适用于 zero-ambient 扩展模块的引入（非云端）
 *     2. 此注册器内置会访问 X_APP / X_SOURCE 构造 {@link HArk} 容器数据
 *     3. 由于容器本身使用了 {@link Vertx}，所以只支持异步模式的注册
 * </code></pre>
 * 注册器中不可以访问 {@link io.macrocosm.specification.app.HAmbient} 接口，因为此时该接口还未执行初始化
 *
 * @author lang : 2023-06-06
 */
public class RegistryExtension implements HRegistry<Vertx> {
    @Override
    public Set<HArk> registry(final Vertx container, final HConfig config) {
        throw new _501NotSupportException(this.getClass());
    }

    @Override
    public Future<Set<HArk>> registryAsync(final Vertx container, final HConfig config) {
        return HFn.combineT(
            // appId = XApp
            () -> RegistryKit.initApp(container),
            // appId = XSource
            () -> RegistryKit.initSource(container),
            // 1 XApp + N XSource
            (app, sources) -> {
                final Set<HArk> arkSet = new LinkedHashSet<>();
                app.keySet().forEach(appId -> {
                    final XApp appItem = app.get(appId);
                    final List<XSource> sourceList = sources.get(appId);
                    arkSet.add(RegistryKit.combine(appItem, sourceList));
                });
                return Ux.future(arkSet);
            }
        );
    }
}
