package io.horizon.specification.storage;

import io.horizon.annotations.reference.One2One;
import io.horizon.specification.app.HUri;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

/**
 * 「抽象块存储接口」Block Store
 * <hr/>
 * 可搭载不同的块存储器，实现完整的块存储服务，主要服务于分布式文件系统，该接口负责：
 * <pre><code>
 *     1. 分布式块存储
 *     2. 基于底层硬件的块存储
 * </code></pre>
 *
 * @author lang : 2023-05-21
 */
public interface HBS extends HStore {
    /**
     * 块是有序的，所以直接使用 List 类型布局
     *
     * @return {@link List}
     */
    List<HBlock> blocks();

    /**
     * 当前节点的网络标识（分布式专用）
     *
     * @return {@link HUri}
     */
    @One2One
    HUri uri();

    /**
     * 父类
     *
     * @return {@link HBS}
     */
    HBS node();

    /**
     * 块可以支持父子级结构
     *
     * @return {@link ConcurrentMap}
     */
    ConcurrentMap<UUID, HBS> nodes();
}
