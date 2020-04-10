package io.vertx.tp.modular.phantom;

import io.vertx.core.Future;
import io.vertx.tp.atom.cv.AoCache;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.up.fn.Fn;

import java.util.Set;

/**
 * 元数据仓库专用读取器
 */
public interface AoPerformer {

    /**
     * > Fn.pool
     * 池化 AoPerformer 管理
     * appName = MetaPerformer
     */
    static AoPerformer getInstance(final String appName) {
        return Fn.pool(AoCache.POOL_PERFORMERS, appName, () -> new ModelPerformer(appName));
    }

    /**
     * 读取当前应用下所有的Model模型数据
     */
    Future<Set<Model>> fetchModelsAsync();

    /**
     * 根据identifier读取Model模型数据（AsyncFlow / Sync）
     */
    Future<Model> fetchModelAsync(String identifier);

    Model fetchModel(String identifier);
}
