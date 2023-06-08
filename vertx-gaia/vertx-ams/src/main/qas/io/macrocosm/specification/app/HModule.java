package io.macrocosm.specification.app;

import io.macrocosm.specification.program.HBundle;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentMap;

/**
 * 「模块」
 * <hr/>
 * 当前模块所属应用的详细信息，包括应用的基本信息，应用的部署信息，应用的版本信息等等
 *
 * @author lang : 2023-05-21
 */
public interface HModule {
    /**
     * 当前模块所属应用
     *
     * @return {@link HApp}
     */
    HApp app();

    /**
     * 所属命名空间
     *
     * @return {@link HNamespace}
     */
    HNamespace namespace();

    /**
     * 当前模块中所有模型列表，键值对模式，倒排表
     * <pre><code>
     *     1. identifier = JsonObject
     * </code></pre>
     * 其中模型的定义值为序列化数据，可直接填充到任意位置
     *
     * @return {@link ConcurrentMap}
     */
    ConcurrentMap<String, JsonObject> atoms();

    /**
     * 当前模块隶属于哪个 Bundle 发布
     *
     * @return {@link HBundle}
     */
    HBundle bundle();
}
