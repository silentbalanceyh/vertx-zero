package io.macrocosm.specification.app;

import io.horizon.specification.app.HUri;
import io.modello.specification.element.HNs;

/**
 * 「命名空间」后续建模会和当前命名空间直接关联，实现命名空间的相关关联
 *
 * @author lang : 2023-05-21
 */
public interface HNamespace extends HNs {
    /**
     * Namespace名空间的网络标识符
     *
     * @return {@link HUri}
     */
    HUri uri();

    /**
     * 名空间所属 Domain 域，此处 Domain 域会关联到三个不同的区域
     * <pre><code>
     *     1. {@link io.macrocosm.specification.secure.HTenant}
     *     2. {@link io.macrocosm.specification.secure.HPlatform}
     *     3. {@link io.macrocosm.specification.secure.HDomain}
     * </code></pre>
     *
     * @return 区域标识符
     */
    String domain();
}
