package io.horizon.spi.mixture;

import io.aeon.experiment.mixture.HPerformer;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.mod.atom.modeling.Model;

import java.util.Set;

/**
 * 元数据仓库专用读取器
 */
public interface AoPerformer extends HPerformer<Model> {

    /* OxPerformer资源池（内部） */
    Cc<String, AoPerformer> CC_PERFORMER = Cc.openThread();

    /**
     * > Cc.pick
     * 池化 AoPerformer 管理
     * appName = MetaPerformer
     */
    static AoPerformer getInstance(final String appName) {
        return CC_PERFORMER.pick(() -> new ModelPerformer(appName), appName);
        // return Fn.po?l(AoCache.POOL_PERFORMERS, appName, () -> new ModelPerformer(appName));
    }

    /**
     * 「写模型」
     * 读取当前应用下所有的Model模型数据，该方法主要服务于读取所有模型，所以在执行过程中
     * 导入模型时会调用该方法，该方法并不位于 HPerformer 中，为写模型专用方法
     */
    Future<Set<Model>> fetchAsync();
}
